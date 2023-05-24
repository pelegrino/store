package br.com.pelegrino.store.security;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.pelegrino.store.ApplicationContextLoad;
import br.com.pelegrino.store.model.Usuario;
import br.com.pelegrino.store.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//Criar e retornar autenticação JWT
@Service
@Component
public class JWTTokenAutenticacaoService {
	
	//Token de validade de 30 dias
	private static final long EXPIRATION_TIME = 864000000;
	
	//Chave de senha para juntar no JWT
	private static final String SECRET = "#,G798IHB>";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	//Gera o token e responde ao cliente com o JWT
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
		
		//Montagem do Token
		String JWT = Jwts.builder() //Chama o gerador do token
						.setSubject(username) //Adiciona o nome
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //Tempo de Expiração
						.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		//Ex.: Bearer *jddfbrufrghrq8gh377834u474y3ip*
		String token = TOKEN_PREFIX + " " + JWT;
		
		//Dá a resposta na tela, API, outro cliente, etc.
		response.addHeader(HEADER_STRING, token);
		
		liberacaoCors(response);
		
		//Usado para ver no Postman
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		
	}
	
	//Retorna o usuário validado com o token
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			
			//Faz a validação do token e obter o usuário
			String user = Jwts.parser()
								.setSigningKey(SECRET)
								.parseClaimsJws(tokenLimpo)
								.getBody()
								.getSubject();
			
			if (user != null) {
				
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
										.getBean(UsuarioRepository.class)
										.findUserByLogin(user);
				
				if (usuario != null) {
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getSenha(),
							usuario.getAuthorities());
				}
			}
			
		}
	
		liberacaoCors(response);
		return null;
		
	}
	
	//Fazendo liberação contra erro de cors no navegador
	private void liberacaoCors(HttpServletResponse response) {
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
	}

}
