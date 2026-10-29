package br.com.nfse.nfse_saas.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.tenant.TenantFilter;
import jakarta.servlet.FilterChain;

class TenantFilterTest {

    @org.junit.jupiter.api.AfterEach
    void limparAutenticacao() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void bloqueiaEmpresaDeOutroUsuario() throws Exception {
        EmpresaRepository repository = mock(EmpresaRepository.class);
        TenantFilter filter = new TenantFilter(repository);
        MockHttpServletRequest request = request("99");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        autenticar("usuario@teste.com");
        when(repository.findByIdAndUsuarioEmailIgnoreCase(99L, "usuario@teste.com")).thenReturn(Optional.empty());

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("Empresa nao autorizada");
    }

    @Test
    void permiteEmpresaDoUsuarioAutenticado() throws Exception {
        EmpresaRepository repository = mock(EmpresaRepository.class);
        TenantFilter filter = new TenantFilter(repository);
        MockHttpServletRequest request = request("7");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        autenticar("usuario@teste.com");
        when(repository.findByIdAndUsuarioEmailIgnoreCase(7L, "usuario@teste.com"))
                .thenReturn(Optional.of(new Empresa()));

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    private MockHttpServletRequest request(String tenantId) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/notas");
        request.addHeader("X-Tenant-Id", tenantId);
        return request;
    }

    private void autenticar(String email) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList()));
    }
}
