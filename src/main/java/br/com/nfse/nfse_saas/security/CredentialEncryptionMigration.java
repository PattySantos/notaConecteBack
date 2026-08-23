package br.com.nfse.nfse_saas.security;

import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "nfse.security.migrate-legacy-credentials", havingValue = "true", matchIfMissing = true)
public class CredentialEncryptionMigration implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final EncryptedStringConverter converter;

    public CredentialEncryptionMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.converter = new EncryptedStringConverter();
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
                select id, certificado_digital_base64, senha_certificado_digital
                from empresa
                where (certificado_digital_base64 is not null and certificado_digital_base64 not like 'enc:v1:%')
                   or (senha_certificado_digital is not null and senha_certificado_digital not like 'enc:v1:%')
                """);

        for (Map<String, Object> row : rows) {
            String certificate = (String) row.get("certificado_digital_base64");
            String password = (String) row.get("senha_certificado_digital");
            jdbcTemplate.update("""
                    update empresa
                    set certificado_digital_base64 = ?, senha_certificado_digital = ?
                    where id = ?
                    """,
                    converter.convertToDatabaseColumn(certificate),
                    converter.convertToDatabaseColumn(password),
                    row.get("id")
            );
        }
    }
}
