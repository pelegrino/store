package br.com.pelegrino.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.pelegrino.store.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1));")
	public boolean existeProduto(String nomeCategoria);

	@Query("select a from Produto a where upper(trim(a.nome)) like %?1%")
	public List<Produto> buscaProdutoNome(String nome);
}
