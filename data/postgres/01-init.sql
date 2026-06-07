-- Postgres init: clients, policies, payments with expanded seed data
CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    national_id VARCHAR(11) UNIQUE NOT NULL,
    risk_score DECIMAL(3,2) DEFAULT 1.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS policies (
    id SERIAL PRIMARY KEY,
    client_id INT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    policy_type VARCHAR(20) CHECK (policy_type IN ('LIFE', 'AUTO', 'HOME', 'HEALTH')),
    status VARCHAR(20) CHECK (status IN ('DRAFT', 'ACTIVE', 'EXPIRED', 'CANCELLED')),
    coverage_limit DECIMAL(12,2) NOT NULL,
    premium_amount DECIMAL(10,2) NOT NULL,
    effective_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    policy_id INT NOT NULL REFERENCES policies(id) ON DELETE CASCADE,
    amount_paid DECIMAL(10,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),
    transaction_reference VARCHAR(100) UNIQUE
);

-- Seed clients
INSERT INTO clients (first_name, last_name, email, national_id, risk_score) VALUES
('John', 'Doe', 'john.doe@example.com', '90010112345', 1.05),
('Jane', 'Smith', 'jane.smith@example.com', '85020298765', 0.95),
('Robert', 'Jones', 'robert.jones@example.com', '78030355443', 1.10),
('Alice', 'Lee', 'alice.lee@example.com', '92040433221', 0.90),
('Maria', 'Garcia', 'maria.garcia@example.com', '94050566778', 1.25),
('Ahmed', 'Khan', 'ahmed.khan@example.com', '88060644556', 0.85)
ON CONFLICT (national_id) DO NOTHING;

-- Seed policies
INSERT INTO policies (client_id, policy_type, status, coverage_limit, premium_amount, effective_date, expiry_date) VALUES
(1, 'LIFE', 'ACTIVE', 500000.00, 1200.00, '2024-01-01', '2034-01-01'),
(2, 'AUTO', 'ACTIVE', 30000.00, 450.00, '2025-03-01', '2026-03-01'),
(3, 'HOME', 'DRAFT', 250000.00, 980.00, '2024-06-15', '2034-06-15'),
(4, 'HEALTH', 'ACTIVE', 100000.00, 200.00, '2024-02-01', '2025-02-01'),
(5, 'LIFE', 'CANCELLED', 150000.00, 600.00, '2020-01-01', '2030-01-01'),
(1, 'AUTO', 'ACTIVE', 40000.00, 520.00, '2024-05-01', '2025-05-01')
ON CONFLICT DO NOTHING;

-- Seed payments
INSERT INTO payments (policy_id, amount_paid, status, transaction_reference) VALUES
(1, 1200.00, 'COMPLETED', 'TXN-99887766'),
(2, 450.00, 'COMPLETED', 'TXN-99880001'),
(4, 200.00, 'PENDING', 'TXN-99880002'),
(6, 520.00, 'COMPLETED', 'TXN-99880003'),
(1, 1200.00, 'COMPLETED', 'TXN-99880004')
ON CONFLICT (transaction_reference) DO NOTHING;

-- Helpful queries (commented): 
-- SELECT * FROM clients ORDER BY created_at DESC LIMIT 10;
-- SELECT p.*, c.first_name, c.last_name FROM policies p JOIN clients c ON c.id = p.client_id WHERE c.national_id = '90010112345';