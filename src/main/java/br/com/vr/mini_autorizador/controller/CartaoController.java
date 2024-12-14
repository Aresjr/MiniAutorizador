package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.CriarCartaoRequest;
import br.com.vr.mini_autorizador.dto.CartaoResponse;
import br.com.vr.mini_autorizador.exception.CartaoExistenteException;
import br.com.vr.mini_autorizador.exception.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.service.CartaoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Log4j2
@RestController
@RequestMapping(value = "/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoResponse> criarCartao(@RequestBody CriarCartaoRequest criarCartaoRequest) {
        log.info("Requisição de criação de cartão. Número cartão: {};", criarCartaoRequest.getNumeroCartao());
        CartaoResponse response;
        try {
            response = cartaoService.criarCartao(criarCartaoRequest);
        } catch (CartaoExistenteException e) {
            log.warn("Criação de cartão recusada. Número cartão: {};", e.getCartao().getNumeroCartao());
            return new ResponseEntity<>(new CartaoResponse(e.getCartao()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Cartão criado. Número cartão: {}", response.getNumeroCartao());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> obterSaldoCartao(@PathVariable String numeroCartao) {
        log.info("Requisição de obtenção de saldo. Número cartão: {};", numeroCartao);
        BigDecimal saldoCartao;
        try {
            saldoCartao = cartaoService.obterSaldoCartao(numeroCartao);
        } catch (CartaoNaoEncontradoException e) {
            log.warn("Cartão não encontrado. Número cartão: {};", numeroCartao);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Saldo de cartão. Número cartão: {}; Saldo: {};", numeroCartao, saldoCartao);
        return new ResponseEntity<>(saldoCartao, HttpStatus.OK);
    }

}
