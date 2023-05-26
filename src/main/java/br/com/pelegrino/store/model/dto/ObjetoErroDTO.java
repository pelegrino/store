package br.com.pelegrino.store.model.dto;

import java.io.Serializable;

public class ObjetoErroDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String error;
	private String codError;
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	public String getCodError() {
		return codError;
	}
	
	public void setCodError(String codError) {
		this.codError = codError;
	}

}
