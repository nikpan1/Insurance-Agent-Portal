-- Postgres init for backend/client module
-- Schema mirrors com.policytracker.client.Client

CREATE TABLE IF NOT EXISTS clients (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    pesel VARCHAR(11) NOT NULL UNIQUE,
    CONSTRAINT clients_pesel_format CHECK (pesel ~ '^[0-9]{11}$')
);

INSERT INTO clients (id, first_name, last_name, email, pesel) VALUES
    (1, 'Jan', 'Kowalski', 'jan.kowalski@example.com', '90010112345'),
    (2, 'Anna', 'Nowak', 'anna.nowak@example.com', '85020298765'),
    (3, 'Piotr', 'Wisniewski', 'piotr.wisniewski@example.com', '78030355443'),
    (4, 'Katarzyna', 'Wojcik', 'katarzyna.wojcik@example.com', '92040433221'),
    (5, 'Marek', 'Kaminski', 'marek.kaminski@example.com', '94050566778')
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('clients', 'id'), COALESCE(MAX(id), 1), true)
FROM clients;
