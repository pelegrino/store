package br.com.pelegrino.store;

import br.com.pelegrino.store.util.ValidaCNPJ;
import br.com.pelegrino.store.util.ValidaCPF;

public class TesteCPFCNPJ {
	
	public static void main(String[] args) {
		boolean isCnpj = ValidaCNPJ.isCNPJ("43.610.502/0001-46");
		System.out.println("CNPJ válido: " + isCnpj);

		boolean isCpf = ValidaCPF.isCPF("271.975.648-23");
		System.out.println("CPF válido: " + isCpf);
	}

}
