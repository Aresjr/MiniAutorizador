package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.CartaoRequest;
import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoResponse> criarCartao(@RequestBody @Valid CartaoRequest cartaoRequest) {

        CartaoResponse response = cartaoService.criarCartao(cartaoRequest);
        HttpStatus status = CREATED;

        return new ResponseEntity<>(response, status);
    }

}
