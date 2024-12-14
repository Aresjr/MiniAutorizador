package br.com.vr.mini_autorizador.exception;

import br.com.vr.mini_autorizador.model.Cartao;
import lombok.Getter;

@Getter
public class CartaoExistenteException extends RuntimeException {

    /*
    * retornar todas as informações do cartão incluindo a senha é uma péssima prática
    * o recomendado seria salvar apenas o número do cartão
    * porém iria contra a especificação técnica
     */
    Cartao cartao;

    public CartaoExistenteException(Cartao cartao) {
        this.cartao = cartao;
    }

}
