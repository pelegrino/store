package br.com.pelegrino.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pelegrino.store.ExceptionStore;
import br.com.pelegrino.store.model.CategoriaProduto;
import br.com.pelegrino.store.model.dto.CategoriaProdutoDto;
import br.com.pelegrino.store.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {
	
	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;
	
	
	@ResponseBody
	@GetMapping(value = "/buscarPorDescCategoria/{desc}")
	public ResponseEntity<List<CategoriaProduto>> buscarPorDescCategoria(@PathVariable("desc") String desc) {
		
		List<CategoriaProduto> acesso = categoriaProdutoRepository.buscaCategoriaDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<CategoriaProduto>>(acesso, HttpStatus.OK);
	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@PostMapping(value = "/deleteCategoria")
	public ResponseEntity<?> deleteCategoria(@RequestBody CategoriaProduto categoriaProduto) {
		
		if (categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false){
			return new ResponseEntity("Categoria já foi removida", HttpStatus.OK);
		}
		
		categoriaProdutoRepository.deleteById(categoriaProduto.getId());
		return new ResponseEntity("Categoria Removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/salvarCategoria")
	public ResponseEntity<CategoriaProdutoDto> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionStore {
		
		if (categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {
			throw new ExceptionStore("A empresa deve ser informada.");
		}
		
		if (categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc())) {
			throw new ExceptionStore("Não pode cadastrar categoria com o mesmo nome.");
		}
		
		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);
		
		CategoriaProdutoDto categoriaProdutoDto = new CategoriaProdutoDto();
		categoriaProdutoDto.setId(categoriaSalva.getId());
		categoriaProdutoDto.setNomedesc(categoriaSalva.getNomeDesc());
		categoriaProdutoDto.setEmpresa(categoriaSalva.getEmpresa().getId().toString());
		
		
		return new ResponseEntity<CategoriaProdutoDto>(categoriaProdutoDto, HttpStatus.OK);
	}
	
}
