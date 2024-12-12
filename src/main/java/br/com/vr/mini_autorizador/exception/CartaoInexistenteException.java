package br.com.vr.mini_autorizador.exception;

import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import lombok.Getter;

@Getter
public class CartaoInexistenteException extends TransacaoException {

    public CartaoInexistenteException() {
        super(MensagemTransacaoCartao.CARTAO_INEXISTENTE);
    }

}
