package io.epopeia.authorization.builder;

import io.epopeia.authorization.domain.backoffice.TransactionType;
import io.epopeia.authorization.faker.TransactionTypeFaker;
import io.epopeia.authorization.faker.TransactionTypeFaker.ETransactionType;

/**
 * Builder de objetos TransactionType
 * 
 * @author Fernando Amaral
 */
public class TransactionTypeBuilder {

	private TransactionType tipoTransacao;

	public TransactionTypeBuilder() {
		this.tipoTransacao = TransactionTypeFaker.getTransactionType();
	}

	public TransactionTypeBuilder transactionType(ETransactionType eTransactionType) {
		this.tipoTransacao = TransactionTypeFaker.getTransactionType(eTransactionType);
		return this;
	}

	public TransactionType create() {
		return tipoTransacao;
	}
}
