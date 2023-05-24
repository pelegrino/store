package br.com.pelegrino.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.pelegrino.store.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	
	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);

}
