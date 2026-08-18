-- The original schema was created by Hibernate. Flyway runs before Hibernate,
-- though, so a new database needs the base tables before the ALTER migrations.
CREATE TABLE IF NOT EXISTS endereco (
    id BIGSERIAL PRIMARY KEY,
    logradouro VARCHAR(255),
    numero VARCHAR(255),
    bairro VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(255),
    cep VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS empresa (
    id BIGSERIAL PRIMARY KEY,
    cnpj VARCHAR(255),
    razao_social VARCHAR(255),
    usuario_id BIGINT REFERENCES usuario (id),
    endereco_id BIGINT REFERENCES endereco (id)
);

CREATE TABLE IF NOT EXISTS cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    documento VARCHAR(255),
    email VARCHAR(255),
    empresa_id BIGINT REFERENCES empresa (id),
    endereco_id BIGINT REFERENCES endereco (id)
);

CREATE TABLE IF NOT EXISTS produto (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    codigo VARCHAR(255),
    valor NUMERIC(38, 2),
    empresa_id BIGINT NOT NULL REFERENCES empresa (id)
);

CREATE TABLE IF NOT EXISTS nota_fiscal (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255),
    valor NUMERIC(38, 2),
    iss NUMERIC(38, 2),
    codigo_servico VARCHAR(255),
    codigo_servico_nacional VARCHAR(255),
    codigo_nbs VARCHAR(255),
    indicador_ibscbs VARCHAR(255),
    municipio VARCHAR(255),
    status VARCHAR(255),
    empresa_id BIGINT REFERENCES empresa (id)
);

ALTER TABLE produto
    ADD COLUMN IF NOT EXISTS servico_fiscal_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_produto_servico_fiscal'
          AND conrelid = 'produto'::regclass
    ) THEN
        ALTER TABLE produto
            ADD CONSTRAINT fk_produto_servico_fiscal
            FOREIGN KEY (servico_fiscal_id)
            REFERENCES servico_fiscal (id);
    END IF;
END
$$;
