package io.epopeia.authorization.model;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Labels;
import io.epopeia.authorization.model.Message;

/**
 * Teste da estrutura fieldset
 * 
 * @author Fernando Amaral
 */
public class FieldSetTest {

	public enum EFieldsUm {
		UM,
		UM_FIELD_A,
		UM_FIELD_B
	}
	public enum EFieldsDois {
		DOIS,
		DOIS_FIELD_A,
		DOIS_FIELD_B
	}
	public enum EFieldsTres {
		TRES,
		TRES_FIELD_A,
		TRES_FIELD_B
	}
	public enum EFieldsQuatro {
		QUATRO,
		QUATRO_FIELD_A,
		QUATRO_FIELD_B
	}
	public enum EFieldsCinco {
		CINCO,
		CINCO_FIELD_A,
		CINCO_FIELD_B
	}

	public enum ELabels {
		LABEL_A,
		LABEL_B,
		LABEL_C
	}

	public enum EValues {
		VALUE_A,
		VALUE_B,
		VALUE_C
	}

	private FieldSet um;
	private FieldSet dois;
	private FieldSet tres;
	private FieldSet quatro;
	private FieldSet cinco;

	@Before
	public void initFieldSets() {
		um = new FieldSet(EFieldsUm.UM);		
		dois = new FieldSet(EFieldsDois.DOIS);
		tres = new FieldSet(EFieldsTres.TRES);
		quatro = new FieldSet(EFieldsQuatro.QUATRO);
		cinco = new FieldSet(EFieldsCinco.CINCO);

		um.add(dois);
		dois.add(tres);
		tres.add(quatro);
		quatro.add(cinco);

		um.setValue(EFieldsUm.UM_FIELD_A, "UM_VALUE_A");
		um.setValue(EFieldsUm.UM_FIELD_B, "UM_VALUE_B");
		dois.setValue(EFieldsDois.DOIS_FIELD_A, "DOIS_VALUE_A", "desc2A");
		dois.setValue(EFieldsDois.DOIS_FIELD_B, "DOIS_VALUE_B");
		tres.setValue(EFieldsTres.TRES_FIELD_A, "TRES_VALUE_A");
		tres.setValue(EFieldsTres.TRES_FIELD_B, "TRES_VALUE_B");
		quatro.setValue(EFieldsQuatro.QUATRO_FIELD_A, "QUATRO_VALUE_A");
		quatro.setValue(EFieldsQuatro.QUATRO_FIELD_B, "QUATRO_VALUE_B");
		cinco.setValue(EFieldsCinco.CINCO_FIELD_A, "CINCO_VALUE_A");
		cinco.setValue(EFieldsCinco.CINCO_FIELD_B, "CINCO_VALUE_B");
	}

	@Test
	public void testNavigate() {
		assertNull(FieldSet.navigate(um, ""));
		assertNull(FieldSet.navigate(um, "tres.quatro.cinco"));
		assertNull(FieldSet.navigate(um, "dois.tres_field_a"));
		assertNull(FieldSet.navigate(um, "dois.quatro.cinco"));
		assertEquals(cinco, FieldSet.navigate(um, "dois.tres.quatro.cinco"));
		assertEquals(cinco.getComponent(EFieldsCinco.CINCO_FIELD_A),
			FieldSet.navigate(um, "dois.tres.quatro.cinco.cinco_field_a"));
	}

	@Test
	public void testFieldSetDump() {
		String dump = um.dump();

		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("[+UM]");
		pw.println("\t[+DOIS]");
		pw.println("\t\t[DOIS_FIELD_A][DOIS_VALUE_A](desc2A)");
		pw.println("\t\t[DOIS_FIELD_B][DOIS_VALUE_B]");
		pw.println("\t\t[+TRES]");
		pw.println("\t\t\t[+QUATRO]");
		pw.println("\t\t\t\t[+CINCO]");
		pw.println("\t\t\t\t\t[CINCO_FIELD_A][CINCO_VALUE_A]");
		pw.println("\t\t\t\t\t[CINCO_FIELD_B][CINCO_VALUE_B]");
		pw.println("\t\t\t\t[QUATRO_FIELD_A][QUATRO_VALUE_A]");
		pw.println("\t\t\t\t[QUATRO_FIELD_B][QUATRO_VALUE_B]");
		pw.println("\t\t\t[TRES_FIELD_A][TRES_VALUE_A]");
		pw.println("\t\t\t[TRES_FIELD_B][TRES_VALUE_B]");
		pw.println("\t[UM_FIELD_A][UM_VALUE_A]");
		pw.println("\t[UM_FIELD_B][UM_VALUE_B]");
		pw.close();

		assertEquals(sw.getBuffer().toString(), dump);
	}

	@Test
	public void testFieldSetDumpWithLabels() {
		Labels labels = new Labels();
		labels.add(ELabels.LABEL_A);
		labels.add(ELabels.LABEL_B);
		labels.add(ELabels.LABEL_C);

		tres.setLabels(EFieldsTres.TRES_FIELD_A, labels);

		String dump = um.dump();

		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("[+UM]");
        pw.println("\t[+DOIS]");
        pw.println("\t\t[DOIS_FIELD_A][DOIS_VALUE_A](desc2A)");
        pw.println("\t\t[DOIS_FIELD_B][DOIS_VALUE_B]");
        pw.println("\t\t[+TRES]");
        pw.println("\t\t\t[+QUATRO]");
        pw.println("\t\t\t\t[+CINCO]");
        pw.println("\t\t\t\t\t[CINCO_FIELD_A][CINCO_VALUE_A]");
        pw.println("\t\t\t\t\t[CINCO_FIELD_B][CINCO_VALUE_B]");
        pw.println("\t\t\t\t[QUATRO_FIELD_A][QUATRO_VALUE_A]");
        pw.println("\t\t\t\t[QUATRO_FIELD_B][QUATRO_VALUE_B]");
        pw.println("\t\t\t[TRES_FIELD_A][[LABEL_A, LABEL_B, LABEL_C]]");
        pw.println("\t\t\t[TRES_FIELD_B][TRES_VALUE_B]");
        pw.println("\t[UM_FIELD_A][UM_VALUE_A]");
        pw.println("\t[UM_FIELD_B][UM_VALUE_B]");
        pw.close();

        assertEquals(sw.getBuffer().toString(), dump);
	}

	@Test
	public void testFieldSetAddingLabelsByReference() {
		tres.setLabels(EFieldsTres.TRES_FIELD_A, new Labels());
		tres.getLabels(EFieldsTres.TRES_FIELD_A)
			.add(ELabels.LABEL_A)
			.add(ELabels.LABEL_B)
			.add(ELabels.LABEL_C);

		String dump = um.dump();

		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("[+UM]");
        pw.println("\t[+DOIS]");
        pw.println("\t\t[DOIS_FIELD_A][DOIS_VALUE_A](desc2A)");
        pw.println("\t\t[DOIS_FIELD_B][DOIS_VALUE_B]");
        pw.println("\t\t[+TRES]");
        pw.println("\t\t\t[+QUATRO]");
        pw.println("\t\t\t\t[+CINCO]");
        pw.println("\t\t\t\t\t[CINCO_FIELD_A][CINCO_VALUE_A]");
        pw.println("\t\t\t\t\t[CINCO_FIELD_B][CINCO_VALUE_B]");
        pw.println("\t\t\t\t[QUATRO_FIELD_A][QUATRO_VALUE_A]");
        pw.println("\t\t\t\t[QUATRO_FIELD_B][QUATRO_VALUE_B]");
        pw.println("\t\t\t[TRES_FIELD_A][[LABEL_A, LABEL_B, LABEL_C]]");
        pw.println("\t\t\t[TRES_FIELD_B][TRES_VALUE_B]");
        pw.println("\t[UM_FIELD_A][UM_VALUE_A]");
        pw.println("\t[UM_FIELD_B][UM_VALUE_B]");
		pw.close();

		assertEquals(sw.getBuffer().toString(), dump);
	}

	@Test
	public void testFieldSetRemovingLabels() {
		Labels labels = new Labels();
		labels.add(ELabels.LABEL_A);
		labels.add(ELabels.LABEL_B);
		labels.add(ELabels.LABEL_C);

		tres.setLabels(EFieldsTres.TRES_FIELD_A, labels);
		tres.getLabels(EFieldsTres.TRES_FIELD_A).remove(ELabels.LABEL_B);

		String dump = um.dump();

		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("[+UM]");
        pw.println("\t[+DOIS]");
        pw.println("\t\t[DOIS_FIELD_A][DOIS_VALUE_A](desc2A)");
        pw.println("\t\t[DOIS_FIELD_B][DOIS_VALUE_B]");
        pw.println("\t\t[+TRES]");
        pw.println("\t\t\t[+QUATRO]");
        pw.println("\t\t\t\t[+CINCO]");
        pw.println("\t\t\t\t\t[CINCO_FIELD_A][CINCO_VALUE_A]");
        pw.println("\t\t\t\t\t[CINCO_FIELD_B][CINCO_VALUE_B]");
        pw.println("\t\t\t\t[QUATRO_FIELD_A][QUATRO_VALUE_A]");
        pw.println("\t\t\t\t[QUATRO_FIELD_B][QUATRO_VALUE_B]");
        pw.println("\t\t\t[TRES_FIELD_A][[LABEL_A, LABEL_C]]");
        pw.println("\t\t\t[TRES_FIELD_B][TRES_VALUE_B]");
        pw.println("\t[UM_FIELD_A][UM_VALUE_A]");
        pw.println("\t[UM_FIELD_B][UM_VALUE_B]");
		pw.close();

		assertEquals(sw.getBuffer().toString(), dump);
	}

	@Test
	public void testFieldSetAddingEnumValue() {
		tres.setValue(EFieldsTres.TRES_FIELD_A, EValues.VALUE_A);

		String dump = um.dump();

		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("[+UM]");
        pw.println("\t[+DOIS]");
        pw.println("\t\t[DOIS_FIELD_A][DOIS_VALUE_A](desc2A)");
        pw.println("\t\t[DOIS_FIELD_B][DOIS_VALUE_B]");
        pw.println("\t\t[+TRES]");
        pw.println("\t\t\t[+QUATRO]");
        pw.println("\t\t\t\t[+CINCO]");
        pw.println("\t\t\t\t\t[CINCO_FIELD_A][CINCO_VALUE_A]");
        pw.println("\t\t\t\t\t[CINCO_FIELD_B][CINCO_VALUE_B]");
        pw.println("\t\t\t\t[QUATRO_FIELD_A][QUATRO_VALUE_A]");
        pw.println("\t\t\t\t[QUATRO_FIELD_B][QUATRO_VALUE_B]");
        pw.println("\t\t\t[TRES_FIELD_A][VALUE_A]");
        pw.println("\t\t\t[TRES_FIELD_B][TRES_VALUE_B]");
        pw.println("\t[UM_FIELD_A][UM_VALUE_A]");
        pw.println("\t[UM_FIELD_B][UM_VALUE_B]");
		pw.close();

		assertEquals(sw.getBuffer().toString(), dump);
	}

	@Test
	public void testFieldSetCompareEnumValue() {
		tres.setValue(EFieldsTres.TRES_FIELD_A, EValues.VALUE_A);
		Enum<?> expected = tres.getEnum(EFieldsTres.TRES_FIELD_A, EValues.class);
		assertEquals(EValues.VALUE_A, expected);
	}

	@Test
	public void testFieldSetCompareEmptyEnumValue() {
		tres.setValue(EFieldsTres.TRES_FIELD_A, "");
		Enum<?> expected = tres.getEnum(EFieldsTres.TRES_FIELD_A, EValues.class);
		assertEquals(Message.EmptyEnum.EMPTY_ENUM, expected);
	}
}
