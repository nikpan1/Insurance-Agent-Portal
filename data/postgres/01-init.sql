-- Postgres init for backend modules using relational storage.
-- Supports both fresh bootstrap and compatibility with older schemas.

CREATE TABLE IF NOT EXISTS clients (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    national_id VARCHAR(11) NOT NULL UNIQUE,
    CONSTRAINT clients_national_id_format CHECK (national_id ~ '^[0-9]{11}$')
);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'clients'
          AND column_name = 'pesel'
    )
    AND NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'clients'
          AND column_name = 'national_id'
    ) THEN
        ALTER TABLE clients RENAME COLUMN pesel TO national_id;
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'clients_national_id_format'
    ) THEN
        ALTER TABLE clients
            ADD CONSTRAINT clients_national_id_format CHECK (national_id ~ '^[0-9]{11}$');
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS system_properties (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    property_key VARCHAR(120) NOT NULL,
    property_value VARCHAR(500) NOT NULL,
    version BIGINT,
    CONSTRAINT uk_system_properties_customer_property UNIQUE (customer_id, property_key)
);

INSERT INTO clients (id, first_name, last_name, email, national_id) VALUES
    (1, 'Jan', 'Kowalski', 'jan.kowalski@example.com', '90010112345'),
    (2, 'Anna', 'Nowak', 'anna.nowak@example.com', '85020298765'),
    (3, 'Piotr', 'Wisniewski', 'piotr.wisniewski@example.com', '78030355443'),
    (4, 'Katarzyna', 'Wojcik', 'katarzyna.wojcik@example.com', '92040433221'),
    (5, 'Marek', 'Kaminski', 'marek.kaminski@example.com', '94050566778')
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('clients', 'id'), COALESCE(MAX(id), 1), true)
FROM clients;
