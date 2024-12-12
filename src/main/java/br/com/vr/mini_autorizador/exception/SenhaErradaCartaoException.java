package br.com.vr.mini_autorizador.exception;

import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import lombok.Getter;

@Getter
public class SenhaErradaCartaoException extends TransacaoException {

    public SenhaErradaCartaoException() {
        super(MensagemTransacaoCartao.SENHA_INVALIDA);
    }

}
