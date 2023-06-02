package br.com.pelegrino.store.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.pelegrino.store.model.PessoaJuridica;
import br.com.pelegrino.store.model.Usuario;
import br.com.pelegrino.store.repository.PessoaRepository;
import br.com.pelegrino.store.repository.UsuarioRepository;

@Service
public class PessoaUserService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PessoaRepository pessoaRepository; 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica) {
		
		//juridica = pessoaRepository.save(juridica);
		
		for (int i = 0; i < juridica.getEnderecos().size(); i++) {
			juridica.getEnderecos().get(i).setPessoa(juridica);
			juridica.getEnderecos().get(i).setEmpresa(juridica);
			
		}
		
		juridica = pessoaRepository.save(juridica);
		
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());
		
		if (usuarioPj == null) {
			String constraint = usuarioRepository.consultaConstraintAcesso();
			
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acessos drop constraint " + constraint + "; commit;");
			}
			
			usuarioPj = new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(juridica);
			usuarioPj.setPessoa(juridica);
			usuarioPj.setLogin(juridica.getEmail());
			
			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPj.setSenha(senhaCript);
			
			usuarioPj = usuarioRepository.save(usuarioPj);
			
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId());
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");
			
			StringBuilder mensagemHtml = new StringBuilder();
			
			mensagemHtml.append("<b>Segue abaixo seus dados de acesso para a Store</b><br/><br/>");
			mensagemHtml.append("<b>Login: </b>" + juridica.getEmail() + "<br/>");
			mensagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
			mensagemHtml.append("Obrigado");
			
			
			try {
				serviceSendEmail.enviarEmailHtml("Acesso gerado para Store", mensagemHtml.toString(), juridica.getEmail());
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}

		}
		
		return juridica;
	}

}
