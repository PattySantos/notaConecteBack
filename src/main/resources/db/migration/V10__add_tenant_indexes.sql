CREATE INDEX IF NOT EXISTS idx_cliente_empresa_id ON cliente (empresa_id);
CREATE INDEX IF NOT EXISTS idx_produto_empresa_id ON produto (empresa_id);
CREATE INDEX IF NOT EXISTS idx_nota_fiscal_empresa_id ON nota_fiscal (empresa_id);
CREATE INDEX IF NOT EXISTS idx_nota_fiscal_empresa_numero
    ON nota_fiscal (empresa_id, numero_nfse);
