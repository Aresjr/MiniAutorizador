package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.SaqueCartaoRequest;
import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import br.com.vr.mini_autorizador.exception.CartaoInexistenteException;
import br.com.vr.mini_autorizador.exception.SaldoInsuficienteException;
import br.com.vr.mini_autorizador.exception.SenhaInvalidaCartaoException;
import br.com.vr.mini_autorizador.service.TransacoesService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransacoesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransacoesService transacoesService;

    @Value("${cartao.saldo.inicial:500.00}")
    private BigDecimal VALOR_PADRAO_INICIAL;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static String URL_TRANSACOES = "/transacoes";
    private final static String NUMERO_CARTAO = "6549873025634501";
    private final static String SENHA_CARTAO = "1234";
    private final static BigDecimal VALOR_SAQUE = BigDecimal.valueOf(100);
    private final static String USUARIO = "username";
    private final static String SENHA = "password";
    private final static String SENHA_ERRADA = "senha_errada";

    @Test
    void whenTransacaoCartaoComSaldo_shouldReturnOk() throws Exception {
        SaqueCartaoRequest saqueCartaoRequest = new SaqueCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO, VALOR_SAQUE);
        MensagemTransacaoCartao response = MensagemTransacaoCartao.OK;
        Mockito.when(transacoesService.realizarSaqueCartao(Mockito.any(SaqueCartaoRequest.class))).thenReturn(response);

        String request = objectMapper.writeValueAsString(saqueCartaoRequest);

        this.mockMvc.perform(
                post(URL_TRANSACOES)
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .with(httpBasic(USUARIO, SENHA))
            )
            .andExpect(status().isCreated())
            .andExpect(content().string(MensagemTransacaoCartao.OK.toString()));
    }

    @Test
    void whenTransacaoCartaoSenhaErrada_shouldReturnCredenciaisInvalidas() throws Exception {
        SaqueCartaoRequest saqueCartaoRequest = new SaqueCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO, VALOR_SAQUE);
        MensagemTransacaoCartao response = MensagemTransacaoCartao.OK;
        Mockito.when(transacoesService.realizarSaqueCartao(Mockito.any(SaqueCartaoRequest.class))).thenReturn(response);

        String request = objectMapper.writeValueAsString(saqueCartaoRequest);

        this.mockMvc.perform(
                post(URL_TRANSACOES)
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
    void whenTransacaoCartaoSaldoInsuficiente_shouldReturnSaldoInsuficiente() throws Exception {
        SaqueCartaoRequest saqueCartaoRequest = new SaqueCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO, VALOR_SAQUE);
        Mockito.when(transacoesService.realizarSaqueCartao(Mockito.any(SaqueCartaoRequest.class)))
                .thenThrow(new SaldoInsuficienteException());

        String request = objectMapper.writeValueAsString(saqueCartaoRequest);

        this.mockMvc.perform(
                post(URL_TRANSACOES)
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .with(httpBasic(USUARIO, SENHA))
            )
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().string(MensagemTransacaoCartao.SALDO_INSUFICIENTE.toString()));
    }

    @Test
    void whenTransacaoCartaoInexistente_shouldReturnCartaoInexistente() throws Exception {
        SaqueCartaoRequest saqueCartaoRequest = new SaqueCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO, VALOR_SAQUE);
        Mockito.when(transacoesService.realizarSaqueCartao(Mockito.any(SaqueCartaoRequest.class)))
                .thenThrow(new CartaoInexistenteException());

        String request = objectMapper.writeValueAsString(saqueCartaoRequest);

        this.mockMvc.perform(
                post(URL_TRANSACOES)
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .with(httpBasic(USUARIO, SENHA))
            )
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().string(MensagemTransacaoCartao.CARTAO_INEXISTENTE.toString()));
    }

    @Test
    void whenTransacaoCartaoSenhaInvalida_shouldReturnSenhaInvalida() throws Exception {
        SaqueCartaoRequest saqueCartaoRequest = new SaqueCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO, VALOR_SAQUE);
        Mockito.when(transacoesService.realizarSaqueCartao(Mockito.any(SaqueCartaoRequest.class)))
                .thenThrow(new SenhaInvalidaCartaoException());

        String request = objectMapper.writeValueAsString(saqueCartaoRequest);

        this.mockMvc.perform(
                post(URL_TRANSACOES)
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .with(httpBasic(USUARIO, SENHA))
            )
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().string(MensagemTransacaoCartao.SENHA_INVALIDA.toString()));
    }

}