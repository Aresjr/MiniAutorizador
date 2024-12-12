package br.com.vr.mini_autorizador.model;

import br.com.vr.mini_autorizador.dto.CartaoRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroCartao;
    private String senha;
    private BigDecimal valor;

    private final static Integer VALOR_PADRAO_INICIAL = 500;

    public Cartao(CartaoRequest cartaoRequest) {
        this.numeroCartao = cartaoRequest.getNumeroCartao();
        this.senha = cartaoRequest.getSenha();
        this.valor = new BigDecimal(VALOR_PADRAO_INICIAL).setScale(2);
    }

}
