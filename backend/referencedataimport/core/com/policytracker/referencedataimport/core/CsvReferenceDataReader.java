package com.policytracker.referencedataimport.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CsvReferenceDataReader {

    public List<String> readAllLines(Path csvPath) {
        try {
            return Files.readAllLines(csvPath);
        } catch (IOException ex) {
            throw new ReferenceDataImportException("Failed to read CSV file: " + csvPath, ex);
        }
    }
}
