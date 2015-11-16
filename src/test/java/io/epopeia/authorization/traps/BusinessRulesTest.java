package io.epopeia.authorization.traps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.epopeia.authorization.TrapsConfig;
import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.bo.HsmBO;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.enums.EParams;
import io.epopeia.authorization.enums.ETipoTransacao;
import io.epopeia.authorization.enums.ETxnAttributes;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Labels;
import io.epopeia.authorization.traps.ValidaAtributosTransacao;
import io.epopeia.authorization.traps.ValidaBlacklistEstabelecimento;
import io.epopeia.authorization.traps.ValidaCartaoExpirado;
import io.epopeia.authorization.traps.ValidaCodigosDeSeguranca;
import io.epopeia.authorization.traps.ValidaExistenciaDoCartao;
import io.epopeia.authorization.traps.ValidaGrupoRestricao;
import io.epopeia.authorization.traps.ValidaSituacaoDaConta;
import io.epopeia.authorization.traps.ValidaSituacaoDoCartao;

/**
 * Teste das travas do framework bem como
 * a ordem que elas devem ser chamadas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TrapsConfig.class })
public class BusinessRulesTest {

	@Autowired(required = false)
	@Qualifier("businessRules")
	private List<AuthorizationTrap> businessRules;

	@Test
	public void deveTravarQuandoOsAtributosDaTransacaoNaoEstiveremCarregados() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaAtributosTransacao.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_ATRIBUTOS_TRANSACAO_INVALIDO.name());
	}

	@Test
	public void deveTravarQuandoOsDadosDeCartaoNaoEstiveremCarregados() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		imf.add(fsTxnAttr);
		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaExistenciaDoCartao.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_NUMERO_CARTAO_INVALIDO.name());
	}

	@Test
	public void deveTravarCartoesBloqueadosParaTransacoesQueExigemValidarStatusCartao() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "S");
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512");
		fsCardInfo.setValue(ECardInfo.CARD_STATUS, "B");
		fsCardInfo.setValue(ECardInfo.CARD_SITUATION, "BLOQUEADO_CARTAO_PERDIDO");
		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaSituacaoDoCartao.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_CARTAO_PERDIDO.name());
	}

	@Test
	public void devePermitirCartoesBloqueadosParaTransacoesQueNaoExigemValidarStatusCartao() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N");
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512");
		fsCardInfo.setValue(ECardInfo.CARD_STATUS, "B");
		fsCardInfo.setValue(ECardInfo.CARD_SITUATION, "BLOQUEADO_CARTAO_PERDIDO");
		validadeBusinessRulesAndAssertItWasNotTraped(imf, ValidaSituacaoDoCartao.class);
	}

	@Test
	public void devePermitirCartoesAtivosParaTransacoesQueExigemValidarStatusCartao() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "S");
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512");
		fsCardInfo.setValue(ECardInfo.CARD_STATUS, "A");
		validadeBusinessRulesAndAssertItWasNotTraped(imf, ValidaSituacaoDoCartao.class);
	}

	@Test
	public void deveTravarContasBloqueadasParaTransacoesQueExigemValidarStatusConta() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "S");
		fsCardInfo.setValue(ECardInfo.ACCOUNT_STATUS, "B");
		fsCardInfo.setValue(ECardInfo.ACCOUNT_SITUATION, "BLOQUEADA_CONTA_EM_COBRANCA");
		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaSituacaoDaConta.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_CONTA_EM_COBRANCA.name());
	}

	@Test
	public void devePermitirContasBloqueadasParaTransacoesQueNaoExigemValidarStatusConta() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N");
		fsCardInfo.setValue(ECardInfo.ACCOUNT_STATUS, "B");
		fsCardInfo.setValue(ECardInfo.ACCOUNT_SITUATION, "BLOQUEADA_CONTA_EM_COBRANCA");
		validadeBusinessRulesAndAssertItWasNotTraped(imf, ValidaSituacaoDaConta.class);
	}

	@Test
	public void devePermitirContasAtivasParaTransacoesQueExigemValidarStatusConta() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "S");
		fsCardInfo.setValue(ECardInfo.ACCOUNT_STATUS, "A");
		validadeBusinessRulesAndAssertItWasNotTraped(imf, ValidaSituacaoDaConta.class);
	}

	@Test
	public void deveTravarCartoesExpiradosParaTransacoesQueExigemValidarValidadeCartao() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "S");

		// -------- Expired Date --------- Today -----------> timeline
		SimpleDateFormat fExpDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1); // set one month before expire the card
		fsCardInfo.setValue(ECardInfo.CARD_EXPIRY_DATE, fExpDate.format(cal.getTime()));

		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaCartaoExpirado.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_CARTAO_EXPIRADO.name());
	}

	@Test
	public void devePermitirCartoesExpiradosParaTransacoesQueNaoExigemValidarValidadeCartao() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N");

		// -------- Expired Date --------- Today -----------> timeline
		SimpleDateFormat fExpDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1); // set one month before expire the card
		fsCardInfo.setValue(ECardInfo.CARD_EXPIRY_DATE, fExpDate.format(cal.getTime()));

		validadeBusinessRulesAndAssertItWasNotTraped(imf, ValidaCartaoExpirado.class);
	}

	@Test
	public void devePermitirCartoesDentroDoPeriodoDeExpiracaoParaTransacoesQueExigemValidarValidadeCartao() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "S");

		// -------- Today --------- Expired Date -----------> timeline
		SimpleDateFormat fExpDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		fsCardInfo.setValue(ECardInfo.CARD_EXPIRY_DATE, fExpDate.format(cal.getTime()));

		validadeBusinessRulesAndAssertItWasNotTraped(imf, ValidaCartaoExpirado.class);
	}

	@Test
	public void deveTravarICVVInvalidoParaCartoesChipCapturadosComoChip() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		FieldSet fsParamInfo = new FieldSet(EParams.PARAMETERS_INFO);
		Labels tags = new Labels();
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.add(fsParamInfo);
		imf.setLabels(EFields.TAGS, tags);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N"); //skip expire date trap
		fsParamInfo.setValue(EParams.VALIDAR_CHIP, "S"); // card issued with chip
		tags.add(ELabels.CHIP);
		tags.add(ELabels.CHIP_DATA);

		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaCodigosDeSeguranca.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_CODIGO_DE_SEGURANCA_CHIP_INVALIDO.name());
	}

	@Test
	public void deveTravarCVCInvalidoParaCartoesCapturadosComoMagnetico() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		FieldSet fsParamInfo = new FieldSet(EParams.PARAMETERS_INFO);
		Labels tags = new Labels();
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.add(fsParamInfo);
		imf.setLabels(EFields.TAGS, tags);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N"); //skip expire date trap
		fsParamInfo.setValue(EParams.VALIDAR_CHIP, "S"); // card issued with chip
		tags.add(ELabels.MAGNETIC); // but acquired as magnetic
		tags.add(ELabels.TRACK1_PRESENT);

		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaCodigosDeSeguranca.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_CODIGO_DE_SEGURANCA_INVALIDO.name());
	}

	@Test
	public void deveTravarCVC2InvalidoParaTodasTransacoesComColetaDeCVC2() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		FieldSet fsParamInfo = new FieldSet(EParams.PARAMETERS_INFO);
		Labels tags = new Labels();
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.add(fsParamInfo);
		imf.setLabels(EFields.TAGS, tags);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N"); //skip expire date trap
		fsParamInfo.setValue(EParams.VALIDAR_CHIP, "S"); // card issued with chip
		tags.add(ELabels.CHIP); // simulate chip
		tags.add(ELabels.CHIP_DATA);
		tags.add(ELabels.MAGNETIC); // simulate magnetic
		tags.add(ELabels.TRACK1_PRESENT);
		imf.setValue(EFields.RESP_ICVV, HsmBO.SECURITY_CODE_OK); // skip icvv trap validation
		imf.setValue(EFields.RESP_CVC, HsmBO.SECURITY_CODE_OK); // skip cvc trap validation
		tags.add(ELabels.CVC2);

		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaCodigosDeSeguranca.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_CODIGO_DE_SEGURANCA_2_INVALIDO.name());
	}

	@Test
	public void deveTravarCartoesEmGrupoRestricaoQueNaoPossuemPermissoesParaMccEEstabelecimentos() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		FieldSet fsParamInfo = new FieldSet(EParams.PARAMETERS_INFO);
		Labels tags = new Labels();
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.add(fsParamInfo);
		imf.setLabels(EFields.TAGS, tags);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N"); //skip expire date trap
		fsParamInfo.setValue(EParams.VALIDAR_CHIP, "S"); // card issued with chip
		tags.add(ELabels.CHIP); // simulate chip
		tags.add(ELabels.CHIP_DATA);
		tags.add(ELabels.MAGNETIC); // simulate magnetic
		tags.add(ELabels.TRACK1_PRESENT);
		tags.add(ELabels.CVC2);
		imf.setValue(EFields.RESP_ICVV, HsmBO.SECURITY_CODE_OK); // skip icvv trap validation
		imf.setValue(EFields.RESP_CVC, HsmBO.SECURITY_CODE_OK); // skip cvc trap validation
		imf.setValue(EFields.RESP_CVC2, HsmBO.SECURITY_CODE_OK); // skip cvc2 trap validation
		fsCardInfo.setValue(ECardInfo.GROUP_RESTRICTION_ID, "111"); //set a group restriction

		imf.setValue(EFields.TIPO_TRANSACAO_BACKOFFICE, ETipoTransacao.COMPRA_CREDITO_A_VISTA);
		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaGrupoRestricao.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_GRUPO_RESTRICAO.name());
	}

	@Test
	public void deveTravarCartoesEmGrupoRestricaoQueNaoPossuemPermissoesParaGruposRestricoesCanais() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		FieldSet fsParamInfo = new FieldSet(EParams.PARAMETERS_INFO);
		Labels tags = new Labels();
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.add(fsParamInfo);
		imf.setLabels(EFields.TAGS, tags);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N"); //skip expire date trap
		fsParamInfo.setValue(EParams.VALIDAR_CHIP, "S"); // card issued with chip
		tags.add(ELabels.CHIP); // simulate chip
		tags.add(ELabels.CHIP_DATA);
		tags.add(ELabels.MAGNETIC); // simulate magnetic
		tags.add(ELabels.TRACK1_PRESENT);
		tags.add(ELabels.CVC2);
		imf.setValue(EFields.RESP_ICVV, HsmBO.SECURITY_CODE_OK); // skip icvv trap validation
		imf.setValue(EFields.RESP_CVC, HsmBO.SECURITY_CODE_OK); // skip cvc trap validation
		imf.setValue(EFields.RESP_CVC2, HsmBO.SECURITY_CODE_OK); // skip cvc2 trap validation
		fsCardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, "1"); //set a channel id
		tags.add(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL); //set groups in this channel

		imf.setValue(EFields.TIPO_TRANSACAO_BACKOFFICE, ETipoTransacao.COMPRA_CREDITO_A_VISTA);
		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaGrupoRestricao.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_GRUPO_RESTRICAO_CANAL.name());
	}

	@Test
	public void devePermitirCartoesECanaisEmGruposDeRestricaoComPermissoes() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		FieldSet fsParamInfo = new FieldSet(EParams.PARAMETERS_INFO);
		Labels tags = new Labels();
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.add(fsParamInfo);
		imf.setLabels(EFields.TAGS, tags);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N"); //skip expire date trap
		fsParamInfo.setValue(EParams.VALIDAR_CHIP, "S"); // card issued with chip
		tags.add(ELabels.CHIP); // simulate chip
		tags.add(ELabels.CHIP_DATA);
		tags.add(ELabels.MAGNETIC); // simulate magnetic
		tags.add(ELabels.TRACK1_PRESENT);
		tags.add(ELabels.CVC2);
		imf.setValue(EFields.RESP_ICVV, HsmBO.SECURITY_CODE_OK); // skip icvv trap validation
		imf.setValue(EFields.RESP_CVC, HsmBO.SECURITY_CODE_OK); // skip cvc trap validation
		imf.setValue(EFields.RESP_CVC2, HsmBO.SECURITY_CODE_OK); // skip cvc2 trap validation
		fsCardInfo.setValue(ECardInfo.GROUP_RESTRICTION_ID, "111"); //set a group restriction
		fsCardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, "1"); //set a channel id
		tags.add(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL); //set groups in this channel
		tags.add(ELabels.ALLOW_MERCHANT); //set a permission to the group
		tags.add(ELabels.ALLOW_CHANNEL); //set a permission to the channel
		imf.setValue(EFields.TIPO_TRANSACAO_BACKOFFICE, ETipoTransacao.COMPRA_CREDITO_A_VISTA);

		validadeBusinessRulesAndAssertItWasNotTraped(imf, ValidaGrupoRestricao.class);
	}

	@Test
	public void deveTravarEstabelecimentosEmBlackList() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		FieldSet fsParamInfo = new FieldSet(EParams.PARAMETERS_INFO);
		Labels tags = new Labels();
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.add(fsParamInfo);
		imf.setLabels(EFields.TAGS, tags);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N"); //skip expire date trap
		fsParamInfo.setValue(EParams.VALIDAR_CHIP, "S"); // card issued with chip
		tags.add(ELabels.CHIP); // simulate chip
		tags.add(ELabels.CHIP_DATA);
		tags.add(ELabels.MAGNETIC); // simulate magnetic
		tags.add(ELabels.TRACK1_PRESENT);
		tags.add(ELabels.CVC2);
		imf.setValue(EFields.RESP_ICVV, HsmBO.SECURITY_CODE_OK); // skip icvv trap validation
		imf.setValue(EFields.RESP_CVC, HsmBO.SECURITY_CODE_OK); // skip cvc trap validation
		imf.setValue(EFields.RESP_CVC2, HsmBO.SECURITY_CODE_OK); // skip cvc2 trap validation
		tags.add(ELabels.MERCHANT_IN_BLACKLIST); //set to trap when a merchant is in blacklist

		validadeBusinessRulesAndAssertItWasTraped(imf, ValidaBlacklistEstabelecimento.class);
		Assert.assertEquals(imf.getValue(EFields.STATUS_TRANSACAO), ECodes.NEGADA_BLACKLIST_ESTABELECIMENTO.name());
	}

	@Test
	public void devePermitirEstabelecimentosQueNaoEstaoEmBlackList() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet fsTxnAttr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		FieldSet fsCardInfo = new FieldSet(ECardInfo.CARD_INFO);
		FieldSet fsParamInfo = new FieldSet(EParams.PARAMETERS_INFO);
		Labels tags = new Labels();
		imf.add(fsTxnAttr);
		imf.add(fsCardInfo);
		fsCardInfo.add(fsParamInfo);
		imf.setLabels(EFields.TAGS, tags);
		fsCardInfo.setValue(ECardInfo.CARD_ID, "30512"); //skip card exists
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, "N"); //skip card status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, "N"); //skip account status trap
		fsTxnAttr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, "N"); //skip expire date trap
		fsParamInfo.setValue(EParams.VALIDAR_CHIP, "S"); // card issued with chip
		tags.add(ELabels.CHIP); // simulate chip
		tags.add(ELabels.CHIP_DATA);
		tags.add(ELabels.MAGNETIC); // simulate magnetic
		tags.add(ELabels.TRACK1_PRESENT);
		tags.add(ELabels.CVC2);
		imf.setValue(EFields.RESP_ICVV, HsmBO.SECURITY_CODE_OK); // skip icvv trap validation
		imf.setValue(EFields.RESP_CVC, HsmBO.SECURITY_CODE_OK); // skip cvc trap validation
		imf.setValue(EFields.RESP_CVC2, HsmBO.SECURITY_CODE_OK); // skip cvc2 trap validation

		validadeBusinessRulesAndAssertItWasNotTraped(imf, ValidaBlacklistEstabelecimento.class);
	}

	private void validadeBusinessRulesAndAssertItWasTraped(FieldSet imf, Class<?> expectedTrapClass) throws Exception {
		String currentTrap = null;
		boolean trapReturn = false;
		for(AuthorizationTrap businessRule : businessRules) {
			currentTrap = businessRule.getClass().getCanonicalName();
			trapReturn = businessRule.validate(imf);
			if (trapReturn == true || currentTrap.equals(expectedTrapClass.getCanonicalName()))
				break;
		}
		Assert.assertEquals(expectedTrapClass.getCanonicalName(), currentTrap);
		Assert.assertTrue(trapReturn);
	}

	private void validadeBusinessRulesAndAssertItWasNotTraped(FieldSet imf, Class<?> expectedTrapClass) throws Exception {
		String currentTrap = null;
		boolean trapReturn = false;
		for(AuthorizationTrap businessRule : businessRules) {
			currentTrap = businessRule.getClass().getCanonicalName();
			trapReturn = businessRule.validate(imf);
			if (trapReturn == true || currentTrap.equals(expectedTrapClass.getCanonicalName()))
				break;
		}
		Assert.assertEquals(expectedTrapClass.getCanonicalName(), currentTrap);
		Assert.assertFalse(trapReturn);
	}
}
