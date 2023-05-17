package br.com.pelegrino.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pelegrino.store.model.Acesso;
import br.com.pelegrino.store.repository.AcessoRepository;

@Service
public class AcessoService {
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	public Acesso save(Acesso acesso) {
		
		return acessoRepository.save(acesso);
	}

}
