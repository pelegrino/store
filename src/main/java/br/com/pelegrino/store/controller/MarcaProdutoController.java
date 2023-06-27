package br.com.pelegrino.store.controller;

import java.util.List;

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
import br.com.pelegrino.store.model.MarcaProduto;
import br.com.pelegrino.store.repository.MarcaRepository;

@Controller
@RestController
public class MarcaProdutoController {

	@Autowired
	private MarcaRepository marcaRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarMarca")
	public ResponseEntity<MarcaProduto> salvarMarca(@RequestBody @Valid MarcaProduto marcaProduto) throws ExceptionStore {
		
		if (marcaProduto.getId() == null) {
			
			List<MarcaProduto> marcaProdutos = marcaRepository.buscaMarcaDesc(marcaProduto.getNomeDesc().toUpperCase());
			
			if (!marcaProdutos.isEmpty()) {
				throw new ExceptionStore("Já existe marca com a descrição: " + marcaProduto.getNomeDesc());
				
			}
		}
		
		MarcaProduto marcaProdutoSalvo = marcaRepository.save(marcaProduto);
		
		return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@PostMapping(value = "/deleteMarca")
	public ResponseEntity<?> deleteMarca(@RequestBody MarcaProduto marcaProduto) {
		
		marcaRepository.deleteById(marcaProduto.getId());
		
		return new ResponseEntity("Marca Removida.", HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@DeleteMapping(value = "/deleteMarcaPorId/{id}")
	public ResponseEntity<?> deleteMarcaPorId(@PathVariable("id") Long id) {
		
		marcaRepository.deleteById(id);
		
		return new ResponseEntity("Marca Removida.", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/obterMarcaProduto/{id}")
	public ResponseEntity<MarcaProduto> obterMarcaProduto(@PathVariable("id") Long id) throws ExceptionStore {
		
		MarcaProduto marcaProduto = marcaRepository.findById(id).orElse(null);
		
		if (marcaProduto == null) {
			throw new ExceptionStore("Não encontrou marca com o código: " + id);
		}
		
		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
	
	}
	
	@ResponseBody
	@GetMapping(value = "/buscarMarcaProdutoPorDesc/{desc}")
	public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoPorDesc(@PathVariable("desc") String desc) {
		
		List<MarcaProduto> marcaProdutos = marcaRepository.buscaMarcaDesc(desc.toUpperCase().trim());
		
		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
	
	}
	
}
