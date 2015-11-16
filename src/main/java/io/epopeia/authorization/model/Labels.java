package io.epopeia.authorization.model;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Implementacao dos rotulos como um set de chaves somente
 * Rotulos nao possuem valores. O proprio rotulo e sua
 * presenca na mensagem eh o valor.
 * 
 * @author Fernando Amaral
 */
public class Labels {

	private Set<String> labels;

	public Labels() {
		labels = new LinkedHashSet<String>();
	}

	public Labels add(Enum<?> label) {
		labels.add(label.name());
		return this;
	}

	public Labels remove(Enum<?> label) {
		labels.remove(label.name());
		return this;
	}

	public boolean contains(Enum<?> label) {
		return labels.contains(label.name());
	}

	public int length() {
		return labels.size();
	}

	public Labels clear() {
		labels.clear();
		return this;
	}

	public Labels getLabels() {
		return this;
	}

	@Override
	public String toString() {
		return getLabelsAsString();
	}

	public String getLabelsAsString() {
		return Arrays.toString(this.getLabelsAsArray());
	}

	public String[] getLabelsAsArray() {
		Object[] array = labels.toArray();
		return Arrays.copyOf(array, array.length, String[].class);
	}
}
