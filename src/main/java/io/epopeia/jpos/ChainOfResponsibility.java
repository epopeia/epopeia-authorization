package io.epopeia.jpos;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jdom.Element;
import org.jpos.core.ConfigurationException;
import org.jpos.q2.QFactory;
import org.jpos.transaction.TransactionManager;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.context.ApplicationContext;

import io.epopeia.authorization.spring.SpringContext;

/**
 * This specialization of original jpos Transaction Manager is designed to override
 * the createParticipant method that jpos uses to create participants since these
 * instances will not be handled by spring context if we let jpos create by it self
 * without register in spring container.
 *
 * @author Fernando Amaral
 */
public class ChainOfResponsibility extends TransactionManager {

	private ApplicationContext springContext = SpringContext.getSpringContainer();

	@Override
	public TransactionParticipant createParticipant(Element e) throws ConfigurationException {
        TransactionParticipant participant = null;
		try {
			QFactory factory = getFactory();
			Class<?> classType = Class.forName(e.getAttributeValue("class"));
			participant = (TransactionParticipant) springContext.getBean(classType);
	            //factory.newInstance (TransactionParticipant, e.getAttributeValue("class"));
	        factory.setLogger (participant, e);
	        QFactory.invoke (participant, "setTransactionManager", this, TransactionManager.class);
	        factory.setConfiguration (participant, e);
		} catch (ClassNotFoundException e1) {
			StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e1.printStackTrace(pw);
	        pw.close();
			getLog().fatal(sw.getBuffer().toString());
		}
		return participant;
	}
}
