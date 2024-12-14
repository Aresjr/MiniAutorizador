package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.dto.CriarCartaoRequest;
import br.com.vr.mini_autorizador.exception.CartaoExistenteException;
import br.com.vr.mini_autorizador.model.Cartao;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CartaoServiceTest {

    @Autowired
    private CartaoService cartaoService;

    @MockitoBean
    private CartaoRepository cartaoRepository;

    @Value("${cartao.saldo.inicial:500.00}")
    private BigDecimal VALOR_PADRAO_INICIAL;

    private final static String NUMERO_CARTAO = "6549873025634501";
    private final static String SENHA_CARTAO = "1234";

    @Test
    void whenCriarCartao_shouldReturnCartao() {
        CriarCartaoRequest criarCartaoRequest = new CriarCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO);
        Mockito.when(cartaoRepository.findByNumeroCartao(criarCartaoRequest.getNumeroCartao()))
                .thenReturn(Optional.empty());

        Cartao cartao = new Cartao(criarCartaoRequest, VALOR_PADRAO_INICIAL);
        Mockito.when(cartaoRepository.save(Mockito.any(Cartao.class))).thenReturn(cartao);

        CartaoResponse response = cartaoService.criarCartao(criarCartaoRequest);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getNumeroCartao()).isEqualTo(NUMERO_CARTAO);
        Assertions.assertThat(response.getSenha()).isEqualTo(SENHA_CARTAO);
    }

    @Test
    void whenCriarCartaoJaExistente_shouldReturnCartaoExistente() {
        CriarCartaoRequest criarCartaoRequest = new CriarCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO);
        Cartao cartao = new Cartao(criarCartaoRequest, VALOR_PADRAO_INICIAL);
        Mockito.when(cartaoRepository.findByNumeroCartao(criarCartaoRequest.getNumeroCartao()))
                .thenReturn(Optional.of(cartao));

        Mockito.when(cartaoRepository.save(Mockito.any(Cartao.class))).thenReturn(cartao);

        CartaoExistenteException exception = assertThrows(CartaoExistenteException.class,
                () -> cartaoService.criarCartao(criarCartaoRequest));

        Assertions.assertThat(exception).isNotNull();
        Assertions.assertThat(exception.getCartao()).isEqualTo(cartao);
    }

    @Test
    void whenObterSaldoCartao_shouldReturnCartaoExistente() {
        Cartao cartao = new Cartao(null, NUMERO_CARTAO, SENHA_CARTAO, VALOR_PADRAO_INICIAL);
        Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO))
                .thenReturn(Optional.of(cartao));

        BigDecimal saldoCartao = cartaoService.obterSaldoCartao(NUMERO_CARTAO);
        Assertions.assertThat(saldoCartao).isEqualTo(VALOR_PADRAO_INICIAL);
    }

}