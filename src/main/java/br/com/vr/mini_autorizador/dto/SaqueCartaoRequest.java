package br.com.vr.mini_autorizador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class SaqueCartaoRequest {

    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;

}
