package br.com.vr.mini_autorizador.exception;

import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import lombok.Getter;

@Getter
public class TransacaoException extends RuntimeException {

    MensagemTransacaoCartao mensagemTransacaoCartao;

    public TransacaoException(MensagemTransacaoCartao mensagemTransacaoCartao) {
        this.mensagemTransacaoCartao = mensagemTransacaoCartao;
    }

}
