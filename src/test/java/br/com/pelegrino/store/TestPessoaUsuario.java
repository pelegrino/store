package br.com.pelegrino.store;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.pelegrino.store.controller.PessoaController;
import br.com.pelegrino.store.model.PessoaJuridica;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = StoreApplication.class)
public class TestPessoaUsuario extends TestCase {

	
	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testCadPessoaFisica() throws ExceptionStore {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Antenor");
		pessoaJuridica.setEmail("teste@teste.com");
		pessoaJuridica.setTelefone("14997167515");
		pessoaJuridica.setInscEstadual("999888777");
		pessoaJuridica.setInscMunicipal("666555444");
		pessoaJuridica.setNomeFantasia("Pelegrino");
		pessoaJuridica.setRazaoSocial("Viagem e Turismo");
		
		pessoaController.salvarPj(pessoaJuridica);
		
	}
	
}
