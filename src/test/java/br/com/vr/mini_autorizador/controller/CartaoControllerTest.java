package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.dto.CriarCartaoRequest;
import br.com.vr.mini_autorizador.model.Cartao;
import br.com.vr.mini_autorizador.service.CartaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static String URL_CARTOES = "/cartoes";
    private final static String NUMERO_CARTAO = "6549873025634501";
    private final static String SENHA_CARTAO = "1234";

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        CriarCartaoRequest criarCartaoRequest = new CriarCartaoRequest(NUMERO_CARTAO, SENHA_CARTAO);
        CartaoResponse response = new CartaoResponse(new Cartao(criarCartaoRequest));
        Mockito.when(cartaoService.criarCartao(Mockito.any(CriarCartaoRequest.class))).thenReturn(response);

        String request = objectMapper.writeValueAsString(criarCartaoRequest);

        this.mockMvc.perform(
                    post(URL_CARTOES)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(httpBasic("username","password"))
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

}