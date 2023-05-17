package br.com.pelegrino.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.pelegrino.store.controller.AcessoController;
import br.com.pelegrino.store.model.Acesso;

@SpringBootTest(classes = StoreApplication.class)
class StoreApplicationTests {
	
	//@Autowired
	//private AcessoService acessoService;

	//@Autowired
	//private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoController acessoController;
	
	@Test
	public void cadastroAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		acessoController.salvarAcesso(acesso);
	}

}
