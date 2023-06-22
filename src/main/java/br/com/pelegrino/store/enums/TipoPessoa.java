package br.com.pelegrino.store.enums;

public enum TipoPessoa {
	
	JURIDICA("Jurídica"),
	JURIDICA_FORNECEDOR("Jurídica e Fornecedor"),
	FISICA("Física");
	
	
	private String descricao;
	
	private TipoPessoa(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
}
