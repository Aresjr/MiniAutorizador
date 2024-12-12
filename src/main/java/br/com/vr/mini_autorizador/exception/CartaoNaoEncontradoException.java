package br.com.vr.mini_autorizador.exception;

import lombok.Getter;

@Getter
public class CartaoNaoEncontradoException extends RuntimeException {

    String numeroCartao;

    public CartaoNaoEncontradoException(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

}
