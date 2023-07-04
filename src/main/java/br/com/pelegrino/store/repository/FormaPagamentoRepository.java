package br.com.pelegrino.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.pelegrino.store.model.FormaPagamento;

@Transactional
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

}
