package io.epopeia.authorization.api;

import java.io.Serializable;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;

import io.epopeia.authorization.bo.AutorizadoraBO;
import io.epopeia.authorization.spring.SpringContext;

/**
 * Wrapper to GroupSelector interface of jpos that
 * add a method execute with the throws Exception for we be
 * able to handle errors in the ChainOfResponsibility layer.
 *
 * @author Fernando Amaral
 */
public abstract class ChainHandlerSelector extends ChainHandler implements GroupSelector, Configurable {

	private Configuration cfg;

	public abstract String select(Context context) throws Exception;

	@Override
	public int execute(Context context) throws Exception {
		return PREPARED | READONLY | NO_JOIN;
	}

	@Override
	public String select(long id, Serializable context) {
		try {
			return select((Context)context);
		} catch (Exception e) {
			SpringContext.getSpringContainer().getBean(AutorizadoraBO.class)
				.logError(this.getClass().getName(), e, context);
		}
		return null;
	}

	@Override
	public void setConfiguration(Configuration cfg) throws ConfigurationException {
		this.cfg = cfg;		
	}

	public final Configuration configuration() {
		return cfg;
	}
}
