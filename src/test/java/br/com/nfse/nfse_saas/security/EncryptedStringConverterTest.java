package br.com.nfse.nfse_saas.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class EncryptedStringConverterTest {

    private final byte[] key = "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8);

    @Test
    void criptografaComIvAleatorioERecuperaValor() {
        EncryptedStringConverter converter = new EncryptedStringConverter(key);
        String first = converter.convertToDatabaseColumn("segredo-fiscal");
        String second = converter.convertToDatabaseColumn("segredo-fiscal");

        assertThat(first).startsWith(EncryptedStringConverter.PREFIX).isNotEqualTo(second);
        assertThat(converter.convertToEntityAttribute(first)).isEqualTo("segredo-fiscal");
        assertThat(first).doesNotContain("segredo-fiscal");
    }

    @Test
    void detectaChaveIncorretaOuPayloadAdulterado() {
        EncryptedStringConverter converter = new EncryptedStringConverter(key);
        String encrypted = converter.convertToDatabaseColumn("segredo-fiscal");
        byte[] otherKey = "abcdef0123456789abcdef0123456789".getBytes(StandardCharsets.UTF_8);

        assertThatThrownBy(() -> new EncryptedStringConverter(otherKey).convertToEntityAttribute(encrypted))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void permiteLeituraTemporariaDeRegistroLegado() {
        assertThat(new EncryptedStringConverter(key).convertToEntityAttribute("texto-legado"))
                .isEqualTo("texto-legado");
    }
}
