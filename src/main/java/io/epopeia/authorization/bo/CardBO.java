package io.epopeia.authorization.bo;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.domain.backoffice.AccountSituation;
import io.epopeia.authorization.domain.backoffice.Card;
import io.epopeia.authorization.domain.backoffice.CardSituation;
import io.epopeia.authorization.domain.backoffice.Channel;
import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.domain.backoffice.TitularAccount;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.EFuncaoCartao;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.CardRepository;

/**
 * Objeto de negocio que define os cartoes de clientes e seus relacionamentos
 * com a conta titular, modalidades etc.
 * 
 * As responsabilidades desse servico sao:
 * 
 * - Consulta de cartoes por numero criptografado
 * 
 * @author Fernando Amaral
 */
@Service
public class CardBO {

	private static final String EMPTY_STR = "";

	private CardRepository cartoes;

	@Autowired
	public CardBO(CardRepository cartoes) {
		this.cartoes = cartoes;
	}

	public FieldSet loadCardInfo(String numeroCriptografado) {
		Card c = cartoes.findByNumero(numeroCriptografado);
		return mapEntitiesToFieldSet(c);
	}

	private FieldSet mapEntitiesToFieldSet(final Card c) {
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);

		if (c != null) {
			cardInfo.setValue(ECardInfo.CARD_ID, c.getCodigoCartao().toString());
			cardInfo.setValue(ECardInfo.CARD_STATUS, c.getStatus().toString());

			CardSituation cs = c.getSituacao();
			cardInfo.setValue(ECardInfo.CARD_SITUATION,
					cs != null ? cs.getIdentificador() : EMPTY_STR);

			SimpleDateFormat fExpDate = new SimpleDateFormat("yyyyMMddHHmmss");
			cardInfo.setValue(ECardInfo.CARD_EXPIRY_DATE,
					fExpDate.format(c.getDataValidade().getTime()));

			Long groupRestrId = c.getCodigoGrupoRestricao();
			cardInfo.setValue(ECardInfo.GROUP_RESTRICTION_ID,
					groupRestrId != null ? groupRestrId.toString() : EMPTY_STR);

			Channel ch = c.getCanal();
			cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, ch != null ? ch
					.getCodigoCanal().toString() : EMPTY_STR);

			TitularAccount ct = c.getContaTitular();
			if (ct != null) {
				cardInfo.setValue(ECardInfo.ACCOUNT_ID, ct
						.getCodigoContaTitular().toString());
				cardInfo.setValue(ECardInfo.ACCOUNT_STATUS, ct.getStatus()
						.toString());

				AccountSituation cts = ct.getSituacao();
				cardInfo.setValue(ECardInfo.ACCOUNT_SITUATION,
						cts != null ? cts.getIdentificador() : EMPTY_STR);

				// TODO: Mover modalidade para cartoes
				Modality m = ct.getModalidade();
				if (m != null) {
					cardInfo.setValue(ECardInfo.MODALITY_ID, m
							.getCodigoModalidade().toString());

					cardInfo.setValue(ECardInfo.CARD_FUNCTION,
							EFuncaoCartao.getFuncaoCartao(m.getCodigoFuncaoCartao()
									.intValue()));

					Product p = m.getProduto();
					if (p != null) {
						cardInfo.setValue(ECardInfo.PRODUCT_ID, p
								.getCodigoProduto().toString());
						cardInfo.setValue(ECardInfo.BRAND_ID, p
								.getCodigoBandeira().toString());
					}
				}
			}
		}
		return cardInfo;
	}
}
