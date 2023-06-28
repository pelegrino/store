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
import br.com.pelegrino.store.model.ContaPagar;
import br.com.pelegrino.store.repository.ContaPagarRepository;

@Controller
@RestController
public class ContaPagarController {

	@Autowired
	private ContaPagarRepository contaPagarRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarContaPagar")
	public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar) throws ExceptionStore {
		
		if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
			throw new ExceptionStore("Empresa responsável deve ser informada.");
		}
		
		if (contaPagar.getPessoa() == null || contaPagar.getPessoa().getId() <= 0) {
			throw new ExceptionStore("Pessoa responsável deve ser informada.");
		}
		
		if (contaPagar.getPessoa_fornecedor() == null || contaPagar.getPessoa_fornecedor().getId() <= 0) {
			throw new ExceptionStore("Fornecedor responsável deve ser informada.");
		}
		
		if (contaPagar.getId() == null) {
			List<ContaPagar> contaPagars = contaPagarRepository.buscaContaDesc(contaPagar.getDescricao().toUpperCase().trim());
			
			if (!contaPagars.isEmpty()) {
				throw new ExceptionStore("Já existe conta a pagar com a mesma descrição.");
			}
		}
		
		ContaPagar contaPagarSalvo = contaPagarRepository.save(contaPagar);
		
		return new ResponseEntity<ContaPagar>(contaPagarSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/deleteContaPagar")
	public ResponseEntity<String> deleteContaPagar(@RequestBody ContaPagar contaPagar) {
		
		contaPagarRepository.deleteById(contaPagar.getId());
		
		return new ResponseEntity<String>("Conta a Pagar Removida", HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteContaPagarPorId/{id}")
	public ResponseEntity<String> deleteContaPagarPorId(@PathVariable("id") Long id) {
		
		contaPagarRepository.deleteById(id);
		
		return new ResponseEntity<String>("Conta a pagar Removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/obterContaPagar/{id}")
	public ResponseEntity<ContaPagar> obterContaPagar(@PathVariable("id") Long id) throws ExceptionStore {
		
		ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);
		
		if (contaPagar == null) {
			throw new ExceptionStore("Não encontrou a conta pagar com o código: " + id);
		}
		
		return new ResponseEntity<ContaPagar>(contaPagar, HttpStatus.OK);
	
	}
	
	@ResponseBody
	@GetMapping(value = "/buscaContaPagarDesc/{desc}")
	public ResponseEntity<List<ContaPagar>> buscaContaPagarDesc(@PathVariable("desc") String desc) {
		
		List<ContaPagar> contaPagar = contaPagarRepository.buscaContaDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<ContaPagar>>(contaPagar, HttpStatus.OK);
	
	}
	
}
