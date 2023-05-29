package br.com.pelegrino.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.pelegrino.store.model.PessoaJuridica;
import br.com.pelegrino.store.repository.PessoaRepository;
import br.com.pelegrino.store.service.PessoaUserService;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = StoreApplication.class)
public class TestPessoaUsuario extends TestCase {

	@SuppressWarnings("unused")
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testCadPessoaFisica() {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("111.222.333/0001-44");
		pessoaJuridica.setNome("Antenor");
		pessoaJuridica.setEmail("pelegrino@gmail.com");
		pessoaJuridica.setTelefone("14997167515");
		pessoaJuridica.setInscEstadual("999888777");
		pessoaJuridica.setInscMunicipal("666555444");
		pessoaJuridica.setNomeFantasia("Pelegrino");
		pessoaJuridica.setRazaoSocial("Viagem e Turismo");
		
		pessoaRepository.save(pessoaJuridica);
		
		/*
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCpf("111.222.333-44");
		pessoaFisica.setNome("Antenor");
		pessoaFisica.setEmail("pelegrino@gmail.com");
		pessoaFisica.setTelefone("14997167515");
		pessoaFisica.setEmpresa(pessoaFisica);
		*/
	}
	
}
