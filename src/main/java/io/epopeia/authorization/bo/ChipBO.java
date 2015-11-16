package io.epopeia.authorization.bo;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Objeto de negocio que age como a ponte entre os participantes do jpos e os
 * componentes do framework especificamente no contexto de chip, onde sao
 * definidos os metodos de negocio relacionados ao processamento de chip e as
 * validacoes necessarias com um servico de validacao emv externo.
 * 
 * @author Fernando Amaral
 */
@Service
public class ChipBO {

	@Value("${emv.host}")
	private String emvHost;

	@Value("${emv.port}")
	private String emvPort;

	@Value("${emv.connection.timeout}")
	private String emvConnTimeout;

	@Value("${emv.read.timeout}")
	private String emvReadTimeout;

	public String emvExtServiceSendCommand(final String command) throws Exception {
		Socket sock = null;
		byte[] b = new byte[1024];
		String res = null;

		try {
			// Conectar
			sock = new Socket();
			sock.connect(new InetSocketAddress(emvHost, new Integer(emvPort)),
					new Integer(emvConnTimeout));
			sock.setTcpNoDelay(true);
			sock.setSoTimeout(new Integer(emvReadTimeout));

			// Enviar comando
			sock.getOutputStream().write(command.getBytes());

			// Receber comando
			sock.getInputStream().read(b);
			res = new String(b);
			return res;
		} catch (Exception e) {
			throw new Exception("Erro Comunicacao EMV Service :: " + e.toString());
		} finally {
			try {
				// Encerra o socket cliente
				if (sock != null && !sock.isClosed())
					sock.close();
			} catch (Exception e) {
				throw new Exception("Erro fechando socket com EMV Service :: " + e.toString());
			}
		}
	}
}
