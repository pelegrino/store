package br.com.pelegrino.money.enums;

public enum StatusContaPagar {

	COBRANCA("Pagar"),
	VENCIDA("Vencida"),
	ABERTA("Aberta"),
	QUITADA("Quitada"),
	RENEGOCIADA("Renegociada");
	
	private String descricao;
	
	StatusContaPagar(String descricao) {
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
