package br.com.pelegrino.store;

import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pelegrino.store.controller.AcessoController;
import br.com.pelegrino.store.model.Acesso;
import br.com.pelegrino.store.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = StoreApplication.class)
class StoreApplicationTests extends TestCase {

	// @Autowired
	// private AcessoService acessoService;

	@Autowired
	private AcessoRepository acessoRepository;

	@Autowired
	private AcessoController acessoController;

	@Autowired
	private WebApplicationContext wac;

	// Testes com Mockito
	// Teste do end-point de salvar
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockmvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_COMPRADOR" + Calendar.getInstance().getTimeInMillis());

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockmvc
				.perform(MockMvcRequestBuilders.post("/salvarAcesso").content(objectMapper.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da API" + retornoApi.andReturn().getResponse().getContentAsString());

		// Converter o retorno da API para objeto de acesso

		Acesso objetoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(),
				Acesso.class);

		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());

	}

	// Teste do end-point de deletar
	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockmvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_TESTE_DELETE");

		acesso = acessoRepository.save(acesso);

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockmvc
				.perform(MockMvcRequestBuilders.post("/deleteAcesso").content(objectMapper.writeValueAsString(acesso))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de Retorno: " + retornoApi.andReturn().getResponse().getStatus());

		assertEquals("Acesso Removido", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

	}

	// Teste do end-point de deletar por Id
	@Test
	public void testRestApiDeletePorIdAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockmvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_TESTE_DELETE_ID");

		acesso = acessoRepository.save(acesso);

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockmvc.perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/" + acesso.getId())
				.content(objectMapper.writeValueAsString(acesso)).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de Retorno: " + retornoApi.andReturn().getResponse().getStatus());

		assertEquals("Acesso Removido", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

	}

	// Teste do end-point de obter acesso por Id
	@Test
	public void testRestApiObterAcessoId() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockmvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_OBTER_ID");

		acesso = acessoRepository.save(acesso);

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockmvc.perform(MockMvcRequestBuilders.get("/obterAcesso/" + acesso.getId())
				.content(objectMapper.writeValueAsString(acesso)).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));

		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

		Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(),
				Acesso.class);

		assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
		assertEquals(acesso.getId(), acessoRetorno.getId());
	}

	// Teste do end-point de busca
	@Test
	public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockmvc = builder.build();

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_TESTE_OBTER_LIST");

		acesso = acessoRepository.save(acesso);

		ObjectMapper objectMapper = new ObjectMapper();

		ResultActions retornoApi = mockmvc
									.perform(MockMvcRequestBuilders.get("/buscarPorDesc/ROLE_TESTE_OBTER_LIST")
									.content(objectMapper.writeValueAsString(acesso))
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON));
		
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

		List<Acesso> retornoApiList = objectMapper.
										readValue(retornoApi
													.andReturn()
													.getResponse()
													.getContentAsString(), 
										new TypeReference<List<Acesso>>(){});
		
		assertEquals(16, retornoApiList.size());
		
		assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());
		
		acessoRepository.deleteById(acesso.getId());

	}

	
	// Testes com JUnit
	@Test
	public void cadastroAcesso() throws ExceptionStore {

		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_FDGKH");

		// Grava no BD
		acesso = acessoController.salvarAcesso(acesso).getBody();

		assertEquals(true, acesso.getId() > 0);

		// Valida dados
		assertEquals("ROLE_FDGKH", acesso.getDescricao());

		// Teste de carregamento
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();

		assertEquals(acesso.getId(), acesso2.getId());

		// Teste de delete
		acessoRepository.deleteById(acesso2.getId());
		acessoRepository.flush();

		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);

		assertEquals(true, acesso3 == null);

		// Teste de Query
		acesso = new Acesso();
		acesso.setDescricao("ROLE_ALUNO");
		acesso = acessoController.salvarAcesso(acesso).getBody();
		List<Acesso> acessos = acessoRepository.buscaAcessoDesc("ALUNO".trim().toUpperCase());

		assertEquals(1, acessos.size());

		acessoRepository.deleteById(acesso.getId());
	}

}
