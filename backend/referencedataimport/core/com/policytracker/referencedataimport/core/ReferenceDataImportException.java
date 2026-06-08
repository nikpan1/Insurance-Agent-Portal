package com.policytracker.referencedataimport.core;

public class ReferenceDataImportException extends RuntimeException {

    public ReferenceDataImportException(String message) {
        super(message);
    }

    public ReferenceDataImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
