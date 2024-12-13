package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.SaqueCartaoRequest;
import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import br.com.vr.mini_autorizador.exception.CartaoInexistenteException;
import br.com.vr.mini_autorizador.exception.SaldoInsuficienteException;
import br.com.vr.mini_autorizador.exception.SenhaErradaCartaoException;
import br.com.vr.mini_autorizador.exception.TransacaoException;
import br.com.vr.mini_autorizador.model.Cartao;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransacoesService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Transactional
    public MensagemTransacaoCartao realizarSaqueCartao(SaqueCartaoRequest saqueCartaoRequest)
            throws TransacaoException {
        Cartao cartao = cartaoRepository.findByNumeroCartao(saqueCartaoRequest.getNumeroCartao())
            .orElseThrow(CartaoInexistenteException::new);

        BigDecimal saldoAtualCartao = Optional.ofNullable(
                cartao.getSenha().equals(saqueCartaoRequest.getSenhaCartao()) ? cartao.getValor() : null)
            .orElseThrow(SenhaErradaCartaoException::new);

        BigDecimal saldoAposSaque = saldoAtualCartao.subtract(saqueCartaoRequest.getValor());
        cartao.setValor(
            Optional.ofNullable(saldoAposSaque.compareTo(BigDecimal.ZERO) >= 0 ? saldoAposSaque : null)
                .orElseThrow(SaldoInsuficienteException::new)
        );
        cartaoRepository.save(cartao);

        return MensagemTransacaoCartao.OK;
    }

}
