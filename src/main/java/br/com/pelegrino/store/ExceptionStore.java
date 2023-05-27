package br.com.pelegrino.store;

public class ExceptionStore extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ExceptionStore(String msgErro) {
		super(msgErro);
	}

}
