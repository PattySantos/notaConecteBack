ALTER TABLE nota_fiscal
    ADD COLUMN IF NOT EXISTS cliente_id BIGINT,
    ADD COLUMN IF NOT EXISTS retorno_emissao_nfse TEXT;