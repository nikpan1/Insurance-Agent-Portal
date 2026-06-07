// Switch to application database
db = db.getSiblingDB('policytracker');

// Collections and indexes
if (!db.getCollectionNames().includes('audit_logs')) {
    db.createCollection('audit_logs');
}
db.audit_logs.createIndex({ targetId: 1 });
db.audit_logs.createIndex({ timestamp: -1 });

if (!db.getCollectionNames().includes('client_interactions')) {
    db.createCollection('client_interactions');
}
db.client_interactions.createIndex({ clientId: 1 });
db.client_interactions.createIndex({ date: -1 });

// Seed Audit Logs (mix of structured and dynamic payloads)
db.audit_logs.insertMany([
    {
        timestamp: new ISODate(),
        action: "CLIENT_REGISTERED",
        details: "Registered John Doe",
        targetId: "90010112345",
        agentId: "system"
    },
    {
        timestamp: new ISODate(),
        action: "CLIENT_REGISTERED",
        details: "Registered Jane Smith",
        targetId: "85020298765",
        agentId: "system"
    },
    {
        timestamp: new ISODate(),
        action: "POLICY_CREATED",
        details: "Created LIFE policy for John Doe",
        targetId: "policy_1001",
        agentId: "underwriter_a",
        metadata: { channel: "portal" }
    },
    {
        timestamp: new ISODate(),
        action: "POLICY_STATUS_CHANGED",
        details: "Policy moved from DRAFT to ACTIVE",
        targetId: "policy_1001",
        agentId: "agent_smith",
        changes: { old_status: "DRAFT", new_status: "ACTIVE" },
        metadata: { ip_address: "192.168.1.105", user_agent: "Mozilla/5.0" }
    },
    {
        timestamp: new ISODate(),
        action: "PAYMENT_RECEIVED",
        details: "Initial premium payment",
        targetId: "policy_1001",
        agentId: "payments_service",
        payment: { amount: 1200.00, currency: "USD", reference: "TXN-99887766" }
    },
    {
        timestamp: new ISODate(),
        action: "CLIENT_UPDATED",
        details: "Updated contact phone",
        targetId: "90010112345",
        agentId: "service_rep",
        diff: { phone: { old: null, new: "+1-555-1234" } }
    },
    {
        timestamp: new ISODate(),
        action: "CLAIM_FILED",
        details: "Minor auto claim filed",
        targetId: "claim_2001",
        agentId: "agent_jones",
        metadata: { severity: "LOW", location: "NY" }
    },
    {
        timestamp: new ISODate(),
        action: "INTERACTION_LOGGED",
        details: "Recorded inbound email conversation",
        targetId: "90010112345",
        agentId: "inbox_bot",
        raw: { subject: "Question about coverage", body: "Can I add spouse?" }
    }
]);

// Seed Client Interactions (unstructured/dynamic payloads)
db.client_interactions.insertMany([
    {
        clientId: "90010112345",
        interaction_type: "PHONE_CALL",
        date: new ISODate(),
        duration_seconds: 450,
        notes: "Client inquired about adding a spouse to the life insurance policy. Needs quote next week.",
        sentiment: "POSITIVE",
        agentId: "service_rep"
    },
    {
        clientId: "90010112345",
        interaction_type: "EMAIL",
        date: new ISODate(),
        subject: "Welcome to Comarch Insurance",
        body_snippet: "Thank you for choosing our services...",
        delivery_status: "DELIVERED",
        agentId: "marketing"
    },
    {
        clientId: "85020298765",
        interaction_type: "CHAT",
        date: new ISODate(),
        transcript: [
            { who: "client", text: "How do I update beneficiary?" },
            { who: "agent", text: "You can update it from your account page." }
        ],
        sentiment: "NEUTRAL",
        agentId: "chatbot"
    },
    {
        clientId: "78030355443",
        interaction_type: "IN_PERSON",
        date: new ISODate(),
        notes: "Signed documents for policy activation.",
        location: "Branch #12",
        agentId: "branch_user_12"
    }
]);

// Helpful sample queries (commented):
// db.audit_logs.find({ targetId: '90010112345' }).sort({ timestamp: -1 }).limit(10)
// db.client_interactions.find({ clientId: '90010112345' }).pretty()