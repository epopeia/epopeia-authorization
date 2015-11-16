package io.epopeia.authorization.domain.autorizadora;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Definicao da entidade MessageIsoIn e os planos de execucao
 * das querys com outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="MESSAGE_ISO_IN")
public class IsoMessageIn {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_MESSAGE_ISO_IN")
	private Long id;
		
	@Column(name="MESSAGE_TYPE", length=4)
	private String messageType;
	
	@Column(name="DATE", nullable=false)
	@Temporal(TemporalType.DATE)
	private Calendar date;
	
	@Column(name="DATETIME", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar datetime;
	
	@Column(name="MESSAGE_XML", length=8000, nullable=false)
	private String messageXML;

	@Column(name="ID_ORIGEN_TRANSACTION")
	private Long idOrigenTransaction;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Calendar getDatetime() {
		return datetime;
	}

	public void setDatetime(Calendar datetime) {
		this.datetime = datetime;
	}

	public String getMessageXML() {
		return messageXML;
	}

	public void setMessageXML(String messageXML) {
		this.messageXML = messageXML;
	}

	public Long getIdOrigenTransaction() {
		return idOrigenTransaction;
	}

	public void setIdOrigenTransaction(Long idOrigenTransaction) {
		this.idOrigenTransaction = idOrigenTransaction;
	}

}
