package br.com.pelegrino.store.model.dto;

import java.io.Serializable;

public class CategoriaProdutoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nomedesc;
	private String empresa;
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNomedesc() {
		return nomedesc;
	}
	
	public void setNomedesc(String nomedesc) {
		this.nomedesc = nomedesc;
	}
	
	public String getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
}
