package br.com.nfse.nfse_saas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NfseSaasApplicationTests {

	static {
		System.setProperty("nfse.certificate-encryption-key", "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=");
		System.setProperty("JWT_SECRET", "test-jwt-secret-with-at-least-32-characters");
		System.setProperty("nfse.security.migrate-legacy-credentials", "false");
	}

	@Test
	void contextLoads() {
	}

}
