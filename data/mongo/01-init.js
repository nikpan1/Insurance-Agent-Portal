// Mongo init for backend/audit module
// Schema mirrors com.policytracker.audit.core.AuditEventDocument

const dbName = process.env.MONGO_INITDB_DATABASE || 'policytracker';
db = db.getSiblingDB(dbName);

if (!db.getCollectionNames().includes('audit_events')) {
    db.createCollection('audit_events');
}

db.audit_events.createIndex({ userId: 1, timestamp: -1 });
db.audit_events.createIndex({ eventType: 1 });

db.audit_events.deleteMany({});

db.audit_events.insertMany([
    {
        userId: NumberLong(1),
        eventType: 'CLIENT_REGISTERED',
        timestamp: ISODate('2026-06-01T08:30:00Z'),
        metadata: { clientId: '1' }
    },
    {
        userId: NumberLong(2),
        eventType: 'CLIENT_REGISTERED',
        timestamp: ISODate('2026-06-01T09:15:00Z'),
        metadata: { clientId: '2' }
    },
    {
        userId: NumberLong(1),
        eventType: 'INSURANCE_USER_DATA_REQUESTED',
        timestamp: ISODate('2026-06-02T10:05:00Z'),
        metadata: {
            externalUserId: 'ext-1',
            correlationId: 'corr-1001'
        }
    },
    {
        userId: NumberLong(1),
        eventType: 'INSURANCE_STATUS_UPDATED',
        timestamp: ISODate('2026-06-02T10:10:00Z'),
        metadata: {
            policyId: 'POL-1001',
            status: 'ACTIVE',
            updatedAt: '2026-06-02T10:10:00Z'
        }
    },
    {
        userId: NumberLong(3),
        eventType: 'REFERENCE_DATA_IMPORTED',
        timestamp: ISODate('2026-06-03T07:45:00Z'),
        metadata: {
            source: 'classpath:data/reference-data.csv',
            importedRecords: '128',
            importedAt: '2026-06-03T07:45:00Z'
        }
    }
]);
