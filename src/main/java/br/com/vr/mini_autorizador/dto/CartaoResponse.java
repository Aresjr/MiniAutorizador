package br.com.vr.mini_autorizador.dto;

import br.com.vr.mini_autorizador.model.Cartao;
import lombok.Value;

@Value
public class CartaoResponse {

    String senha;
    String numeroCartao;

    public CartaoResponse(Cartao cartao) {
        this.senha = cartao.getSenha();
        this.numeroCartao = cartao.getNumeroCartao();
    }
}
