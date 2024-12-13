package br.com.vr.mini_autorizador;

import br.com.vr.mini_autorizador.controller.CartaoController;
import br.com.vr.mini_autorizador.controller.TransacoesController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MiniAutorizadorApplicationTests {

	@Autowired
	private CartaoController cartaoController;

	@Autowired
	private TransacoesController transacoesController;

	@Test
	void contextLoads() {
		Assertions.assertThat(cartaoController).isNotNull();
		Assertions.assertThat(transacoesController).isNotNull();
	}

}
