package br.com.vr.mini_autorizador.model;

import br.com.vr.mini_autorizador.dto.CriarCartaoRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroCartao;
    private String senha;
    private BigDecimal valor;

    public Cartao(CriarCartaoRequest criarCartaoRequest, BigDecimal valor) {
        this.numeroCartao = criarCartaoRequest.getNumeroCartao();
        this.senha = criarCartaoRequest.getSenha();
        this.valor = valor;
    }

}
