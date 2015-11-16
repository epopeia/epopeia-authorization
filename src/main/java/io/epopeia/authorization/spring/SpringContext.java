package io.epopeia.authorization.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Classe responsavel por manipular o contexto do Spring visto que nem todos as
 * classes sao beans gerenciados pelo Spring as injecoes por autowired nao serao
 * possiveis.
 * 
 * @author Fernando Amaral
 */
public final class SpringContext {

	private final static ApplicationContext springContext = new AnnotationConfigApplicationContext(AuthorizationConfig.class);

	public final static ApplicationContext getSpringContainer() {
		return springContext;
	}
}
