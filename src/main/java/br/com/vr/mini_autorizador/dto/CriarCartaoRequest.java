package br.com.vr.mini_autorizador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CriarCartaoRequest {

    private String numeroCartao;
    private String senha;

}
