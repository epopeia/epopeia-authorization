package io.epopeia.authorization.model;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

/**
 * Definicao da interface para implementacao de um padrao Composite para a
 * estrutura da mensagem. Esse eh um container para armazenar os campos que
 * serao trafegados pelo framework em uma estrutura em arvore.
 * 
 * @author Fernando Amaral
 */
public abstract class Message {
	protected static final String EMPTY_STRING = "";
	protected static final Labels EMPTY_LABELS = new Labels();

	public static enum EmptyEnum {
		EMPTY_ENUM
	}

	/* Field operations */
	public String getValue() {
		throw new UnsupportedOperationException();
	}

	public Labels getLabels() {
		throw new UnsupportedOperationException();
	}

	public Integer getValueAsInteger() {
		throw new UnsupportedOperationException();
	}

	public Long getValueAsLong() {
		throw new UnsupportedOperationException();
	}

	public Date getValueAsDate() {
		throw new UnsupportedOperationException();
	}

	public Date getValueAsDate(String mask) {
		throw new UnsupportedOperationException();
	}

	public BigDecimal getValueAsBigDecimal() {
		throw new UnsupportedOperationException();
	}

	public BigDecimal getValueAsBigDecimal(int fator, int scale, boolean div) {
		throw new UnsupportedOperationException();
	}

	public void setValue(String value) {
		throw new UnsupportedOperationException();
	}

	public void setValue(Enum<?> value) {
		throw new UnsupportedOperationException();
	}

	public void setLabels(Labels labels) {
		throw new UnsupportedOperationException();
	}

	/* FieldSet operations */
	public void add(Message component) {
		throw new UnsupportedOperationException();
	}

	public Message getComponent(Enum<?> key) {
		throw new UnsupportedOperationException();
	}

	public Message getComponent(String key) {
		throw new UnsupportedOperationException();
	}

	public String getValue(Enum<?> key) {
		throw new UnsupportedOperationException();
	}

	public Enum<?> getEnum(Enum<?> key, Class<?> enumType) {
		throw new UnsupportedOperationException();
	}

	public Labels getLabels(Enum<?> key) {
		throw new UnsupportedOperationException();
	}

	public Integer getValueAsInteger(Enum<?> key) {
		throw new UnsupportedOperationException();
	}

	public Long getValueAsLong(Enum<?> key) {
		throw new UnsupportedOperationException();
	}

	public Date getValueAsDate(Enum<?> key) {
		throw new UnsupportedOperationException();
	}

	public Date getValueAsDate(Enum<?> key, String mask) {
		throw new UnsupportedOperationException();
	}

	public BigDecimal getValueAsBigDecimal(Enum<?> key) {
		throw new UnsupportedOperationException();
	}

	public BigDecimal getValueAsBigDecimal(Enum<?> key, int fator, int scale,
			boolean div) {
		throw new UnsupportedOperationException();
	}

	public void setValue(Enum<?> key, String value, String description) {
		throw new UnsupportedOperationException();
	}

	public void setValue(Enum<?> key, Enum<?> value, String description) {
		throw new UnsupportedOperationException();
	}

	public void setValue(Enum<?> key, String value) {
		throw new UnsupportedOperationException();
	}

	public void setValue(Enum<?> key, Enum<?> value) {
		throw new UnsupportedOperationException();
	}

	public void setLabels(Enum<?> key, Labels labels) {
		throw new UnsupportedOperationException();
	}

	public void setValue(Enum<?> key, String value, boolean sensitive) {
		throw new UnsupportedOperationException();
	}

	public void setValue(Enum<?> key, Enum<?> value, boolean sensitive) {
		throw new UnsupportedOperationException();
	}

	public void setLabels(Enum<?> key, Labels labels, boolean sensitive) {
		throw new UnsupportedOperationException();
	}

	/* Global operations */
	public Enum<?> getKey() {
		throw new UnsupportedOperationException();
	}

	public String dump() {
		throw new UnsupportedOperationException();
	}

	protected void dump(PrintStream p, Iterator<Message> it) {
		throw new UnsupportedOperationException();
	}

	public Iterator<Message> createIterator() {
		throw new UnsupportedOperationException();
	}

}
