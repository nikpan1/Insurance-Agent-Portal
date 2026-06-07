package com.policytracker.audit;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    @Id
    private String id;

    private Long clientId;

    private String eventType;

    private Instant createdAt;

}
