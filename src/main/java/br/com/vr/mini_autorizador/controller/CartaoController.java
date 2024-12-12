package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.CriarCartaoRequest;
import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.exception.CartaoExistenteException;
import br.com.vr.mini_autorizador.exception.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoResponse> criarCartao(@RequestBody CriarCartaoRequest criarCartaoRequest) {
        CartaoResponse response;
        try {
            response = cartaoService.criarCartao(criarCartaoRequest);
        } catch (CartaoExistenteException e) {
            return new ResponseEntity<>(new CartaoResponse(e.getCartao()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> obterSaldoCartao(@PathVariable String numeroCartao) {
        BigDecimal saldoCartao;
        try {
            saldoCartao = cartaoService.obterSaldoCartao(numeroCartao);
        } catch (CartaoNaoEncontradoException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(saldoCartao, HttpStatus.OK);
    }

}
