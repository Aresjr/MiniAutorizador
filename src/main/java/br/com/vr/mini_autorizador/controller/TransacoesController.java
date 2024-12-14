package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.SaqueCartaoRequest;
import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import br.com.vr.mini_autorizador.exception.TransacaoException;
import br.com.vr.mini_autorizador.service.TransacoesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(value = "/transacoes")
public class TransacoesController {

    @Autowired
    private TransacoesService transacoesService;

    @PostMapping
    public ResponseEntity<String> saqueCartao(@RequestBody SaqueCartaoRequest saqueCartaoRequest) {
        log.info("Requisição de saque. Número cartão: {}; Valor: {};",
                saqueCartaoRequest.getNumeroCartao(), saqueCartaoRequest.getValor());
        MensagemTransacaoCartao response;
        try {
            response = transacoesService.realizarSaqueCartao(saqueCartaoRequest);
        } catch (TransacaoException e) {
            log.warn("Transação negada. Mensagem: {};", e.getMensagemTransacaoCartao());
            return new ResponseEntity<>(e.getMensagemTransacaoCartao().toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Saque autorizado.");
        return new ResponseEntity<>(response.toString(), HttpStatus.CREATED);
    }

}
