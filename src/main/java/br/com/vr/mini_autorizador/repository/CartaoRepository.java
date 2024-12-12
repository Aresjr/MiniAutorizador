package br.com.vr.mini_autorizador.repository;

import br.com.vr.mini_autorizador.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, String> { }
