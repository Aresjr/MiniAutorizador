package br.com.vr.mini_autorizador.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SaqueCartaoRequest {

    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;

}
