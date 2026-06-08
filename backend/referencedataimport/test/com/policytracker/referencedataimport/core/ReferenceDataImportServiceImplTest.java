package com.policytracker.referencedataimport.core;

import com.policytracker.referencedataimport.api.ReferenceDataImportResult;
import com.policytracker.requestcontext.CurrentUserContext;
import com.policytracker.requestcontext.UserId;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferenceDataImportServiceImplTest {

    @Mock
    private CsvReferenceDataReader csvReferenceDataReader;

    @Mock
    private CurrentUserContext currentUserContext;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ReferenceDataImportServiceImpl service;

    @Test
    void importFromCsvBuildsTreeAndPublishesEvent() {
        when(csvReferenceDataReader.readAllLines(Path.of("data.csv"))).thenReturn(List.of(
                "id,parentId,name",
                "ROOT,,Root",
                "CHILD,ROOT,Child"
        ));
        when(currentUserContext.getUserId()).thenReturn(new UserId(9L));

        ReferenceDataImportResult result = service.importFromCsv("data.csv");

        assertThat(result.importedRecords()).isEqualTo(2);
        assertThat(result.roots()).hasSize(1);
        assertThat(result.roots().get(0).children()).hasSize(1);
        verify(eventPublisher).publishEvent(any(Object.class));
    }
}
