package br.com.pelegrino.store.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.pelegrino.store.model.Usuario;
import br.com.pelegrino.store.repository.UsuarioRepository;

@Service
public class TarefaAutomatizadaService {
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//@Scheduled(initialDelay = 2000, fixedDelay = 86400000) //Roda a cada 24h
	@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") // Roda todo dia as 11h pelo horário de Brasília
	public void notificarUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
		
		List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();
		
		for (Usuario usuario : usuarios) {
			StringBuilder msg = new StringBuilder();
			msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/>");
			msg.append("Está na hora de trocar sua senha, já passou 90 dias de validade.").append("<br/>");
			msg.append("Troque sua senha na Store");
			
			serviceSendEmail.enviarEmailHtml("Troca de Senha", msg.toString(), usuario.getLogin());
			
			Thread.sleep(3000);
			
		}
		
	}

}
