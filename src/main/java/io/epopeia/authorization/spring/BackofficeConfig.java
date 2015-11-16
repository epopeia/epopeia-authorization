package io.epopeia.authorization.spring;

import java.beans.PropertyVetoException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Configuracao de acesso a base do backoffice
 * 
 * @author Fernando Amaral
 */
@Configuration
@EnableJpaRepositories(
	basePackages = "io.epopeia.authorization.repository.backoffice",
	entityManagerFactoryRef = "emfBackoffice",
	transactionManagerRef = "txMgrBackoffice")
@EnableTransactionManagement
public class BackofficeConfig {

	@Value("${backoffice.driver:}")
	private String backofficeDriver;

	@Value("${backoffice.user:}")
	private String backofficeUser;

	@Value("${backoffice.password:}")
	private String backofficePassword;

	@Value("${backoffice.url:}")
	private String backofficeUrl;

	@Value("${c3p0.minsize:}")
	private Integer c3p0Minsize;

	@Value("${c3p0.maxsize:}")
	private Integer c3p0Maxsize;

	@Value("${c3p0.maxidletime:}")
	private Integer c3p0Maxidletime;

	@Value("${c3p0.acquireincrement:}")
	private Integer c3p0Acquireincrement;

	@Value("${c3p0.acquireretryattempts:}")
	private Integer c3p0Acquireretryattempts;

	@Value("${c3p0.initialsize:}")
	private Integer c3p0Initialsize;

	@Value("${c3p0.maxstatements:}")
	private Integer c3p0Maxstatements;

	@Value("${c3p0.idleconntestperiod:}")
	private Integer c3p0Idleconntestperiod;

	@Value("${c3p0.validate:}")
	private Boolean c3p0Validate;

	@Value("${vpjpa.database:}")
	private String vpjpaDatabase;

	@Value("${vpjpa.platform:}")
	private String vpjpaPlatform;

	@Value("${vpjpa.generateddl:}")
	private Boolean vpjpaGenerateddl;

	@Value("${vpjpa.showsql:}")
	private Boolean vpjpaShowsql;

	@Bean(name = "dsBackoffice", destroyMethod = "close")
	public DataSource dsBackoffice() {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		try { ds.setDriverClass(backofficeDriver); } catch (PropertyVetoException e) { e.printStackTrace(); }
		ds.setUser(backofficeUser);
		ds.setPassword(backofficePassword);
		ds.setJdbcUrl(backofficeUrl);
		ds.setMinPoolSize(c3p0Minsize);
		ds.setMaxPoolSize(c3p0Maxsize);
		ds.setMaxIdleTime(c3p0Maxidletime);
		ds.setAcquireIncrement(c3p0Acquireincrement);
		ds.setAcquireRetryAttempts(c3p0Acquireretryattempts);
		ds.setInitialPoolSize(c3p0Initialsize);
		ds.setMaxStatements(c3p0Maxstatements);
		ds.setIdleConnectionTestPeriod(c3p0Idleconntestperiod);
		ds.setTestConnectionOnCheckout(c3p0Validate);
		return ds;
	}

	@Bean(name = "emfBackoffice")
	public EntityManagerFactory emfBackoffice() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.valueOf(vpjpaDatabase));
		vendorAdapter.setDatabasePlatform(vpjpaPlatform);
		vendorAdapter.setGenerateDdl(vpjpaGenerateddl);
		vendorAdapter.setShowSql(vpjpaShowsql);

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
}
