package io.epopeia.authorization.model;

import java.util.Iterator;
import java.util.Stack;

/**
 * Implementacao de um iterador de tronco da arvore.
 * Cada sub-tronco da arvore eh iterado para obter
 * todos as folhas de cada tronco. O dump da arvore
 * completa utiliza tabulacoes para melhor visualizacao
 * da estrutura.
 * 
 * @author Fernando Amaral
 */
public class FieldSetIterator implements Iterator<Message> {
	private Stack<Iterator<Message>> stack;
	private int deepLevel;

	public FieldSetIterator(Iterator<Message> iterator) {
		stack = new Stack<Iterator<Message>>();
		stack.push(iterator);
		deepLevel = 1;
	}

	@Override
	public Message next() {		
		if(hasNext()) {
			Iterator<Message> iterator = stack.peek();
			Message component = iterator.next();
			if(component instanceof FieldSet) {
				stack.push(component.createIterator());
				deepLevel++;
			}
			return component;
		}		
		return null;
	}

	@Override
	public boolean hasNext() {
		if(stack.empty())
			return false;

		Iterator<Message> iterator = stack.peek();
		if(!iterator.hasNext()) {
			stack.pop();
			if(deepLevel > 1)
				deepLevel--;
			return hasNext();
		}
		return true;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public int getDeepLevel() {
		return deepLevel;
	}
}
