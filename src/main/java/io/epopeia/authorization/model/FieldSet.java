package io.epopeia.authorization.model;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementacao dos troncos de um padrao Composite. A estrutura da mensagem
 * fica sendo uma arvore com diversos troncos e folhas.
 * 
 * @author Fernando Amaral
 */
public class FieldSet extends Message {
	private static final Logger logger = LoggerFactory
			.getLogger(FieldSet.class);
	private Map<String, Message> items;
	private Enum<?> key;

	public FieldSet(Enum<?> key) {
		this.items = new TreeMap<String, Message>();
		this.key = key;
	}

	@Override
	public void add(Message item) {
		if (item != null)
			items.put(item.getKey().name(), item);
	}

	@Override
	public Message getComponent(Enum<?> key) {
		return items.get(key.name());
	}

	@Override
	public Message getComponent(String key) {
		return items.get(key);
	}

	@Override
	public String getValue(Enum<?> key) {
		Message c = getComponent(key);
		return c != null ? c.getValue() : EMPTY_STRING;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Enum<?> getEnum(Enum<?> key, Class<?> enumType) {
		Message c = getComponent(key);
		if (c != null && enumType != null) {
			try {
				return Enum.valueOf((Class<Enum>) enumType, c.getValue());
			} catch (Exception e) {
				logger.warn(
						"Catch Exception converting value of field {} to Enum",
						key);
			}
		}
		return EmptyEnum.EMPTY_ENUM;
	}

	@Override
	public Labels getLabels(Enum<?> key) {
		Message c = getComponent(key);
		return c != null ? c.getLabels() : EMPTY_LABELS;
	}

	@Override
	public Integer getValueAsInteger(Enum<?> key) {
		Message c = getComponent(key);
		return c != null ? c.getValueAsInteger() : null;
	}

	@Override
	public Long getValueAsLong(Enum<?> key) {
		Message c = getComponent(key);
		return c != null ? c.getValueAsLong() : null;
	}

	@Override
	public Date getValueAsDate(Enum<?> key) {
		Message c = getComponent(key);
		return c != null ? c.getValueAsDate() : null;
	}

	@Override
	public Date getValueAsDate(Enum<?> key, String mask) {
		Message c = getComponent(key);
		return c != null ? c.getValueAsDate(mask) : null;
	}

	@Override
	public BigDecimal getValueAsBigDecimal(Enum<?> key) {
		Message c = getComponent(key);
		return c != null ? c.getValueAsBigDecimal() : null;
	}

	@Override
	public BigDecimal getValueAsBigDecimal(Enum<?> key, int fator, int scale,
			boolean div) {
		Message c = getComponent(key);
		return c != null ? c.getValueAsBigDecimal(fator, scale, div) : null;
	}

	@Override
	public void setValue(Enum<?> key, String value, String description) {
		if (value != null && !value.isEmpty())
			this.add(new Field(key, value, description));
	}

	@Override
	public void setValue(Enum<?> key, Enum<?> value, String description) {
		if (value != null)
			this.add(new Field(key, value, description));
	}

	@Override
	public void setValue(Enum<?> key, String value) {
		if (value != null && !value.isEmpty())
			this.add(new Field(key, value));
	}

	@Override
	public void setValue(Enum<?> key, Enum<?> value) {
		if (value != null)
			this.add(new Field(key, value));
	}

	@Override
	public void setValue(Enum<?> key, String value, boolean sensitive) {
		if (value != null && !value.isEmpty())
			this.add(new Field(key, value, sensitive));
	}

	@Override
	public void setValue(Enum<?> key, Enum<?> value, boolean sensitive) {
		if (value != null)
			this.add(new Field(key, value, sensitive));
	}

	@Override
	public void setLabels(Enum<?> key, Labels labels) {
		if (labels != null)
			this.add(new Field(key, labels));
	}

	@Override
	public void setLabels(Enum<?> key, Labels labels, boolean sensitive) {
		if (labels != null)
			this.add(new Field(key, labels, sensitive));
	}

	@Override
	public Enum<?> getKey() {
		return key;
	}

	@Override
	public String dump() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		Iterator<Message> it = new FieldSetIterator(createIterator());
		this.dump(ps, it);
		return baos.toString();
	}

	@Override
	protected void dump(PrintStream p, Iterator<Message> it) {
		p.println("[+" + key + ']');
		while (it.hasNext()) {
			for (int i = 0; i < ((FieldSetIterator) it).getDeepLevel(); i++)
				p.print("\t");
			Message component = it.next();
			component.dump(p, it);
		}
	}

	@Override
	public Iterator<Message> createIterator() {
		return items.values().iterator();
	};

	public static Message navigate(final Message root, String fsPath) {
		Message chield = null;
		Message parent = root;
		for (String chieldName : fsPath.split("\\.")) {
			chield = parent.getComponent(chieldName.toUpperCase());
			if (chield != null && chield instanceof FieldSet) {
				parent = chield;
				continue;
			}
			break;
		}
		return chield;
	}
}
