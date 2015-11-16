package io.epopeia.authorization.helper;

import java.io.ByteArrayOutputStream;

/**
 * Classe para contabilizar as instrucoes DML geradas pelo hibernate e validar a
 * query gerada.
 * 
 * @author Fernando Amaral
 */
public class DMLValidator {

	private ByteArrayOutputStream querySpy;
	private MutableLong counter;

	public DMLValidator() {
		this.querySpy = new ByteArrayOutputStream();
		this.counter = new MutableLong(0);
		HibernateSqlCounter.setOutputs(querySpy, counter);
	}

	public void clearQueryHistory() {
		querySpy.reset();
	}

	public String getLastQuery() {
		String query = querySpy.toString();
		return query;
	}

	public long getStatementCounter() {
		return counter.get();
	}
}
