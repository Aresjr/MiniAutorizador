package br.com.vr.mini_autorizador.exception;

import br.com.vr.mini_autorizador.model.Cartao;
import lombok.Getter;

@Getter
public class CartaoExistenteException extends RuntimeException {

    Cartao cartao;

    public CartaoExistenteException(Cartao cartao) {
        this.cartao = cartao;
    }

}
