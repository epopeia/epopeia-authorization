package io.epopeia.authorization.bo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.domain.backoffice.WhitelistEstabelecimento;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.repository.backoffice.WhitelistEstabelecimentosRepository;

/**
 * Objeto de negocio que define os estabelecimentos que possuem alguma
 * permissão em WhiteList.
 * 
 * A unica permissao que existe hoje sao para estabelecimentos que nao coletam CVC2.
 * 
 * Exemplos:
 * lojas ecommerce como (MAGAZINE LUIZA COM, AMERICANAS COM, SUBMARINO COM, SHOPTIME.COM)
 * que utilizam terminais com capacidade de ler o CVC2, enviam a transacao como digitada,
 * porem nao coletam o CVC2. Nesses casos deixamos esses estabelecimentos em whitelist
 * para skipar a validacao do CVC2. Abaixo os casos encontrados desses estabelecimentos:
 * 
 * <field id="61" value="0000000000800076 : DE22 011
 * <field id="61" value="0001100000600076 : DE22 011
 * <field id="61" value="0001100000800076 : DE22 011
 * 
 * @author Fernando Amaral
 */
@Service
public class WhitelistBO {

	private WhitelistEstabelecimentosRepository whitelistRepository;

	@Autowired
	public WhitelistBO(WhitelistEstabelecimentosRepository whitelistRepository) {
		this.whitelistRepository = whitelistRepository;
	}

	@Cacheable(value = "whitelist", condition = "(#codigoAdquirente?:'').length() > 0 and (#codigoEstabelecimento?:'').length() > 0", unless = "#result == null")
	public Map<String, String> getPermissions(String codigoAdquirente, String codigoEstabelecimento) {
		if (codigoAdquirente == null || codigoAdquirente.length() == 0 ||
			codigoEstabelecimento == null || codigoEstabelecimento.length() == 0)
			return null;
		WhitelistEstabelecimento wl = whitelistRepository
			.findByCodigoAdquirenteAndCodigoEstabelecimento(codigoAdquirente, codigoEstabelecimento);
		if (wl != null) {
			Map<String, String> permissions = new HashMap<String, String>();
			permissions.put(ELabels.WHITELIST_ALLOW_EMPTY_CVC2.name(),
					wl.getCvc2ausente() != null && wl.getCvc2ausente() == 'S' ? "S" : "N");
			return permissions;
		}
		return null;
	}

}
