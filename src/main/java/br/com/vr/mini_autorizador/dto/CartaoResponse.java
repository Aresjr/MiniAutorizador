package br.com.vr.mini_autorizador.dto;

import br.com.vr.mini_autorizador.model.Cartao;

public class CartaoResponse {

    private String numeroCartao;

    public CartaoResponse(Cartao cartao) {
        this.numeroCartao = cartao.getNumeroCartao();
    }
}
