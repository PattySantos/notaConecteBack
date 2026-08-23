package br.com.nfse.nfse_saas.security;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    static final String PREFIX = "enc:v1:";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH_BITS = 128;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final SecretKeySpec key;

    public EncryptedStringConverter() {
        this(resolveKey());
    }

    EncryptedStringConverter(byte[] keyBytes) {
        if (keyBytes == null || keyBytes.length != 32) {
            throw new IllegalStateException("NFSE_CERTIFICATE_ENCRYPTION_KEY deve ser uma chave Base64 de 32 bytes");
        }
        this.key = new SecretKeySpec(keyBytes, "AES");
    }

    @Override
    public String convertToDatabaseColumn(String value) {
        if (value == null || value.isBlank()) return value;
        if (value.startsWith(PREFIX)) return value;
        try {
            byte[] iv = new byte[IV_LENGTH];
            RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            ByteBuffer payload = ByteBuffer.allocate(iv.length + encrypted.length).put(iv).put(encrypted);
            return PREFIX + Base64.getEncoder().encodeToString(payload.array());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Nao foi possivel proteger o dado fiscal", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String value) {
        if (value == null || value.isBlank() || !value.startsWith(PREFIX)) return value;
        try {
            byte[] payload = Base64.getDecoder().decode(value.substring(PREFIX.length()));
            if (payload.length <= IV_LENGTH) throw new IllegalArgumentException("Payload criptografado invalido");
            ByteBuffer buffer = ByteBuffer.wrap(payload);
            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);
            byte[] encrypted = new byte[buffer.remaining()];
            buffer.get(encrypted);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new IllegalStateException("Dado fiscal criptografado invalido ou chave incorreta", e);
        }
    }

    private static byte[] resolveKey() {
        String encoded = System.getenv("NFSE_CERTIFICATE_ENCRYPTION_KEY");
        if (encoded == null || encoded.isBlank()) {
            encoded = System.getProperty("nfse.certificate-encryption-key");
        }
        if (encoded == null || encoded.isBlank()) {
            throw new IllegalStateException("Configure NFSE_CERTIFICATE_ENCRYPTION_KEY antes de armazenar certificados");
        }
        try {
            return Base64.getDecoder().decode(encoded);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("NFSE_CERTIFICATE_ENCRYPTION_KEY nao esta em Base64 valido", e);
        }
    }
}
