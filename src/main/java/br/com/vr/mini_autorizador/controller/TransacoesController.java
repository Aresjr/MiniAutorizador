package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.SaqueCartaoRequest;
import br.com.vr.mini_autorizador.enums.MensagemTransacaoCartao;
import br.com.vr.mini_autorizador.exception.TransacaoException;
import br.com.vr.mini_autorizador.service.TransacoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/transacoes")
public class TransacoesController {

    @Autowired
    private TransacoesService transacoesService;

    @PostMapping
    public ResponseEntity<String> saqueCartao(@RequestBody SaqueCartaoRequest saqueCartaoRequest) {
        MensagemTransacaoCartao response;
        try {
            response = transacoesService.realizarSaqueCartao(saqueCartaoRequest);
        } catch (TransacaoException e) {
            return new ResponseEntity<>(e.getMensagemTransacaoCartao().toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response.toString(), HttpStatus.CREATED);
    }

}
