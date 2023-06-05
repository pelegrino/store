package br.com.pelegrino.store;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.pelegrino.store.controller.PessoaController;
import br.com.pelegrino.store.enums.TipoEndereco;
import br.com.pelegrino.store.model.Endereco;
import br.com.pelegrino.store.model.PessoaFisica;
import br.com.pelegrino.store.model.PessoaJuridica;
import br.com.pelegrino.store.repository.PessoaRepository;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = StoreApplication.class)
public class TestPessoaUsuario extends TestCase {

	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testCadPessoaJuridica() throws ExceptionStore {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Antenor");
		pessoaJuridica.setEmail("teste@teste3.com");
		pessoaJuridica.setTelefone("14997167515");
		pessoaJuridica.setInscEstadual("999888777");
		pessoaJuridica.setInscMunicipal("666555444");
		pessoaJuridica.setNomeFantasia("Pelegrino");
		pessoaJuridica.setRazaoSocial("Viagem e Turismo");
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Sta. Clara");
		endereco1.setCep("17222-888");
		endereco1.setComplemento("Casa");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setNumero("889");
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setRuaLogradouro("Av. Tamoios");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("SP");
		endereco1.setCidade("Tupã");
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Centro");
		endereco2.setCep("17222-998");
		endereco2.setComplemento("Apartametno");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setNumero("556");
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setRuaLogradouro("Av. Tabajaras");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("SP");
		endereco2.setCidade("Tupã");
		
		pessoaJuridica.getEnderecos().add(endereco1);
		pessoaJuridica.getEnderecos().add(endereco2);
		
		pessoaController.salvarPj(pessoaJuridica);
		
		assertEquals(true, pessoaJuridica.getId() > 0);
		
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());
		
	}
	
	@Test
	public void testCadPessoaFisica() throws ExceptionStore {
		
		PessoaJuridica pessoaJuridica = pessoaRepository.existeCnpjCadastrado("1685709292674");
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCpf("283.513.200-72");
		pessoaFisica.setNome("Antenor");
		pessoaFisica.setEmail("teste@teste6.com");
		pessoaFisica.setTelefone("14997167515");
		pessoaFisica.setEmpresa(pessoaJuridica);
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Sta. Clara");
		endereco1.setCep("17222-888");
		endereco1.setComplemento("Casa");
		endereco1.setNumero("889");
		endereco1.setPessoa(pessoaFisica);
		endereco1.setRuaLogradouro("Av. Tamoios");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("SP");
		endereco1.setCidade("Tupã");
		endereco1.setEmpresa(pessoaJuridica);
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Centro");
		endereco2.setCep("17222-998");
		endereco2.setComplemento("Apartametno");
		endereco2.setNumero("556");
		endereco2.setPessoa(pessoaFisica);
		endereco2.setRuaLogradouro("Av. Tabajaras");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("SP");
		endereco2.setCidade("Tupã");
		endereco2.setEmpresa(pessoaJuridica);
		
		pessoaFisica.getEnderecos().add(endereco1);
		pessoaFisica.getEnderecos().add(endereco2);
		
		pessoaController.salvarPf(pessoaFisica);
		
		assertEquals(true, pessoaFisica.getId() > 0);
		
		for (Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaFisica.getEnderecos().size());
		
	}
	
}
