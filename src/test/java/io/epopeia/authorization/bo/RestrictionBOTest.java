package io.epopeia.authorization.bo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.epopeia.authorization.BusinessObjectConfig;
import io.epopeia.authorization.bo.RestrictionBO;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.faker.AllowMerchantFaker.EAllowMerchant;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Labels;
import io.epopeia.authorization.repository.backoffice.AllowChannelRepository;
import io.epopeia.authorization.repository.backoffice.AllowMerchantCnpjRepository;
import io.epopeia.authorization.repository.backoffice.AllowMerchantRepository;

/**
 * Teste do objeto de negocios RestrictionBO
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessObjectConfig.class })
public class RestrictionBOTest {

	@Autowired
	private AllowMerchantRepository restricoesMock;

	@Autowired
	private AllowMerchantCnpjRepository restricoesCnpjMock;

	@Autowired
	private AllowChannelRepository restricoesCanaisMock;

	@Autowired
	private RestrictionBO restrictionBO;

	@Before
	public void setUp() {
		Mockito.reset(restricoesMock);
		Mockito.reset(restricoesCnpjMock);
		Mockito.reset(restricoesCanaisMock);
	}

	@Test
	public void deveVerificarCartoesQueNaoPossuemCanalENemGrupoRestricao() {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		Labels tags = new Labels();

		imf.setLabels(EFields.TAGS, tags);
		imf.add(cardInfo);

		restrictionBO.check(imf);
		assertFalse("Restricao Estabelecimento foi consultada", tags.contains(ELabels.ALLOW_MERCHANT));
		assertFalse("Grupos Restricoes Canais foi consultada", tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL));
		assertFalse("Restricao Canais foi consultada", tags.contains(ELabels.ALLOW_CHANNEL));
	}

	@Test
	public void deveVerificarCartoesQueEstaoNumCanalSemGruposRestricao() {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		Labels tags = new Labels();

		imf.setLabels(EFields.TAGS, tags);
		imf.add(cardInfo);
		Long codigoCanal = EChannels.CanalUm.getCodigoCanal();
		cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, codigoCanal.toString());

		// specify mock behave when method called
		when(restricoesCanaisMock.countByCodigoCanal(codigoCanal)).thenReturn(0L);

		restrictionBO.check(imf);
		assertFalse("Restricao Estabelecimento foi consultada", tags.contains(ELabels.ALLOW_MERCHANT));
		assertFalse("Grupos Restricoes Canais encontrados", tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL));
		assertFalse("Restricao Canais foi consultada", tags.contains(ELabels.ALLOW_CHANNEL));
	}

	@Test
	public void deveVerificarCartoesQueEstaoNumCanalComGruposRestricaoSemPermissoes() {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		Labels tags = new Labels();

		imf.setLabels(EFields.TAGS, tags);
		imf.add(cardInfo);
		Long codigoCanal = EChannels.CanalUm.getCodigoCanal();
		cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, codigoCanal.toString());

		// specify mock behave when method called
		when(restricoesCanaisMock.countByCodigoCanal(codigoCanal)).thenReturn(1L);
		when(restricoesCanaisMock
			.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
				eq(codigoCanal), anyString(), anyString(), anyCollectionOf(String.class))).thenReturn(0L);
		when(restricoesCanaisMock.countByCodigoCanalAndCnpj(
				eq(codigoCanal), anyString())).thenReturn(0L);

		restrictionBO.check(imf);
		assertFalse("Restricao Estabelecimento foi consultada", tags.contains(ELabels.ALLOW_MERCHANT));
		assertTrue("Grupos Restricoes Canais encontrados", tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL));
		assertFalse("Restricao Canais foi consultada", tags.contains(ELabels.ALLOW_CHANNEL));
	}

	@Test
	public void deveVerificarCartoesQueEstaoNumCanalComGruposRestricaoComPermissaoPorEstabelecimento() {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		Labels tags = new Labels();

		imf.setLabels(EFields.TAGS, tags);
		imf.add(cardInfo);
		Long codigoCanal = EChannels.CanalUm.getCodigoCanal();
		cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, codigoCanal.toString());

		// specify mock behave when method called
		when(restricoesCanaisMock.countByCodigoCanal(codigoCanal)).thenReturn(1L);
		when(restricoesCanaisMock
			.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
				eq(codigoCanal), anyString(), anyString(), anyCollectionOf(String.class))).thenReturn(1L);
		when(restricoesCanaisMock.countByCodigoCanalAndCnpj(
				eq(codigoCanal), anyString())).thenReturn(0L);

		restrictionBO.check(imf);
		assertFalse("Restricao Estabelecimento foi consultada", tags.contains(ELabels.ALLOW_MERCHANT));
		assertTrue("Grupos Restricoes Canais encontrados", tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL));
		assertTrue("Permissoes para Grupo Canal encontrada", tags.contains(ELabels.ALLOW_CHANNEL));
	}

	@Test
	public void deveVerificarCartoesQueEstaoNumCanalComGruposRestricaoComPermissaoPorCnpj() {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		Labels tags = new Labels();

		imf.setLabels(EFields.TAGS, tags);
		imf.add(cardInfo);
		Long codigoCanal = EChannels.CanalUm.getCodigoCanal();
		cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, codigoCanal.toString());

		String cnpj = "12345678901234";
		imf.setValue(EFields.CPF_CNPJ, cnpj);

		// specify mock behave when method called
		when(restricoesCanaisMock.countByCodigoCanal(codigoCanal)).thenReturn(1L);
		when(restricoesCanaisMock
			.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
				eq(codigoCanal), anyString(), anyString(), anyCollectionOf(String.class))).thenReturn(0L);
		when(restricoesCanaisMock.countByCodigoCanalAndCnpj(
				eq(codigoCanal), eq(cnpj))).thenReturn(1L);

		restrictionBO.check(imf);
		assertFalse("Restricao Estabelecimento foi consultada", tags.contains(ELabels.ALLOW_MERCHANT));
		assertTrue("Grupos Restricoes Canais encontrados", tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL));
		assertTrue("Permissoes para Grupo Canal encontrada", tags.contains(ELabels.ALLOW_CHANNEL));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveVerificarCartoesQueEstaoNumGrupoRestricaoSemPermissoes() {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		Labels tags = new Labels();

		imf.setLabels(EFields.TAGS, tags);
		imf.add(cardInfo);
		Long codigoCanal = EChannels.CanalUm.getCodigoCanal();
		Long codigoGrupoRestricao = EAllowMerchant.MinhaCasaMelhorComPOSCielo.getCodigoGrupoRestricao();
		cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, codigoCanal.toString());
		cardInfo.setValue(ECardInfo.GROUP_RESTRICTION_ID, codigoGrupoRestricao.toString());

		// specify mock behave when method called
		when(restricoesCanaisMock.countByCodigoCanal(codigoCanal)).thenThrow(RuntimeException.class);
		when(restricoesMock
			.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
				eq(codigoGrupoRestricao), anyString(), anyString(), anyCollectionOf(String.class))).thenReturn(0L);
		when(restricoesCnpjMock.countByCodigoGrupoRestricaoAndCnpj(
				eq(codigoGrupoRestricao), anyString())).thenReturn(0L);

		restrictionBO.check(imf);
		assertFalse("Restricao Estabelecimento foi consultada", tags.contains(ELabels.ALLOW_MERCHANT));
		assertFalse("Grupos Restricoes Canais encontrados", tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL));
		assertFalse("Restricao Canais foi consultada", tags.contains(ELabels.ALLOW_CHANNEL));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveVerificarCartoesQueEstaoNumGrupoRestricaoComPermissaoParaEstabelecimento() {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		Labels tags = new Labels();

		imf.setLabels(EFields.TAGS, tags);
		imf.add(cardInfo);
		Long codigoCanal = EChannels.CanalUm.getCodigoCanal();
		Long codigoGrupoRestricao = EAllowMerchant.MinhaCasaMelhorComPOSCielo.getCodigoGrupoRestricao();
		cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, codigoCanal.toString());
		cardInfo.setValue(ECardInfo.GROUP_RESTRICTION_ID, codigoGrupoRestricao.toString());

		// specify mock behave when method called
		when(restricoesCanaisMock.countByCodigoCanal(codigoCanal)).thenThrow(RuntimeException.class);
		when(restricoesMock
			.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
				eq(codigoGrupoRestricao), anyString(), anyString(), anyCollectionOf(String.class))).thenReturn(1L);
		when(restricoesCnpjMock.countByCodigoGrupoRestricaoAndCnpj(
				eq(codigoGrupoRestricao), anyString())).thenReturn(0L);

		restrictionBO.check(imf);
		assertTrue("Restricao Estabelecimento nao foi consultada", tags.contains(ELabels.ALLOW_MERCHANT));
		assertFalse("Grupos Restricoes Canais encontrados", tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL));
		assertFalse("Restricao Canais foi consultada", tags.contains(ELabels.ALLOW_CHANNEL));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveVerificarCartoesQueEstaoNumGrupoRestricaoComPermissaoParaCnpj() {
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		Labels tags = new Labels();

		imf.setLabels(EFields.TAGS, tags);
		imf.add(cardInfo);
		Long codigoCanal = EChannels.CanalUm.getCodigoCanal();
		Long codigoGrupoRestricao = EAllowMerchant.MinhaCasaMelhorComPOSCielo.getCodigoGrupoRestricao();
		cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, codigoCanal.toString());
		cardInfo.setValue(ECardInfo.GROUP_RESTRICTION_ID, codigoGrupoRestricao.toString());

		String cnpj = "12345678901234";
		imf.setValue(EFields.CPF_CNPJ, cnpj);

		// specify mock behave when method called
		when(restricoesCanaisMock.countByCodigoCanal(codigoCanal)).thenThrow(RuntimeException.class);
		when(restricoesMock
			.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
				eq(codigoGrupoRestricao), anyString(), anyString(), anyCollectionOf(String.class))).thenReturn(0L);
		when(restricoesCnpjMock.countByCodigoGrupoRestricaoAndCnpj(
				eq(codigoGrupoRestricao), eq(cnpj))).thenReturn(1L);

		restrictionBO.check(imf);
		assertTrue("Restricao Cnpj nao foi consultada", tags.contains(ELabels.ALLOW_MERCHANT));
		assertFalse("Grupos Restricoes Canais encontrados", tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL));
		assertFalse("Restricao Canais foi consultada", tags.contains(ELabels.ALLOW_CHANNEL));
	}
}
