package br.com.pelegrino.store;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.pelegrino.store.model.dto.ObjetoErroDTO;
import br.com.pelegrino.store.service.ServiceSendEmail;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@ExceptionHandler(ExceptionStore.class)
	public ResponseEntity<Object> handleExceptionCustom (ExceptionStore ex) {
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
		
		objetoErroDTO.setError(ex.getMessage());
		objetoErroDTO.setCodError(HttpStatus.OK.toString());
		
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.OK);
		
	}
	
	//Captura exceções do projeto
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
		
		String msg = "";
		
		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			
			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n";
			}
			
		} else if (ex instanceof HttpMessageNotReadableException) {
			
			msg = "Não está sendo enviado dados no corpo da requisição.";
		
		} else {
			msg = ex.getMessage();
			
		}
		
		objetoErroDTO.setError(msg);
		objetoErroDTO.setCodError(status.value() + " ==> " + status.getReasonPhrase());
		
		ex.printStackTrace();
		
		try {
			serviceSendEmail.enviarEmailHtml("Erro na loja virtual", ExceptionUtils.getStackTrace(ex), "pelegrino@gmail.com");
			
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	//Captura erro no BD
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {
		
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
		
		String msg = "";
		
		if (ex instanceof DataIntegrityViolationException) {
			msg = "Erro de integridade no Banco de Dados " + ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
			
		} else {
			msg = ex.getMessage();
			
		}
		
		if (ex instanceof ConstraintViolationException) {
			msg = "Erro de Chave Estrangeira " + ((ConstraintViolationException) ex).getCause().getCause().getMessage();
			
		} else {
			msg = ex.getMessage();
			
		}
		
		if (ex instanceof SQLException) {
			msg = "Erro de SQL do Banco de Dados " + ((SQLException) ex).getCause().getCause().getMessage();
			
		} else {
			msg = ex.getMessage();
			
		}
		
		objetoErroDTO.setError(msg);
		objetoErroDTO.setCodError(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		
		ex.printStackTrace();
		
		try {
			serviceSendEmail.enviarEmailHtml("Erro na loja virtual", ExceptionUtils.getStackTrace(ex), "pelegrino@gmail.com");
			
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

}
