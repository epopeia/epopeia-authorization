package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandlerSelector;
import io.epopeia.authorization.api.MessageClassifier;

/**
 * Implementacao do Switch do jpos responsavel por selecionar o grupo
 * de participantes que o transaction manager ira processar. A classificacao
 * da mensagem eh feita delegando para o gateway atraves da API informar
 * o tipo de mensagem que estamos tratando.
 * 
 * @author Fernando Amaral
 */
@Service
public class Switch extends ChainHandlerSelector {

	@Autowired(required=false)
	private MessageClassifier<?> messageClassifier;

	@Override
	public String select(Context context) throws Exception {
		Enum<?> tipoTransacao = messageClassifier.classify(context);

		String groups = configuration().get(tipoTransacao.name(), null);

		if(groups == null)
			throw new Exception("Received a message with MTI:" +
				request(context).getMTI() + " that isn't handled by any group");

		return groups;
	}
}
