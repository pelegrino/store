package br.com.pelegrino.store.security;

import java.sql.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
		
		//Usado para ver no Postman
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		
	}

}
