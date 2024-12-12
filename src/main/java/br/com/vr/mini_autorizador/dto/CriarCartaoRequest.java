package br.com.vr.mini_autorizador.dto;

import lombok.Getter;

@Getter
public class CriarCartaoRequest {

    private String numeroCartao;
    private String senha;

}
