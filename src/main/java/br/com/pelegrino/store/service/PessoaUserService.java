package br.com.pelegrino.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pelegrino.store.repository.UsuarioRepository;

@Service
public class PessoaUserService {
	
	@SuppressWarnings("unused")
	@Autowired
	private UsuarioRepository usuarioRepository;

}
