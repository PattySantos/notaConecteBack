package br.com.nfse.nfse_saas.nfse.soap;

import java.io.IOException;

public class NfseSoapException extends IOException {

    private final String respostaSanitizada;

    public NfseSoapException(int statusCode, String detalhe, String respostaSanitizada) {
        super("SGISS respondeu HTTP " + statusCode + (detalhe == null || detalhe.isBlank() ? "" : ": " + detalhe));
        this.respostaSanitizada = respostaSanitizada;
    }

    public String getRespostaSanitizada() {
        return respostaSanitizada;
    }
}
