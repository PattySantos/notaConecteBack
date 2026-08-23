package br.com.nfse.nfse_saas.security;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CertificateValidator {

    private static final int MAX_CERTIFICATE_BYTES = 10 * 1024 * 1024;

    public void validarA1(String certificateBase64, String password) {
        if (certificateBase64 == null || certificateBase64.isBlank() || password == null || password.isBlank()) {
            throw badRequest("Certificado A1 e senha devem ser informados juntos.");
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(certificateBase64);
            if (bytes.length == 0 || bytes.length > MAX_CERTIFICATE_BYTES) {
                throw badRequest("O certificado deve possuir no maximo 10 MB.");
            }
            char[] passwordChars = password.toCharArray();
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new ByteArrayInputStream(bytes), passwordChars);

            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (!keyStore.isKeyEntry(alias)) continue;
                if (!(keyStore.getKey(alias, passwordChars) instanceof PrivateKey)) continue;
                if (!(keyStore.getCertificate(alias) instanceof X509Certificate certificate)) continue;
                certificate.checkValidity();
                return;
            }
            throw badRequest("O arquivo nao contem certificado A1 com chave privada.");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (java.security.cert.CertificateExpiredException e) {
            throw badRequest("O certificado digital esta expirado.");
        } catch (java.security.cert.CertificateNotYetValidException e) {
            throw badRequest("O certificado digital ainda nao e valido.");
        } catch (Exception e) {
            throw badRequest("Certificado PFX/P12 invalido ou senha incorreta.");
        }
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
