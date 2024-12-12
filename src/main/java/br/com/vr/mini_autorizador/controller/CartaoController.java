package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.CartaoRequest;
import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.exception.CartaoExistenteException;
import br.com.vr.mini_autorizador.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoResponse> criarCartao(@RequestBody CartaoRequest cartaoRequest) {

        CartaoResponse response;
        try {
            response = cartaoService.criarCartao(cartaoRequest);
        } catch (CartaoExistenteException e) {
            return new ResponseEntity<>(new CartaoResponse(e.getCartao()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
