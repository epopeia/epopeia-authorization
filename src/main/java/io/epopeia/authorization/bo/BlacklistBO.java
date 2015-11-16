package io.epopeia.authorization.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.domain.backoffice.BlacklistEstabelecimento;
import io.epopeia.authorization.repository.backoffice.BlacklistEstabelecimentoRepository;

/**
 * Objeto de negocio que define os estabelecimentos que possuem alguma
 * restricao em BlackList por motivos como suspeita de fraude.
 * 
 * @author Fernando Amaral
 */
@Service
public class BlacklistBO {

	private BlacklistEstabelecimentoRepository blacklistRepository;

	@Autowired
	public BlacklistBO(BlacklistEstabelecimentoRepository blacklistRepository) {
		this.blacklistRepository = blacklistRepository;
	}

	public boolean checkRestriction(String codigoAdquirente, String codigoEstabelecimento) {
		if (codigoAdquirente == null || codigoAdquirente.length() == 0 ||
			codigoEstabelecimento == null || codigoEstabelecimento.length() == 0)
			return false;
		String codigoAdquirenteTruncado = codigoAdquirente.length() > 6 ?
				codigoAdquirente.substring(0, 6) : codigoAdquirente;
		BlacklistEstabelecimento bl = blacklistRepository
			.findByCodigoAdquirenteAndCodigoEstabelecimento(codigoAdquirenteTruncado, codigoEstabelecimento);
		if (bl != null) {
			return true;
		}
		return false;
	}

}
