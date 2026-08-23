package br.com.nfse.nfse_saas.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Set;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;

@Component
public class TenantFilter extends OncePerRequestFilter {
    private static final Set<String> TENANT_PATHS = Set.of("/clientes", "/produtos", "/notas");
    private final EmpresaRepository empresaRepository;

    public TenantFilter(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod()) ||
                TENANT_PATHS.stream().noneMatch(request.getRequestURI()::startsWith);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            long tenantId = Long.parseLong(request.getHeader("X-Tenant-Id"));
            if (tenantId < 1) throw new NumberFormatException();
            if (request.getUserPrincipal() == null || empresaRepository
                    .findByIdAndUsuarioEmailIgnoreCase(tenantId, request.getUserPrincipal().getName())
                    .isEmpty()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"erro\":\"Empresa nao autorizada para o usuario autenticado\"}");
                return;
            }
            TenantContext.setTenantId(tenantId);
            filterChain.doFilter(request, response);
        } catch (NumberFormatException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\":\"Cabecalho X-Tenant-Id obrigatorio e deve ser numerico\"}");
        } finally {
            TenantContext.clear();
        }
    }
}
