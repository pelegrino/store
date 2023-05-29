package br.com.pelegrino.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.pelegrino.store.model.PessoaJuridica;

public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long>{
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpjCadastrado(String cnpj);

}
