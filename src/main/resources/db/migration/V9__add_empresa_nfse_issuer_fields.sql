ALTER TABLE empresa
    ADD COLUMN IF NOT EXISTS nome_fantasia VARCHAR(255),
    ADD COLUMN IF NOT EXISTS inscricao_municipal VARCHAR(50),
    ADD COLUMN IF NOT EXISTS inscricao_estadual VARCHAR(50),
    ADD COLUMN IF NOT EXISTS situacao_cadastral VARCHAR(100),
    ADD COLUMN IF NOT EXISTS natureza_juridica VARCHAR(255),
    ADD COLUMN IF NOT EXISTS cnae_principal VARCHAR(20),
    ADD COLUMN IF NOT EXISTS descricao_cnae_principal VARCHAR(500),
    ADD COLUMN IF NOT EXISTS regime_tributario VARCHAR(50),
    ADD COLUMN IF NOT EXISTS regime_especial_tributacao VARCHAR(50),
    ADD COLUMN IF NOT EXISTS optante_simples_nacional BOOLEAN,
    ADD COLUMN IF NOT EXISTS incentivador_cultural BOOLEAN,
    ADD COLUMN IF NOT EXISTS incentivo_fiscal BOOLEAN,
    ADD COLUMN IF NOT EXISTS aliquota_iss NUMERIC(10, 4),
    ADD COLUMN IF NOT EXISTS item_lista_servico VARCHAR(20),
    ADD COLUMN IF NOT EXISTS codigo_tributacao_municipal VARCHAR(50),
    ADD COLUMN IF NOT EXISTS codigo_tributacao_nacional VARCHAR(50),
    ADD COLUMN IF NOT EXISTS descricao_servico_padrao TEXT,
    ADD COLUMN IF NOT EXISTS ambiente_emissao VARCHAR(20);

CREATE INDEX IF NOT EXISTS idx_empresa_cnpj ON empresa (cnpj);
CREATE INDEX IF NOT EXISTS idx_empresa_inscricao_municipal
    ON empresa (inscricao_municipal);
