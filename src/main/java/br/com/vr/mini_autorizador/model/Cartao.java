package br.com.vr.mini_autorizador.model;

import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Value
public class Cartao {

    String numeroCartao;
    String senha;
    Double valor;

}
