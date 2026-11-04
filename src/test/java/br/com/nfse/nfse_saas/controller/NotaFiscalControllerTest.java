package br.com.nfse.nfse_saas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.com.nfse.nfse_saas.domain.Empresa;
import br.com.nfse.nfse_saas.domain.NotaFiscal;
import br.com.nfse.nfse_saas.nfse.service.NfseEmissaoService;
import br.com.nfse.nfse_saas.repository.EmpresaRepository;
import br.com.nfse.nfse_saas.repository.NotaFiscalRepository;
import br.com.nfse.nfse_saas.tenant.TenantContext;

class NotaFiscalControllerTest {

    private final NotaFiscalRepository repository = mock(NotaFiscalRepository.class);
    private final EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
    private final NotaFiscalController controller = new NotaFiscalController(
            repository, empresaRepository, mock(NfseEmissaoService.class));

    @AfterEach
    void limparTenant() {
        TenantContext.clear();
    }

    @Test
    void buscaNotaDoTenant() {
        TenantContext.setTenantId(7L);
        NotaFiscal nota = new NotaFiscal();
        nota.setId(3L);
        when(repository.findByIdAndEmpresaId(3L, 7L)).thenReturn(Optional.of(nota));

        assertThat(controller.buscar(3L)).isSameAs(nota);
    }

    @Test
    void retornaNotFoundQuandoNotaNaoExisteNoTenant() {
        TenantContext.setTenantId(7L);
        when(repository.findByIdAndEmpresaId(3L, 7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.buscar(3L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(error -> assertThat(((ResponseStatusException) error).getStatusCode())
                        .isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void atualizaSomenteCamposEditaveis() {
        TenantContext.setTenantId(7L);
        Empresa empresaOriginal = new Empresa();
        empresaOriginal.setId(7L);
        NotaFiscal existente = new NotaFiscal();
        existente.setId(3L);
        existente.setEmpresa(empresaOriginal);
        existente.setNumeroNfse(99L);
        existente.setCodigoVerificacao("codigo-original");
        existente.setStatus("RASCUNHO");
        existente.setRetornoEmissaoNfse("retorno-original");
        when(repository.findByIdAndEmpresaId(3L, 7L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Empresa empresaRecebida = new Empresa();
        empresaRecebida.setId(8L);
        NotaFiscal dados = new NotaFiscal();
        dados.setId(55L);
        dados.setEmpresa(empresaRecebida);
        dados.setNumeroNfse(100L);
        dados.setCodigoVerificacao("codigo-alterado");
        dados.setStatus("EMITIDA");
        dados.setDescricao("Servico atualizado");
        dados.setValor(new BigDecimal("125.00"));

        NotaFiscal atualizada = controller.atualizar(3L, dados);

        assertThat(atualizada.getDescricao()).isEqualTo("Servico atualizado");
        assertThat(atualizada.getValor()).isEqualByComparingTo("125.00");
        assertThat(atualizada.getId()).isEqualTo(3L);
        assertThat(atualizada.getEmpresa()).isSameAs(empresaOriginal);
        assertThat(atualizada.getNumeroNfse()).isEqualTo(99L);
        assertThat(atualizada.getCodigoVerificacao()).isEqualTo("codigo-original");
        assertThat(atualizada.getStatus()).isEqualTo("RASCUNHO");
        assertThat(atualizada.getRetornoEmissaoNfse()).isEqualTo("retorno-original");
        verify(repository).save(existente);
    }

    @Test
    void rejeitaEdicaoDeNotaEmitida() {
        TenantContext.setTenantId(7L);
        NotaFiscal emitida = new NotaFiscal();
        emitida.setStatus("EMITIDA");
        when(repository.findByIdAndEmpresaId(3L, 7L)).thenReturn(Optional.of(emitida));

        assertThatThrownBy(() -> controller.atualizar(3L, new NotaFiscal()))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(error -> assertThat(((ResponseStatusException) error).getStatusCode())
                        .isEqualTo(HttpStatus.CONFLICT));
    }

    @Test
    void postSempreCriaUmaNovaNota() {
        TenantContext.setTenantId(7L);
        Empresa empresa = new Empresa();
        empresa.setProximoNumeroRps(1L);
        NotaFiscal nota = new NotaFiscal();
        nota.setId(3L);
        when(empresaRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(empresa));
        when(repository.save(any(NotaFiscal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NotaFiscal criada = controller.salvar(nota);

        assertThat(criada.getId()).isNull();
        assertThat(criada.getStatus()).isEqualTo("RASCUNHO");
        assertThat(criada.getNumeroRps()).isEqualTo("1");
    }
}
