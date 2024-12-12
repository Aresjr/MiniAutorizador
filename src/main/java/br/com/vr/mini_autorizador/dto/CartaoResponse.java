package br.com.vr.mini_autorizador.dto;

import br.com.vr.mini_autorizador.model.Cartao;
import lombok.Value;

@Value
public class CartaoResponse {

    String numeroCartao;
    String senha;

    public CartaoResponse(Cartao cartao) {
        this.numeroCartao = cartao.getNumeroCartao();
        this.senha = cartao.getSenha();
    }
}
