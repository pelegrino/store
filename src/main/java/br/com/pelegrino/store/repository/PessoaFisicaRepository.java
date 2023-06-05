package br.com.pelegrino.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.pelegrino.store.model.PessoaFisica;

public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long>{
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public PessoaFisica existeCpfCadastrado(String cpf);

}
