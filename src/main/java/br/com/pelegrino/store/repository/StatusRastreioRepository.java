package br.com.pelegrino.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.pelegrino.store.model.StatusRastreio;

@Transactional
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {

}
