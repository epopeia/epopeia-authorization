package io.epopeia.authorization.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Sniffer criado usando um filter do framework logback para capturar as querys
 * SQL que o hibernate gera, podendo ser utilizado para validar em unit testes.
 * 
 * @author Fernando Amaral
 */
public class HibernateSqlCounter extends Filter<ILoggingEvent> {

	private static ByteArrayOutputStream stream = null;
	private static MutableLong counter = null;

	public static void setOutputs(ByteArrayOutputStream stream, MutableLong counter) {
		HibernateSqlCounter.stream = stream;
		HibernateSqlCounter.counter = counter;
	}

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (stream != null) {
			StackTraceElement e = event.getCallerData()[0];
			if (e.getClassName().equals(
					"org.hibernate.engine.jdbc.spi.SqlStatementLogger")
					&& e.getMethodName().equals("logStatement")) {
				String message = event.getMessage();
				if (message.contains("select")) {
					try {
						counter.increment();
						stream.write(message.getBytes());
						stream.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return FilterReply.ACCEPT;
	}
}
