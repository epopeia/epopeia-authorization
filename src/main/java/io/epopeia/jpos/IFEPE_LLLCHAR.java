package io.epopeia.jpos;

import org.jpos.iso.EbcdicInterpreter;
import org.jpos.iso.EbcdicPrefixer;
import org.jpos.iso.ISOTagStringFieldPackager;
import org.jpos.iso.NullPadder;

/**
 * A versao do jpos nao fornece um tipo para parse de campos LLLCHAR.
 * Essa eh uma implementacao para ser utilizado pelo DE112 da Mastercard
 * ate que o framework jpos forneca esse tipo nativamente.
 * 
 * @author Fernando Amaral
 */
public class IFEPE_LLLCHAR extends ISOTagStringFieldPackager {
    public IFEPE_LLLCHAR() {
        super(0, null, EbcdicPrefixer.LLL, NullPadder.INSTANCE,
                EbcdicInterpreter.INSTANCE, EbcdicPrefixer.LLL);
    }

    /**
     * @param len
     *            - field len
     * @param description
     *            symbolic descrption
     */
    public IFEPE_LLLCHAR(int len, String description) {
        super(len, description, EbcdicPrefixer.LLL, NullPadder.INSTANCE,
                EbcdicInterpreter.INSTANCE, EbcdicPrefixer.LLL);
    }

}
