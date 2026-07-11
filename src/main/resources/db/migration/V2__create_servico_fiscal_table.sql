CREATE TABLE IF NOT EXISTS servico_fiscal (
    id BIGSERIAL PRIMARY KEY,
    codigo_servico_nacional VARCHAR(20) NOT NULL,
    descricao_servico_nacional VARCHAR(500) NOT NULL,
    codigo_nbs VARCHAR(20),
    descricao_nbs VARCHAR(500),
    indicador_ibscbs VARCHAR(50),
    codigo_servico_municipal VARCHAR(20),
    aliquota_iss NUMERIC(10, 4),
    municipio_ibge VARCHAR(7),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_servico_fiscal_codigo_nacional
    ON servico_fiscal (codigo_servico_nacional);

CREATE INDEX IF NOT EXISTS idx_servico_fiscal_codigo_nbs
    ON servico_fiscal (codigo_nbs);
