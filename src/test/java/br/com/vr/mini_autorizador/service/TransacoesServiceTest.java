package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.SaqueCartaoRequest;
import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import br.com.vr.mini_autorizador.exception.CartaoInexistenteException;
import br.com.vr.mini_autorizador.exception.SaldoInsuficienteException;
import br.com.vr.mini_autorizador.exception.SenhaInvalidaCartaoException;
import br.com.vr.mini_autorizador.model.Cartao;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
class TransacoesServiceTest {

    @Autowired
    private TransacoesService transacoesService;

    @MockitoBean
    private CartaoRepository cartaoRepository;

    @Value("${cartao.saldo.inicial:500.00}")
    private BigDecimal VALOR_PADRAO_INICIAL;

    private Cartao cartao;

    private final static String NUMERO_CARTAO = "6549873025634501";
    private final static String NUMERO_CARTAO_INEXISTENTE = "65498730256345011111";
    private final static String SENHA_CARTAO = "1234";
    private final static String SENHA_CARTAO_INVALIDA = "123456";

    @BeforeEach
    void setup() {
        cartao = new Cartao(null, NUMERO_CARTAO, SENHA_CARTAO, VALOR_PADRAO_INICIAL);
        Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO))
                .thenReturn(Optional.of(cartao));
    }

    @Test
    void whenRealizarSaqueCartao_shouldReturnOk() {
        SaqueCartaoRequest request = new SaqueCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO, VALOR_PADRAO_INICIAL);
        MensagemTransacaoCartao response = transacoesService.realizarSaqueCartao(request);
        Assertions.assertEquals(response, MensagemTransacaoCartao.OK);
    }

    @Test
    void whenRealizarSaqueSenhaInvalida_shouldReturnSenhaInvalida() {
        SaqueCartaoRequest request = new SaqueCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO_INVALIDA, VALOR_PADRAO_INICIAL);

        Assertions.assertThrows(SenhaInvalidaCartaoException.class,
                () -> transacoesService.realizarSaqueCartao(request));
    }

    @Test
    void whenRealizarSaqueSaldoInsuficiente_shouldReturnSaldoInsuficiente() {
        BigDecimal valorAcimaLimite = VALOR_PADRAO_INICIAL.add(new BigDecimal(100));
        SaqueCartaoRequest request = new SaqueCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO, valorAcimaLimite);

        Assertions.assertThrows(SaldoInsuficienteException.class,
                () -> transacoesService.realizarSaqueCartao(request));
    }

    @Test
    void whenRealizarSaqueCartaoInexistente_shouldReturnCartaoInexistente() {
        SaqueCartaoRequest request = new SaqueCartaoRequest(NUMERO_CARTAO_INEXISTENTE, SENHA_CARTAO, VALOR_PADRAO_INICIAL);

        Assertions.assertThrows(CartaoInexistenteException.class,
                () -> transacoesService.realizarSaqueCartao(request));

    }

}