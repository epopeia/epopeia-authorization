package io.epopeia.authorization.model;

import java.util.Iterator;

/**
 * Implementacao de um iterador dummy para as folhas
 * de um padrao composite.
 * 
 * @author Fernando Amaral
 */
public class FieldIterator implements Iterator<Message> {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Message next() {
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
