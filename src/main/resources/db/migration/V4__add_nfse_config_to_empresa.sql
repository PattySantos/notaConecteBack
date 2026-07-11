ALTER TABLE empresa
    ADD COLUMN IF NOT EXISTS certificado_digital_nome VARCHAR(255),
    ADD COLUMN IF NOT EXISTS certificado_digital_base64 TEXT,
    ADD COLUMN IF NOT EXISTS senha_certificado_digital VARCHAR(255),
    ADD COLUMN IF NOT EXISTS proximo_numero_nfse BIGINT;