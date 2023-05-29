package br.com.pelegrino.store.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.pelegrino.store.model.PessoaJuridica;

public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long>{

}
