package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.CartaoRequest;
import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.model.Cartao;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    public CartaoResponse criarCartao(CartaoRequest cartaoRequest) {
        Cartao cartao = cartaoRepository.save(new Cartao(cartaoRequest));
        return new CartaoResponse(cartao);
    }

}
