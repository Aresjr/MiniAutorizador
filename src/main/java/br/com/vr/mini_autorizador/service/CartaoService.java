package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.CriarCartaoRequest;
import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.exception.CartaoExistenteException;
import br.com.vr.mini_autorizador.exception.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.model.Cartao;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    public CartaoResponse criarCartao(CriarCartaoRequest criarCartaoRequest) throws CartaoExistenteException {
        cartaoRepository.findByNumeroCartao(criarCartaoRequest.getNumeroCartao())
            .ifPresent(cartao -> {
                throw new CartaoExistenteException(cartao);
            });
        Cartao cartao = cartaoRepository.save(new Cartao(criarCartaoRequest));
        return new CartaoResponse(cartao);
    }

    public BigDecimal obterSaldoCartao(String numeroCartao) {
        return cartaoRepository.findByNumeroCartao(numeroCartao)
                .map(Cartao::getValor).orElseThrow(() -> new CartaoNaoEncontradoException(numeroCartao));
    }

}
