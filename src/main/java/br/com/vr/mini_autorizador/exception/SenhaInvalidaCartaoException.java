package br.com.vr.mini_autorizador.exception;

import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import lombok.Getter;

@Getter
public class SenhaInvalidaCartaoException extends TransacaoException {

    public SenhaInvalidaCartaoException() {
        super(MensagemTransacaoCartao.SENHA_INVALIDA);
    }

}
