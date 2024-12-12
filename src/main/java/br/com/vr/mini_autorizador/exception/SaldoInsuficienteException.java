package br.com.vr.mini_autorizador.exception;

import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import lombok.Getter;

@Getter
public class SaldoInsuficienteException extends TransacaoException {

    public SaldoInsuficienteException() {
        super(MensagemTransacaoCartao.SALDO_INSUFICIENTE);
    }

}
