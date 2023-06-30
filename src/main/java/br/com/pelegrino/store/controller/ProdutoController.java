package br.com.pelegrino.store.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pelegrino.store.ExceptionStore;
import br.com.pelegrino.store.model.Produto;
import br.com.pelegrino.store.repository.ProdutoRepository;
import br.com.pelegrino.store.service.ServiceSendEmail;

@Controller
@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@ResponseBody
	@PostMapping(value = "/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionStore, UnsupportedEncodingException, MessagingException {
		
		if (produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {
			throw new ExceptionStore("Tipo da unidade deve ser informada.");
		}
		
		if (produto.getNome().length() < 10) {
			throw new ExceptionStore("Nome do produto deve ter mais de 10 letras.");
		}
		
		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionStore("Empresa responsável deve ser informada.");
		}
		
		if (produto.getId() == null) {
			
			List<Produto> produtos = produtoRepository.buscaProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());
			
			if (!produtos.isEmpty()) {
				throw new ExceptionStore("Já existe produto com a descrição: " + produto.getNome());
				
			}
		}
		
		if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
			throw new ExceptionStore("Categoria deve ser informada.");
		}
		
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
			throw new ExceptionStore("Marca deve ser informada.");
		}
		
		if (produto.getQtdEstoque() < 1) {
			throw new ExceptionStore("O produto deve ter no mínimo 1 no estoque.");
		}
		
		Produto produtoSalvo = produtoRepository.save(produto);
		
		if (produto.getAlertaQtdEstoque() && produto.getQtdEstoque() <= 1) {
			
			StringBuilder html = new StringBuilder();
			html.append("<h2>")
			.append("Produto: " + produto.getNome())
			.append(" com estoque baixo: " + produto.getQtdEstoque());
			html.append("<p> Id Prod.: ").append(produto.getId()).append("</h2>");
			
			if (produto.getEmpresa().getEmail() != null) {
				serviceSendEmail.enviarEmailHtml("Produto sem estoque", html.toString(), produto.getEmpresa().getEmail());
			}
		}
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/deleteProduto")
	public ResponseEntity<String> deleteProduto(@RequestBody Produto produto) {
		
		produtoRepository.deleteById(produto.getId());
		
		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteProdutoPorId/{id}")
	public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id) {
		
		produtoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/obterProduto/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionStore {
		
		Produto produto = produtoRepository.findById(id).orElse(null);
		
		if (produto == null) {
			throw new ExceptionStore("Não encontrou produto com o código: " + id);
		}
		
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	
	}
	
	@ResponseBody
	@GetMapping(value = "/buscarProdNome/{desc}")
	public ResponseEntity<List<Produto>> buscaProdNome(@PathVariable("desc") String desc) {
		
		List<Produto> produto = produtoRepository.buscaProdutoNome(desc.toUpperCase());
		
		return new ResponseEntity<List<Produto>>(produto, HttpStatus.OK);
	
	}
	
}
