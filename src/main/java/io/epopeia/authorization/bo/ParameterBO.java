package io.epopeia.authorization.bo;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.AuthorizationParameter;
import io.epopeia.authorization.domain.backoffice.Parameter;
import io.epopeia.authorization.domain.backoffice.SystemParameter;
import io.epopeia.authorization.repository.backoffice.AccountParameterRepository;
import io.epopeia.authorization.repository.backoffice.ChannelParameterRepository;
import io.epopeia.authorization.repository.backoffice.ModalityParameterRepository;
import io.epopeia.authorization.repository.backoffice.ParameterRepository;
import io.epopeia.authorization.repository.backoffice.ProductParameterRepository;

/**
 * Objeto de negocio que busca os parametros do sistema e carrega um fieldset
 * com os valores escolhidos com base na prioridade das entidades contas,
 * canais, modalidades, produtos e como fallback os parametros de sistema.
 * 
 * @author Fernando Amaral
 */
@Service
public class ParameterBO {

	private ParameterRepository parametros;
	private ProductParameterRepository parametrosProdutos;
	private ModalityParameterRepository parametrosModalidades;
	private ChannelParameterRepository parametrosCanais;
	private AccountParameterRepository parametrosContas;

	@Autowired
	public ParameterBO(ParameterRepository parametros,
			ProductParameterRepository parametrosProdutos,
			ModalityParameterRepository parametrosModalidades,
			ChannelParameterRepository parametrosCanais,
			AccountParameterRepository parametrosContas) {
		this.parametros = parametros;
		this.parametrosProdutos = parametrosProdutos;
		this.parametrosModalidades = parametrosModalidades;
		this.parametrosCanais = parametrosCanais;
		this.parametrosContas = parametrosContas;
	}

	@Cacheable(value = "parametrosSistemas", condition = "(#identificador?:'').length() > 0", unless = "#result == null")
	public String getSystemParameterValue(String identificador) {
		Parameter p = parametros.findByIdentificador(identificador);
		if (p != null) {
			SystemParameter sp = p.getParametroSistema();
			return sp != null ? sp.getValor() : null;
		}
		return null;
	}

	@Cacheable(value = "parametrosProdutos", unless = "#result == null")
	public Map<String, String> getAllProductParameters(Long codigoProduto) {
		return codigoProduto == null ? null
				: convertResultSetIntoMap(parametrosProdutos
						.findByCodigoProduto(codigoProduto));
	}

	@Cacheable(value = "parametrosModalidades", unless = "#result == null")
	public Map<String, String> getAllModalityParameters(Long codigoModalidade) {
		return codigoModalidade == null ? null
				: convertResultSetIntoMap(parametrosModalidades
						.findByCodigoModalidade(codigoModalidade));
	}

	@Cacheable(value = "parametrosCanais", unless = "#result == null")
	public Map<String, String> getAllChannelParameters(Long codigoCanal) {
		return codigoCanal == null ? null
				: convertResultSetIntoMap(parametrosCanais
						.findByCodigoCanal(codigoCanal));
	}

	@Cacheable(value = "parametrosContas", unless = "#result == null")
	public Map<String, String> getAllAccountParameters(Long codigoContaTitular) {
		return codigoContaTitular == null ? null
				: convertResultSetIntoMap(parametrosContas
						.findByCodigoContaTitular(codigoContaTitular));
	}

	private Map<String, String> convertResultSetIntoMap(List<AuthorizationParameter> resultSet) {
		final Optional<List<AuthorizationParameter>> oResultSet = Optional.ofNullable(resultSet);
		return oResultSet.orElse(null).stream()
			.filter(kp -> Optional.ofNullable(kp.getDataExpiracao()).get().compareTo(Calendar.getInstance()) >= 0)
			.collect(Collectors.toMap(
				kp -> Optional.ofNullable(kp.getParametro()).orElseGet(Parameter::new).getIdentificador(),
				AuthorizationParameter::getValor)
			);
	}
}
