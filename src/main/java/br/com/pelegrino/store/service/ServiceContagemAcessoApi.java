package br.com.pelegrino.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServiceContagemAcessoApi {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void atualizaAcessoEndPointPF() {
		
		jdbcTemplate.execute("begin; UPDATE tabela_acesso_end_point SET qtd_acesso_end_point = qtd_acesso_end_point + 1 where nome_end_point = 'END_POINT_NOME_PESSOA_FISICA'; commit;");
		
	}

}
