package io.epopeia.authorization.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Configuracao do framework Authorization
 * 
 * @author Fernando Amaral
 */
@Configuration
@PropertySources ({
	@PropertySource(name="authorizationProperties", value="classpath:/authorization.properties", ignoreResourceNotFound=true),
	@PropertySource(name="authorizationProperties", value="file:/opt/epopeia-authorization/config/authorization.properties", ignoreResourceNotFound=true)
})
@ComponentScan(basePackages = "io.epopeia")
public class AuthorizationConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
