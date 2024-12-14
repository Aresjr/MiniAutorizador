package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.dto.CriarCartaoRequest;
import br.com.vr.mini_autorizador.exception.CartaoExistenteException;
import br.com.vr.mini_autorizador.exception.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.model.Cartao;
import br.com.vr.mini_autorizador.service.CartaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartaoService cartaoService;

    @Value("${cartao.saldo.inicial:500.00}")
    private BigDecimal VALOR_PADRAO_INICIAL;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static String URL_CARTOES = "/cartoes";
    private final static String URL_SALDO_CARTAO = URL_CARTOES + "/{numeroCartao}";
    private final static String NUMERO_CARTAO = "6549873025634501";
    private final static String NUMERO_CARTAO_INEXISTENTE = "6549873025634501111";
    private final static String SENHA_CARTAO = "1234";
    private final static String USUARIO = "username";
    private final static String SENHA = "password";
    private final static String SENHA_ERRADA = "senha_errada";

    @Test
    void whenCriarCartao_shouldReturnOkWithCartao() throws Exception {
        CriarCartaoRequest criarCartaoRequest = new CriarCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO);
        CartaoResponse response = new CartaoResponse(new Cartao(criarCartaoRequest, VALOR_PADRAO_INICIAL));
        Mockito.when(cartaoService.criarCartao(Mockito.any(CriarCartaoRequest.class))).thenReturn(response);

        String request = objectMapper.writeValueAsString(criarCartaoRequest);

        this.mockMvc.perform(
                    post(URL_CARTOES)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(httpBasic(USUARIO, SENHA))
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenCriarCartaoCredenciaisInvalidas_shouldReturnCredenciaisInvalidas() throws Exception {
        CriarCartaoRequest criarCartaoRequest = new CriarCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO);
        CartaoResponse response = new CartaoResponse(new Cartao(criarCartaoRequest, VALOR_PADRAO_INICIAL));
        Mockito.when(cartaoService.criarCartao(Mockito.any(CriarCartaoRequest.class))).thenReturn(response);

        String request = objectMapper.writeValueAsString(criarCartaoRequest);

        this.mockMvc.perform(
                    post(URL_CARTOES)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                            .with(httpBasic(USUARIO, SENHA_ERRADA))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    void whenCriarCartaoExistente_shouldReturnException() throws Exception {
        CriarCartaoRequest criarCartaoRequest = new CriarCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO);
        CartaoResponse response = new CartaoResponse(new Cartao(criarCartaoRequest, VALOR_PADRAO_INICIAL));
        Mockito.when(cartaoService.criarCartao(Mockito.any(CriarCartaoRequest.class)))
                .thenThrow(new CartaoExistenteException(new Cartao(criarCartaoRequest, VALOR_PADRAO_INICIAL)));

        String request = objectMapper.writeValueAsString(criarCartaoRequest);

        this.mockMvc.perform(
                    post(URL_CARTOES)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(httpBasic(USUARIO, SENHA))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void whenObterSaldoCartao_shouldReturnSaldo() throws Exception {
        Mockito.when(cartaoService.obterSaldoCartao(NUMERO_CARTAO)).thenReturn(VALOR_PADRAO_INICIAL);

        this.mockMvc.perform(
                    get(URL_SALDO_CARTAO, NUMERO_CARTAO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(httpBasic(USUARIO, SENHA))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(VALOR_PADRAO_INICIAL)));
    }

    @Test
    void whenObterSaldoCartaoCredenciaisInvalidas_shouldReturnCredenciaisInvalidas() throws Exception {
        Mockito.when(cartaoService.obterSaldoCartao(NUMERO_CARTAO)).thenReturn(VALOR_PADRAO_INICIAL);

        this.mockMvc.perform(
                    get(URL_SALDO_CARTAO, NUMERO_CARTAO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(httpBasic(USUARIO, SENHA_ERRADA))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    void whenObterSaldoCartaoInexistente_shouldReturnCartaoNaoEncontrado() throws Exception {
        Mockito.when(cartaoService.obterSaldoCartao(NUMERO_CARTAO_INEXISTENTE))
                .thenThrow(new CartaoNaoEncontradoException(NUMERO_CARTAO_INEXISTENTE));

        this.mockMvc.perform(
                    get(URL_SALDO_CARTAO, NUMERO_CARTAO_INEXISTENTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(httpBasic(USUARIO, SENHA))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}