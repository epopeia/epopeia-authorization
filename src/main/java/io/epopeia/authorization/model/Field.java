package io.epopeia.authorization.model;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementacao das folhas de um padrao Composite. As folhas sao um conjunto
 * unico de chave valor.
 * 
 * @author Fernando Amaral
 */
public class Field extends Message {
	private static final Logger logger = LoggerFactory.getLogger(Field.class);
	private Enum<?> key;
	private String value;
	private Labels labels;
	private boolean sensitive;
	private boolean useLabels;
	private String description;

	public Field(Enum<?> key, String value, String description) {
		this(key, value, false);
		this.description = description != null ? description : EMPTY_STRING;
	}

	public Field(Enum<?> key, Enum<?> value, String description) {
		this(key, value.name(), false);
		this.description = description != null ? description : EMPTY_STRING;
	}

	public Field(Enum<?> key, String value) {
		this(key, value, false);
	}

	public Field(Enum<?> key, Enum<?> value) {
		this(key, value.name(), false);
	}

	public Field(Enum<?> key, String value, boolean sensitive) {
		this.key = key;
		this.value = value == null ? EMPTY_STRING : value;
		this.labels = EMPTY_LABELS;
		this.useLabels = false;
		this.sensitive = sensitive;
		this.description = EMPTY_STRING;
	}

	public Field(Enum<?> key, Enum<?> value, boolean sensitive) {
		this(key, value.name(), sensitive);
	}

	public Field(Enum<?> key, Labels labels) {
		this(key, labels, false);
	}

	public Field(Enum<?> key, Labels labels, boolean sensitive) {
		this.key = key;
		if (labels == null) {
			this.labels = EMPTY_LABELS;
			this.value = EMPTY_STRING;
			this.useLabels = false;
		} else {
			this.labels = labels;
			this.useLabels = true;
			this.value = EMPTY_STRING;
		}
		this.sensitive = sensitive;
		this.description = EMPTY_STRING;
	}

	@Override
	public Enum<?> getKey() {
		return key;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public Labels getLabels() {
		return labels;
	}

	@Override
	public Integer getValueAsInteger() {
		// Check if value is not null and is not empty
		if (value != null && !value.isEmpty()) {
			try {
				return Integer.parseInt(value, 10);
			} catch (NumberFormatException e) {
				logger.warn(
						"Catch Exception converting value of field {} to Integer",
						key);
				e.printStackTrace(System.err);
			}
		}
		return null;
	}

	@Override
	public Long getValueAsLong() {
		// Check if value is not null and is not empty
		if (value != null && !value.isEmpty()) {
			try {
				return Long.parseLong(value, 10);
			} catch (NumberFormatException e) {
				logger.warn(
						"Catch Exception converting value of field {} to Long",
						key);
				e.printStackTrace(System.err);
			}
		}
		return null;
	}

	@Override
	public Date getValueAsDate(String mask) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			if (mask != null && mask.equals("MMDDHHmmss"))
				return sdf.parse(cal.get(Calendar.YEAR) + value);
			else if (mask != null && mask.equals("yyyyMMddHHmmss"))
				return sdf.parse(value);
		} catch (Exception e) {
			logger.warn("Catch Exception converting value of field {} to Date",
					key);
			e.printStackTrace(System.err);
		}
		return null;
	}

	@Override
	public Date getValueAsDate() {
		return getValueAsDate("yyyyMMddHHmmss");
	}

	@Override
	public BigDecimal getValueAsBigDecimal() {
		// Check if value is not null and is not empty
		if (value != null && !value.isEmpty()) {
			try {
				return new BigDecimal(value);
			} catch (ArithmeticException e) {
				logger.warn(
						"Catch Exception converting value of field {} to BigDecimal",
						key);
				e.printStackTrace(System.err);
			} catch (NumberFormatException e) {
				logger.warn(
						"Catch Exception converting value of field {} to BigDecimal",
						key);
				e.printStackTrace(System.err);
			} catch (Exception e) {
				logger.warn(
						"Catch Exception converting value of field {} to BigDecimal",
						key);
				e.printStackTrace(System.err);
			}
		}
		return null;
	}

	@Override
	public BigDecimal getValueAsBigDecimal(int fator, int scale, boolean div) {
		// Check if value is not null and is not empty
		if (value != null && !value.isEmpty()) {
			try {
				if (div)
					return new BigDecimal(value).divide(new BigDecimal(fator),
							scale, BigDecimal.ROUND_DOWN);
				else
					return new BigDecimal(value)
							.multiply(new BigDecimal(fator));
			} catch (ArithmeticException e) {
				logger.warn(
						"Catch Exception converting value of field {} to BigDecimal",
						key);
				e.printStackTrace(System.err);
			} catch (NumberFormatException e) {
				logger.warn(
						"Catch Exception converting value of field {} to BigDecimal",
						key);
				e.printStackTrace(System.err);
			} catch (Exception e) {
				logger.warn(
						"Catch Exception converting value of field {} to BigDecimal",
						key);
				e.printStackTrace(System.err);
			}
		}
		return null;
	}

	@Override
	public String dump() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		Iterator<Message> it = createIterator();
		this.dump(ps, it);
		return baos.toString();
	}

	@Override
	protected void dump(PrintStream p, Iterator<Message> it) {
		boolean hasDescription = description != null && !description.isEmpty();
		p.println("["
				+ key
				+ "]["
				+ (sensitive ? wipe() : (useLabels ? labels.toString() : value))
				+ "]"
				+ (hasDescription ? "(" + description + ")" : EMPTY_STRING));
	}

	private String wipe() {
		return "WIPED(len:" + value.length() + ")";
	}

	@Override
	public Iterator<Message> createIterator() {
		// Return a dummy iterator that does nothing
		return new FieldIterator();
	};
}
