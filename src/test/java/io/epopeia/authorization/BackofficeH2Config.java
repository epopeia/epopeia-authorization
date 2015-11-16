package io.epopeia.authorization;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;

/**
 * Configuracao de acesso a base do backoffice usando o H2 em memoria para
 * testar o ddl e a montagem das queries pelo hibernate
 * 
 * @author Fernando Amaral
 */
@Configuration
@EnableJpaRepositories(
	basePackages = "io.epopeia.authorization.repository.backoffice",
	entityManagerFactoryRef = "emfBackoffice",
	transactionManagerRef = "txMgrBackoffice"
)
public class BackofficeH2Config {

	@Bean(name = "dsBackoffice")
	public DataSource dsBackoffice() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.H2).build();
	}

	@Bean(name = "emfBackoffice")
	public EntityManagerFactory emfBackoffice() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);

		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setPersistenceUnitName("backoffice");
		emf.setDataSource(dsBackoffice());
		emf.setPackagesToScan("io.epopeia.authorization.domain.backoffice");
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.afterPropertiesSet();
		return emf.getObject();
	}

	@Bean(name = "txMgrBackoffice")
	public PlatformTransactionManager txMgrBackoffice() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emfBackoffice());
		return transactionManager;
	}

	@Bean(name = "ddlValidator")
	public DDLValidator ddlValidator() {
		DDLValidator ddlValidator = new DDLValidator();
		ddlValidator.setDataSource(dsBackoffice());
		return ddlValidator;
	}

	@Bean(name = "dmlValidator")
	public DMLValidator dmlValidator() {
		DMLValidator dmlValidator = new DMLValidator();
		return dmlValidator;
	}

	/**
	 * Manter comentado pq nao sabemos se o Jenkins tera essas portas disponiveis.
	 * Utilize esses beans como ferramenta visual apenas durante o desenvolvimento.
	 * 
	@Bean(name = "h2WebServer", initMethod="start", destroyMethod="stop")
	public org.h2.tools.Server h2WebServer() throws SQLException {
	    return org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
	}

	@Bean(initMethod="start", destroyMethod="stop")
	@DependsOn(value = "h2WebServer")
	public org.h2.tools.Server h2Server() throws SQLException {
	    return org.h2.tools.Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
	}
	*/
}
