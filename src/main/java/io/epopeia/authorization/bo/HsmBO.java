package io.epopeia.authorization.bo;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.domain.backoffice.Key;
import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.domain.backoffice.ModalityKey;
import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.ModalityKeysRepository;
import io.epopeia.authorization.repository.backoffice.ProceduresRepository;
import io.epopeia.authorization.repository.backoffice.ProductRepository;

/**
 * Objeto de negocio responsavel por criptografar/decriptografar dados
 * utilizando chaves especificas ou chaves associadas com a
 * modalidade/modalidadechaves/chaves.
 * 
 * @author Fernando Amaral
 */
@Service
public class HsmBO {

	private ProductRepository produtos;
	private ModalityKeysRepository modalidadeChaves;
	private ProceduresRepository procedures;

	public static final String SECURITY_CODE_OK = "OK";
	public static final String SECURITY_CODE_NOK = "NOK";
	public static final String SECURITY_CODE_FAIL = "FAIL";

	@Value("${hsm.host}")
	private String host;

	@Value("${hsm.port}")
	private String port;

	@Autowired
	public HsmBO(ProductRepository produtos,
			ModalityKeysRepository modalidadeChaves,
			ProceduresRepository procedures) {
		this.produtos = produtos;
		this.modalidadeChaves = modalidadeChaves;
		this.procedures = procedures;
	}

	/**
	 * Since a product has many modalities, lets return the first object in the
	 * set just to get any modality Id of this product.
	 * TODO: Refatorar para utilizar Optionals do java 8
	 */
	@Cacheable(value = "chaves", condition = "(#bin?:'').length() == 6 and (#keyType?:'').length() == 3", unless = "#result == null")
	public String getKey(String bin, String keyType) {
		if (bin == null || bin.length() != 6 || keyType == null
				|| keyType.length() != 3)
			return null;
		Product p = produtos.findByBin(bin);
		if (p != null) {
			Modality m = p.getModalidades().iterator().next();
			if (m != null) {
				Set<ModalityKey> mks = modalidadeChaves.findByModalidade(m);
				if (mks != null) {
					for (ModalityKey mk : mks) {
						Key k = mk.getChave();
						if (k != null && k.getCodigoTipoChave().equals(keyType)
								&& k.getAtiva().equals('S'))
							return k.getChave();
					}
				}
			}
		}
		return null;
	}

	public void validateSecurityCodes(FieldSet imf) throws Exception {
		String retorno = procedures.cvv2(imf);

		if(retorno != null && retorno.length() >= 3) {
			char respCVC = retorno.charAt(0);
			if (respCVC == '0')
				imf.setValue(EFields.RESP_CVC, SECURITY_CODE_OK);
			else if (respCVC == '1')
				imf.setValue(EFields.RESP_CVC, SECURITY_CODE_NOK);
			else
				imf.setValue(EFields.RESP_CVC, SECURITY_CODE_FAIL);

			char respCVC2 = retorno.charAt(1);
			if (respCVC2 == '0')
				imf.setValue(EFields.RESP_CVC2, SECURITY_CODE_OK);
			else if (respCVC2 == '1')
				imf.setValue(EFields.RESP_CVC2, SECURITY_CODE_NOK);
			else
				imf.setValue(EFields.RESP_CVC2, SECURITY_CODE_FAIL);

			char respICVV = retorno.charAt(2);
			if (respICVV == '0')
				imf.setValue(EFields.RESP_ICVV, SECURITY_CODE_OK);
			else if (respICVV == '1')
				imf.setValue(EFields.RESP_ICVV, SECURITY_CODE_NOK);
			else
				imf.setValue(EFields.RESP_ICVV, SECURITY_CODE_FAIL);
		}
		else {
			throw new Exception("Erro no retorno da procedure CVV2");
		}
	}

	public void validatePinBlock(FieldSet imf) {
		procedures.verificaPINBlockCriptografado(imf);
	}

	public String encryptPan(String pan, String dek) throws Exception {
		return "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
	}
}
