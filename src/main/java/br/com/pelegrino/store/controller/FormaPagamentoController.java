package br.com.pelegrino.store.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pelegrino.store.ExceptionStore;
import br.com.pelegrino.store.model.FormaPagamento;
import br.com.pelegrino.store.repository.FormaPagamentoRepository;

@RestController
public class FormaPagamentoController {
	
	@Autowired
	public FormaPagamentoRepository formaPagamentoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarFormaPagamento")
	public ResponseEntity<FormaPagamento> salvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) throws ExceptionStore {
		
			
		formaPagamento = formaPagamentoRepository.save(formaPagamento);
		
		return new ResponseEntity<FormaPagamento>(formaPagamento, HttpStatus.OK);
	}

}
