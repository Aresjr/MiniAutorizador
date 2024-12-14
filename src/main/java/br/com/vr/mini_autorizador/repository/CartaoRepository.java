package br.com.vr.mini_autorizador.repository;

import br.com.vr.mini_autorizador.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, String> {

    Optional<Cartao> findByNumeroCartao(String numeroCartao);

    Optional<Cartao.CartaoSaldo> findValorByNumeroCartao(String numeroCartao);

    @Modifying
    @Query("update Cartao set valor = :saldoNovo where numeroCartao = :numeroCartao")
    Integer atualizaSaldoCartao(@Param("numeroCartao") String numeroCartao, @Param("saldoNovo") BigDecimal saldoNovo);

}
