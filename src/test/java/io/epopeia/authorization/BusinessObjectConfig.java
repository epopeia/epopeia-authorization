package io.epopeia.authorization;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import io.epopeia.authorization.beans.ParameterSearch;
import io.epopeia.authorization.bo.AutorizadoraBO;
import io.epopeia.authorization.bo.BackofficeBO;
import io.epopeia.authorization.repository.backoffice.AccountParameterRepository;
import io.epopeia.authorization.repository.backoffice.AllowChannelRepository;
import io.epopeia.authorization.repository.backoffice.AllowMerchantCnpjRepository;
import io.epopeia.authorization.repository.backoffice.AllowMerchantRepository;
import io.epopeia.authorization.repository.backoffice.BlacklistEstabelecimentoRepository;
import io.epopeia.authorization.repository.backoffice.CardRepository;
import io.epopeia.authorization.repository.backoffice.ChannelParameterRepository;
import io.epopeia.authorization.repository.backoffice.ModalityKeysRepository;
import io.epopeia.authorization.repository.backoffice.ModalityParameterRepository;
import io.epopeia.authorization.repository.backoffice.ParameterRepository;
import io.epopeia.authorization.repository.backoffice.ProceduresRepository;
import io.epopeia.authorization.repository.backoffice.ProductParameterRepository;
import io.epopeia.authorization.repository.backoffice.ProductRepository;
import io.epopeia.authorization.repository.backoffice.TransactionTypeRepository;
import io.epopeia.authorization.repository.backoffice.WhitelistEstabelecimentosRepository;

@Configuration
@ComponentScan(basePackages = "io.epopeia.authorization.bo",
	excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = {
		AutorizadoraBO.class, BackofficeBO.class }))
public class BusinessObjectConfig {

	@Bean
	public ProductRepository productRepository() {
		return mock(ProductRepository.class);
	}

	@Bean
	public ModalityKeysRepository modalityKeysRepository() {
		return mock(ModalityKeysRepository.class);
	}

	@Bean
	public CardRepository cardRepository() {
		return mock(CardRepository.class);
	}

	@Bean
	public TransactionTypeRepository transactionTypeRepository() {
		return mock(TransactionTypeRepository.class);
	}

	@Bean
	public ParameterRepository parameterRepository() {
		return mock(ParameterRepository.class);
	}

	@Bean
	public ProductParameterRepository productParameterRepository() {
		return mock(ProductParameterRepository.class);
	}

	@Bean
	public ModalityParameterRepository modalityParameterRepository() {
		return mock(ModalityParameterRepository.class);
	}

	@Bean
	public ChannelParameterRepository channelParameterRepository() {
		return mock(ChannelParameterRepository.class);
	}

	@Bean
	public ProceduresRepository proceduresRepository() {
		return mock(ProceduresRepository.class);
	}

	@Bean
	public AccountParameterRepository accountParameterRepository() {
		return mock(AccountParameterRepository.class);
	}

	@Bean
	public WhitelistEstabelecimentosRepository whitelistEstabelecimentosRepository() {
		return mock(WhitelistEstabelecimentosRepository.class);
	}

	@Bean
	public BlacklistEstabelecimentoRepository blacklistEstabelecimentosRepository() {
		return mock(BlacklistEstabelecimentoRepository.class);
	}

	@Bean
	public AllowMerchantRepository allowMerchantRepository() {
		return mock(AllowMerchantRepository.class);
	}

	@Bean
	public AllowMerchantCnpjRepository allowMerchantCnpjRepository() {
		return mock(AllowMerchantCnpjRepository.class);
	}

	@Bean
	public AllowChannelRepository allowChannelRepository() {
		return mock(AllowChannelRepository.class);
	}

	@Bean
	public ParameterSearch parameterSearch() {
		return new ParameterSearch();
	}
}
