package io.epopeia.authorization.builder;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import io.epopeia.authorization.domain.backoffice.Card;
import io.epopeia.authorization.domain.backoffice.Channel;
import io.epopeia.authorization.domain.backoffice.ChannelParameter;
import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.domain.backoffice.ModalityParameter;
import io.epopeia.authorization.domain.backoffice.Parameter;
import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.domain.backoffice.ProductParameter;
import io.epopeia.authorization.domain.backoffice.SystemParameter;
import io.epopeia.authorization.domain.backoffice.TitularAccount;
import io.epopeia.authorization.domain.backoffice.TitularAccountParameter;
import io.epopeia.authorization.faker.AccountFaker;
import io.epopeia.authorization.faker.CardFaker;
import io.epopeia.authorization.faker.ChannelFaker;
import io.epopeia.authorization.faker.ModalityFaker;
import io.epopeia.authorization.faker.ParameterFaker;
import io.epopeia.authorization.faker.ProductFaker;
import io.epopeia.authorization.faker.AccountFaker.EAccounts;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ParameterFaker.EParamsFaker;
import io.epopeia.authorization.faker.ProductFaker.EProduct;

/**
 * Builder de objetos Parameter e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
public class ParameterBuilder {

	private Parameter parametro;
	private Set<TitularAccountParameter> ctps = null;
	private Set<ChannelParameter> chps = null;
	private Set<ModalityParameter> mps = null;
	private Set<ProductParameter> pps = null;

	public ParameterBuilder() {
	}

	public ParameterBuilder parameter(EParamsFaker eParamsFaker) {
		this.parametro = ParameterFaker.getParameter(eParamsFaker);
		this.ctps = new HashSet<TitularAccountParameter>();
		this.chps = new HashSet<ChannelParameter>();
		this.mps = new HashSet<ModalityParameter>();
		this.pps = new HashSet<ProductParameter>();
		this.parametro.setParametrosProduto(pps);
		this.parametro.setParametrosModalidade(mps);
		this.parametro.setParametrosContaTitular(ctps);
		this.parametro.setParametrosCanais(chps);
		return this;
	}

	public ParameterBuilder withSystemValue(String value) {
		SystemParameter sp = new SystemParameter();
		sp.setCodigoParametro(parametro.getCodigoParametro());
		sp.setParametro(parametro);
		sp.setValor(value);
		parametro.setParametroSistema(sp);
		return this;
	}

	public ParameterBuilder withChannelValue(EChannels chf, String value, Calendar dtExpiracao) {
		ChannelParameter chp = new ChannelParameter();
		chp.setCodigoParametro(parametro.getCodigoParametro());
		chp.setParametro(parametro);
		Channel ch = ChannelFaker.getCardChannels(chf);
		Card c = CardFaker.getCard();
		ch.setCartao(c);
		ch.setCodigoCartao(c.getCodigoCartao());
		chp.setCodigoCanal(ch.getCodigoCanal());
		chp.setDataExpiracao(dtExpiracao);
		chp.setValor(value);
		chps.add(chp);
		return this;
	}

	public ParameterBuilder withChannelValue(EChannels chf, String value) {
		return withChannelValue(chf, value, null);
	}

	public ParameterBuilder withAccountValue(EAccounts ctf, String value, Calendar dtExpiracao) {
		TitularAccountParameter ctp = new TitularAccountParameter();
		ctp.setCodigoParametro(parametro.getCodigoParametro());
		ctp.setParametro(parametro);
		TitularAccount ct = AccountFaker.getAccount(ctf);
		ctp.setContaTitular(ct);
		ctp.setCodigoContaTitular(ct.getCodigoContaTitular());
		ctp.setDataExpiracao(dtExpiracao);
		ctp.setValor(value);
		ctps.add(ctp);
		return this;
	}

	public ParameterBuilder withAccountValue(EAccounts ctf, String value) {
		return withAccountValue(ctf, value, null);
	}

	public ParameterBuilder withModalityValue(EModality mf, String value) {
		ModalityParameter mp = new ModalityParameter();
		mp.setCodigoParametro(parametro.getCodigoParametro());
		mp.setParametro(parametro);
		Modality m = ModalityFaker.getModality(mf);
		mp.setModalidade(m);
		mp.setCodigoModalidade(m.getCodigoModalidade());
		mp.setValor(value);
		mps.add(mp);
		return this;
	}

	public ParameterBuilder withProductValue(EProduct pf,String value) {
		ProductParameter pp = new ProductParameter();
		pp.setCodigoParametro(parametro.getCodigoParametro());
		pp.setParametro(parametro);
		Product p = ProductFaker.getProduct();
		pp.setProduto(p);
		pp.setCodigoProduto(p.getCodigoProduto());
		pp.setValor(value);
		pps.add(pp);
		return this;
	}

	public Parameter create() {
		return parametro;
	}
}
