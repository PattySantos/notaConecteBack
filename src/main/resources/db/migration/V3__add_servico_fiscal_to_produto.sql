ALTER TABLE produto
    ADD COLUMN IF NOT EXISTS servico_fiscal_id BIGINT;

ALTER TABLE produto
    ADD CONSTRAINT fk_produto_servico_fiscal
    FOREIGN KEY (servico_fiscal_id)
    REFERENCES servico_fiscal (id);
