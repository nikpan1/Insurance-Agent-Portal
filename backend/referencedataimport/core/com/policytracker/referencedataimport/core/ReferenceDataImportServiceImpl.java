package com.policytracker.referencedataimport.core;

import com.policytracker.common.architecture.DomainCore;
import com.policytracker.events.api.ReferenceDataImportedEvent;
import com.policytracker.referencedataimport.api.ReferenceDataImportResult;
import com.policytracker.referencedataimport.api.ReferenceDataImportService;
import com.policytracker.referencedataimport.api.ReferenceDataNode;
import com.policytracker.requestcontext.CurrentUserContext;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferenceDataImportServiceImpl implements ReferenceDataImportService, DomainCore {

    private static final int MIN_COLUMNS = 3;

    private final CsvReferenceDataReader csvReferenceDataReader;
    private final CurrentUserContext currentUserContext;
    private final ApplicationEventPublisher eventPublisher;

    private volatile List<ReferenceDataNode> currentTreeRoots = List.of();

    @Override
    public ReferenceDataImportResult importFromCsv(String sourcePath) {
        List<String> lines = csvReferenceDataReader.readAllLines(java.nio.file.Path.of(sourcePath));
        if (lines.size() <= 1) {
            throw new ReferenceDataImportException("CSV file is empty or missing data rows: " + sourcePath);
        }

        List<ReferenceDataCsvRecord> records = parseRecords(lines);
        Map<String, MutableNode> indexedNodes = indexNodes(records);
        List<ReferenceDataNode> roots = buildTree(records, indexedNodes);
        Instant importedAt = Instant.now();

        currentTreeRoots = roots;
        publishImportedEvent(sourcePath, records.size(), importedAt);

        return new ReferenceDataImportResult(sourcePath, records.size(), importedAt, roots);
    }

    @Override
    public List<ReferenceDataNode> getCurrentTreeRoots() {
        return currentTreeRoots;
    }

    private List<ReferenceDataCsvRecord> parseRecords(List<String> lines) {
        return IntStream.range(1, lines.size())
                .parallel()
                .mapToObj(lineIndex -> parseRecord(lines.get(lineIndex), lineIndex + 1))
                .toList();
    }

    private ReferenceDataCsvRecord parseRecord(String line, int lineNumber) {
        String[] columns = line.split(",", -1);
        if (columns.length < MIN_COLUMNS) {
            throw new ReferenceDataImportException("Invalid CSV line %d, expected at least 3 columns".formatted(lineNumber));
        }

        String id = columns[0].trim();
        String parentId = columns[1].trim();
        String name = columns[2].trim();

        if (id.isEmpty() || name.isEmpty()) {
            throw new ReferenceDataImportException("Invalid CSV line %d, id and name are required".formatted(lineNumber));
        }

        return new ReferenceDataCsvRecord(lineNumber, id, parentId.isEmpty() ? null : parentId, name);
    }

    private Map<String, MutableNode> indexNodes(List<ReferenceDataCsvRecord> records) {
        Map<String, MutableNode> nodes = new ConcurrentHashMap<>();
        records.parallelStream().forEach(record -> {
            MutableNode previous = nodes.putIfAbsent(record.id(), new MutableNode(record.id(), record.parentId(), record.name()));
            if (previous != null) {
                throw new ReferenceDataImportException(
                        "Duplicate node id '%s' in CSV around line %d".formatted(record.id(), record.lineNumber())
                );
            }
        });
        return nodes;
    }

    private List<ReferenceDataNode> buildTree(List<ReferenceDataCsvRecord> records, Map<String, MutableNode> indexedNodes) {
        List<MutableNode> roots = new CopyOnWriteArrayList<>();

        records.parallelStream().forEach(record -> {
            MutableNode current = indexedNodes.get(record.id());
            if (record.parentId() == null) {
                roots.add(current);
                return;
            }

            MutableNode parent = indexedNodes.get(record.parentId());
            if (parent == null) {
                throw new ReferenceDataImportException(
                        "Missing parent '%s' for node '%s' (line %d)".formatted(record.parentId(), record.id(), record.lineNumber())
                );
            }
            parent.children.add(current);
        });

        return roots.stream()
                .map(MutableNode::toImmutable)
                .toList();
    }

    private void publishImportedEvent(String sourcePath, int importedRecords, Instant importedAt) {
        Long userId = currentUserContext.getUserId() != null ? currentUserContext.getUserId().value() : null;

        eventPublisher.publishEvent(ReferenceDataImportedEvent.builder()
                .userId(userId)
                .source(sourcePath)
                .importedRecords(importedRecords)
                .importedAt(importedAt)
                .build());
    }

    private static final class MutableNode {
        private final String id;
        private final String parentId;
        private final String name;
        private final List<MutableNode> children = new CopyOnWriteArrayList<>();

        private MutableNode(String id, String parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        private ReferenceDataNode toImmutable() {
            List<ReferenceDataNode> immutableChildren = children.stream()
                    .filter(Objects::nonNull)
                    .map(MutableNode::toImmutable)
                    .toList();
            return new ReferenceDataNode(id, parentId, name, Collections.unmodifiableList(new ArrayList<>(immutableChildren)));
        }
    }
}
