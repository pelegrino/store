package br.com.pelegrino.store.controller;

import java.util.List;

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
import br.com.pelegrino.store.model.Acesso;
import br.com.pelegrino.store.repository.AcessoRepository;
import br.com.pelegrino.store.service.AcessoService;

@Controller
@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarAcesso")
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionStore {
		
		if (acesso.getId() == null) {
			
			List<Acesso> acessos = acessoRepository.buscaAcessoDesc(acesso.getDescricao().toUpperCase());
			
			if (!acessos.isEmpty()) {
				throw new ExceptionStore("Já existe acesso com a descrição: " + acesso.getDescricao());
				
			}
		}
		
		Acesso acessoSalvo = acessoService.save(acesso);
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@PostMapping(value = "/deleteAcesso")
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) {
		
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity("Acesso Removido", HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@DeleteMapping(value = "/deleteAcessoPorId/{id}")
	public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) {
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity("Acesso Removido", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/obterAcesso/{id}")
	public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) throws ExceptionStore {
		
		Acesso acesso = acessoRepository.findById(id).orElse(null);
		
		if (acesso == null) {
			throw new ExceptionStore("Não encontrou acesso com o código: " + id);
		}
		
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	
	}
	
	@ResponseBody
	@GetMapping(value = "/buscarPorDesc/{desc}")
	public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) {
		
		List<Acesso> acesso = acessoRepository.buscaAcessoDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	
	}
	
}
