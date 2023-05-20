package br.com.pelegrino.store;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.pelegrino.store.controller.AcessoController;
import br.com.pelegrino.store.model.Acesso;
import br.com.pelegrino.store.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = StoreApplication.class)
class StoreApplicationTests extends TestCase {
	
	//@Autowired
	//private AcessoService acessoService;

	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoController acessoController;
	
	@Test
	public void cadastroAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		//Grava no BD
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		assertEquals(true, acesso.getId() > 0);
		
		//Valida dados
		assertEquals("ROLE_ADMIN", acesso.getDescricao());
		
		//Teste de carregamento
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();
		
		assertEquals(acesso.getId(), acesso2.getId());
		
		//Teste de delete
		acessoRepository.deleteById(acesso2.getId());
		acessoRepository.flush();
	
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);
		
		assertEquals(true, acesso3 == null);
		
		//Teste de Query
		acesso = new Acesso();
		acesso.setDescricao("ROLE_ALUNO");
		acesso = acessoController.salvarAcesso(acesso).getBody();
		List<Acesso> acessos = acessoRepository.buscaAcessoDesc("ALUNO".trim().toUpperCase());
		
		assertEquals(1, acessos.size());
		
		acessoRepository.deleteById(acesso.getId());
	}

}
