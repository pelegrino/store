package br.com.pelegrino.store.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pelegrino.store.ExceptionStore;
import br.com.pelegrino.store.model.NotaFiscalCompra;
import br.com.pelegrino.store.repository.NotaFiscalCompraRepository;

@RestController
public class NotaFiscalCompraController {

	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;
	
	@ResponseBody
	@GetMapping(value = "/buscarNotaFiscalPorDesc/{desc}")
	public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalPorDesc(@PathVariable("desc") String desc) {
		
		List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.buscaNotaDesc(desc.toUpperCase().trim());
		
		return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompras, HttpStatus.OK);
	
	}
	
	@ResponseBody
	@GetMapping(value = "/obterNotaFiscalCompra/{id}")
	public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompra(@PathVariable("id") Long id) throws ExceptionStore {
		
		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);
		
		if (notaFiscalCompra == null) {
			throw new ExceptionStore("Não encontrou a nota fiscal com o código: " + id);
		}
		
		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@DeleteMapping(value = "/deleteNotaFiscalCompraPorId/{id}")
	public ResponseEntity<?> deleteNotaFiscalCompraPorId(@PathVariable("id") Long id) {
		
		notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id);
		notaFiscalCompraRepository.deleteById(id);
		
		return new ResponseEntity("Nota fiscal Removida.", HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionStore {
		
		if (notaFiscalCompra.getId() == null) {
		
			if (notaFiscalCompra.getDescricaoObs() != null) {
				List<NotaFiscalCompra> fiscalCompras = notaFiscalCompraRepository.buscaNotaDesc(notaFiscalCompra.getDescricaoObs().toUpperCase().trim());
				
				if (!fiscalCompras.isEmpty()) {
					throw new ExceptionStore("Já existe nota de Compra com essa descrição: " + notaFiscalCompra.getId());
				}
			}
		}
		
		if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
			throw new ExceptionStore("A pessoa juridica da nota deve ser informada.");
		}
		
		if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
			throw new ExceptionStore("A empresa da nota deve ser informada.");
		}
		
		if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {
			throw new ExceptionStore("A conta a pagar da nota deve ser informada.");
		}
		
		NotaFiscalCompra notaFiscalCompraSalva = notaFiscalCompraRepository.save(notaFiscalCompra);
		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalva, HttpStatus.OK);
	}
	
}
