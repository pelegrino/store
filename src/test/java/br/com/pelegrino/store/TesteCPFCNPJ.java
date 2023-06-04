package br.com.pelegrino.store;

import br.com.pelegrino.store.util.ValidaCNPJ;

public class TesteCPFCNPJ {
	
	public static void main(String[] args) {
		boolean isCnpj = ValidaCNPJ.isCNPJ("43.610.502/0001-47");
		
		System.out.println("CNPJ válido: " + isCnpj);
	}

}
