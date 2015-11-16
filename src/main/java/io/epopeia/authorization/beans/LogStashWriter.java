package io.epopeia.authorization.beans;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jpos.q2.QBeanSupport;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionManager;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.epopeia.authorization.bo.ProductBO;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.participant.LogStash;
import io.epopeia.authorization.spring.SpringContext;
import io.epopeia.jpos.JPOSContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Bean que consome a fila do logstash e transforma a mensagem
 * em um json para ser gravado num arquivo de monitoracao.
 * 
 * @author Fernando Amaral
 */
public class LogStashWriter extends QBeanSupport implements Runnable {

	private static final Logger logStashFile = LoggerFactory.getLogger(LogStash.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private ProductBO productBO = SpringContext.getSpringContainer().getBean(ProductBO.class);

	private TransactionManager txnmgr;

	@SuppressWarnings("unchecked")
	Space<String, Context> sp = SpaceFactory.getSpace("tspace:default");

	public LogStashWriter() {
		super();
		txnmgr = null;
	}

	public void startService() {
		new Thread(this).start();
	}

	public void run() {
		this.setName("LogStashWriter");
		Thread.currentThread().setName("LogStashWriter");

		try {
			txnmgr = (TransactionManager) NameRegistrar.get ("txnmgr");
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		for(;;) {
			Context ctx = sp.in(LogStash.LOGSTASH_QUEUE);

			FieldSet imf = JPOSContext.getInternalMessageFormat(ctx);

			Map<String, Object> objToJson = new LinkedHashMap<String, Object>();

			objToJson.put("id", imf.getValueAsLong(EFields.AUTORIZADORA_NSU));
			objToJson.put("data", dateFormat.format(new Date()));
			objToJson.put("type", imf.getValue(EFields.TIPO_TRANSACAO_AUTORIZADORA));
			objToJson.put("amount", imf.getValueAsBigDecimal(EFields.AMOUNT_AUTHORIZATION, 100, 2, true));
			objToJson.put("queued_ms", getQueuedMs(imf,ctx));
			objToJson.put("emvService_ms", getEmvServiceMs(imf,ctx));
			objToJson.put("elapsed_ms", getElapsedMs(imf,ctx));
			objToJson.put("instance", getHostname());
			objToJson.put("tps", txnmgr != null ? txnmgr.getTPS().intValue() : -1L);

			Long brandId = productBO.getBrandId(imf.getValue(EFields.BIN));
			brandId = brandId == null ? -1 : brandId;
			objToJson.put("brand", brandId);

			/**
			 * Estamos considerando na monitoracao como produto o bin id
			 * ao inves da modalidade por questoes de agrupamento de todos
			 * os bins de uma modalidade.
			 */
			Long productId = productBO.getProductId(imf.getValue(EFields.BIN));
			productId = productId == null ? -1 : productId;
			objToJson.put("product", productId);

			objToJson.put("merchant_cep", imf.getValue(EFields.MERCHANT_CEP));

			Enum<?> situacao = imf.getEnum(EFields.STATUS_TRANSACAO, ECodes.class);
			if(situacao.equals(ECodes.AUTORIZADA))
				objToJson.put("status", "approved");
			else if(situacao.equals(ECodes.AUTORIZADA_PARCIALMENTE)) {
				objToJson.put("status", "parcial_approved");
				objToJson.put("original_amount", imf.getValueAsBigDecimal(EFields.AMOUNT_CARDHOLDER, 100, 2, true));
			}
			else {
				objToJson.put("status", "denied");
				objToJson.put("reason", situacao.name());
			}

			/* Get labels assigned in the parser process */
			try {
				String[] tags = imf.getLabels(EFields.TAGS).getLabelsAsArray();
				if(tags.length > 0)
					objToJson.put("tags", tags);
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* Convert it into a json format and write it */
			try {
				ObjectMapper mapper =  new ObjectMapper();
				String s = mapper.writeValueAsString(objToJson);
				logStashFile.info(s + "\n");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	private String getHostname() {
		String hostname = "Unknown";
		try {
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		}
		catch (UnknownHostException ex) {
		    System.err.println("Hostname can not be resolved");
		}
		return hostname;
	}

	private Long getQueuedMs(FieldSet imf, Context ctx) {
		Long listenerIn = JPOSContext.getTimeMillisListenerIn(ctx);
		Long firstParticipant = JPOSContext.getTimeMillisFirstParticipant(ctx);
		if(listenerIn == null) listenerIn = (long) 0;
		if(firstParticipant == null) firstParticipant = (long) 0;
		return firstParticipant >= listenerIn ? firstParticipant - listenerIn : 0;
	}

	private Long getEmvServiceMs(FieldSet imf, Context ctx) {
		Long emvServiceSend = JPOSContext.getTimeMillisEmvServiceSend(ctx);
		Long emvServiceReceive = JPOSContext.getTimeMillisEmvServiceReceive(ctx);
		if(emvServiceSend == null) emvServiceSend = (long) 0;
		if(emvServiceReceive == null) emvServiceReceive = (long) 0;
		return emvServiceReceive >= emvServiceSend ? emvServiceReceive - emvServiceSend : 0;
	}

	private Long getElapsedMs(FieldSet imf, Context ctx) {
		Long firstParticipant = JPOSContext.getTimeMillisFirstParticipant(ctx);
		Long lastParticipant = JPOSContext.getTimeMillisLastParticipant(ctx);
		if(firstParticipant == null) firstParticipant = (long) 0;
		if(lastParticipant == null) lastParticipant = (long) 0;
		return lastParticipant >= firstParticipant ? lastParticipant - firstParticipant : 0;
	}
}

