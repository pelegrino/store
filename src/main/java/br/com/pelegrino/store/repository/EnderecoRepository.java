package br.com.pelegrino.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pelegrino.store.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
