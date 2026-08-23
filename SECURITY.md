# Configuracao de seguranca

O backend exige dois segredos externos. Eles nao devem ser gravados no Git, no banco ou em arquivos servidos pela aplicacao.

## Variaveis obrigatorias

- `JWT_SECRET`: texto aleatorio com pelo menos 32 caracteres.
- `NFSE_CERTIFICATE_ENCRYPTION_KEY`: exatamente 32 bytes codificados em Base64.

Exemplo para gerar a chave de certificados no PowerShell:

```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
[Convert]::ToBase64String($bytes)
```

Use um gerenciador de segredos do ambiente de hospedagem. A perda de `NFSE_CERTIFICATE_ENCRYPTION_KEY` impede a recuperacao dos certificados armazenados.

## Banco de dados

Configure `DATABASE_URL`, `DATABASE_USERNAME` e `DATABASE_PASSWORD`. Ao iniciar com a chave correta, registros legados de certificado e senha sao migrados automaticamente para AES-256-GCM no formato `enc:v1:`.

Antes de trocar a chave de criptografia, mantenha backup seguro e execute uma rotina explicita de rotacao. Nao substitua a variavel diretamente enquanto existirem certificados criptografados com a chave anterior.
