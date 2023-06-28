package br.com.pelegrino.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.pelegrino.store.model.ContaPagar;

@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

	@Query("select a from ContaPagar a where upper(trim(a.descricao)) like %?1%")
	List<ContaPagar> buscaContaDesc(String desc);
	
	@Query("select a from ContaPagar a where a.pessoa.id = ?1")
	List<ContaPagar> buscaContaPorPessoa(Long idpessoa);
	
	@Query("select a from ContaPagar a where a.pessoa_fornecedor.id = ?1")
	List<ContaPagar> buscaContaPorFornecedor(Long idFornecedor);
	
	@Query("select a from ContaPagar a where a.empresa.id = ?1")
	List<ContaPagar> buscaContaPorEmpresa(Long idEmpresa);
	
}
