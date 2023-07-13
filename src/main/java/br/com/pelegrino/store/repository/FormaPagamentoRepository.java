package br.com.pelegrino.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.pelegrino.store.model.FormaPagamento;

@Transactional
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

	@Query(value = "select f from FormaPagamento f where f.empresa.id = ?1")
	List<FormaPagamento> findAll(long idEmpresa);

}
