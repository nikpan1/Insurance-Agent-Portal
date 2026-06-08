package com.policytracker.audit.core;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditEventRepository extends MongoRepository<AuditEventDocument, String> {

    List<AuditEventDocument> findByUserIdOrderByTimestampDesc(Long userId);
}
