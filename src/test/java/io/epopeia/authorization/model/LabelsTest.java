package io.epopeia.authorization.model;

import static org.junit.Assert.*;

import org.junit.Test;

import io.epopeia.authorization.model.Labels;

/**
 * Teste do estrutura labels
 * 
 * @author Fernando Amaral
 */
public class LabelsTest {

	public enum ELabels {
		LABEL_A,
		LABEL_B,
		LABEL_C
	}

	@Test
	public void testEmptyLabelsDump() {
		Labels labels = new Labels();

		String dumpToString1 = labels.toString();
		String dumpToString2 = labels.getLabelsAsString();
		String[] dumpToArray = labels.getLabelsAsArray();

		assertEquals("[]", dumpToString1);
		assertEquals("[]", dumpToString2);
		assertEquals(0, labels.length());
		assertEquals(0, dumpToArray.length);
	}

	@Test
	public void testLabelsDump() {
		Labels labels = new Labels();
		labels.add(ELabels.LABEL_A);
		labels.add(ELabels.LABEL_B);
		labels.add(ELabels.LABEL_C);

		String dumpToString1 = labels.toString();
		String dumpToString2 = labels.getLabelsAsString();
		String[] dumpToArray = labels.getLabelsAsArray();

		assertEquals("[LABEL_A, LABEL_B, LABEL_C]", dumpToString1);
		assertEquals("[LABEL_A, LABEL_B, LABEL_C]", dumpToString2);
		assertEquals(3, labels.length());
		assertEquals(3, dumpToArray.length); 
		assertEquals(dumpToArray[0], "LABEL_A");
		assertEquals(dumpToArray[1], "LABEL_B");
		assertEquals(dumpToArray[2], "LABEL_C");
	}

	@Test
	public void testLabelsClear() {
		Labels labels = new Labels();
		labels.add(ELabels.LABEL_A);
		labels.clear();

		String dumpToString1 = labels.toString();
		String dumpToString2 = labels.getLabelsAsString();
		String[] dumpToArray = labels.getLabelsAsArray();

		assertEquals("[]", dumpToString1);
		assertEquals("[]", dumpToString2);
		assertEquals(0, labels.length());
		assertEquals(0, dumpToArray.length);
	}

	@Test
	public void testLabelsContains() {
		Labels labels = new Labels();
		labels.add(ELabels.LABEL_A);
		assertEquals(true, labels.contains(ELabels.LABEL_A));
	}

	@Test
	public void testLabelsNotContains() {
		Labels labels = new Labels();
		labels.add(ELabels.LABEL_A);
		assertEquals(false, labels.contains(ELabels.LABEL_B));
	}
}
