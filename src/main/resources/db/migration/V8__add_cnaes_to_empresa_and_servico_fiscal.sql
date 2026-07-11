ALTER TABLE empresa
    ADD COLUMN IF NOT EXISTS cnaes TEXT;

ALTER TABLE servico_fiscal
    ADD COLUMN IF NOT EXISTS cnaes_relacionados TEXT;
