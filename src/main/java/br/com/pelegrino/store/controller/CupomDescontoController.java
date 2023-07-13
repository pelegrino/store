package br.com.pelegrino.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pelegrino.store.model.CupomDesconto;
import br.com.pelegrino.store.repository.CupomDescontoRepository;

@RestController
public class CupomDescontoController {
	
	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;
	
	@ResponseBody
	@GetMapping(value = "/listaCuponsDescontoPorEmpresa/{idEmpresa}")
	public ResponseEntity<List<CupomDesconto>> listaCuponsDescontoPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.cupomDescontoPorEmpresa(idEmpresa), HttpStatus.OK);
	}

	
	@ResponseBody
	@GetMapping(value = "/listaCuponsDesconto")
	public ResponseEntity<List<CupomDesconto>> listaCuponsDesconto() {
		
		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.findAll(), HttpStatus.OK);
	}

}
