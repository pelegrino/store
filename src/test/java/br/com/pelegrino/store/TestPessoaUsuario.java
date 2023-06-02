package br.com.pelegrino.store;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.pelegrino.store.controller.PessoaController;
import br.com.pelegrino.store.enums.TipoEndereco;
import br.com.pelegrino.store.model.Endereco;
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
	
}
