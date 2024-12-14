package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.SaqueCartaoRequest;
import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import br.com.vr.mini_autorizador.exception.CartaoInexistenteException;
import br.com.vr.mini_autorizador.exception.SaldoInsuficienteException;
import br.com.vr.mini_autorizador.exception.SenhaInvalidaCartaoException;
import br.com.vr.mini_autorizador.exception.TransacaoException;
import br.com.vr.mini_autorizador.model.Cartao;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransacoesService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Transactional
    public MensagemTransacaoCartao realizarSaqueCartao(SaqueCartaoRequest saqueRequest)
            throws TransacaoException {
        Cartao cartao = cartaoRepository.findByNumeroCartao(saqueRequest.getNumeroCartao())
                .orElseThrow(CartaoInexistenteException::new);

        Optional.ofNullable(senhaValidaCartao(cartao,
                        saqueRequest.getSenhaCartao()) ? cartao.getValor() : null)
                .orElseThrow(SenhaInvalidaCartaoException::new);

        atualizaSaldoCartao(saqueRequest.getNumeroCartao(), saqueRequest.getValor());

        return MensagemTransacaoCartao.OK;
    }

    /*
     * Impede a leitura de um registro com alterações pendentes em um transação.
     * Aqui verifico se o saldo do cartão tem o valor suficiente para realizar o saque e subtraio o valor do saque
     * na mesma transação.
     * Definição de acordo com a documentação original:
     *
     * Isolation.REPEATABLE_READ
     * A constant indicating that dirty reads and non-repeatable reads are prevented; phantom reads can occur.
     * This level prohibits a transaction from reading a row with uncommitted changes in it,
     * and it also prohibits the situation where one transaction reads a row, a second transaction alters the row,
     * and the first transaction re-reads the row, getting different values the second time (a "non-repeatable read")
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    private void atualizaSaldoCartao(String numeroCartao, BigDecimal valorSaque) {
        BigDecimal saldoCartao = obterSaldoCartao(numeroCartao);
        Optional.ofNullable(saqueDentroDoSaldo(saldoCartao, valorSaque) ? saldoCartao : null)
                .map(saldoAtual -> cartaoRepository.atualizaSaldoCartao(numeroCartao,
                        saldoAtual.subtract(valorSaque)))
                .orElseThrow(SaldoInsuficienteException::new);
    }

    private BigDecimal obterSaldoCartao(String numeroCartao) {
        return cartaoRepository.findValorByNumeroCartao(numeroCartao)
                .map(Cartao.CartaoSaldo::getValor)
                .orElseThrow(CartaoInexistenteException::new);
    }

    private Boolean senhaValidaCartao(Cartao cartao, String senha) {
        return cartao.getSenha().equals(senha);
    }

    private Boolean saqueDentroDoSaldo(BigDecimal saldo, BigDecimal saque) {
        return saldo.subtract(saque).compareTo(BigDecimal.ZERO) >= 0;
    }

}
