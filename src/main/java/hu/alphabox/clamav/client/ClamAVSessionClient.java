package hu.alphabox.clamav.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import hu.alphabox.clamav.client.command.SessionCommand;

/**
 * <p>
 * It is a synchronized blocking session client for ClamAV.
 * You can send {@code SessionCommand} commands, and you will block
 * while you do not get the response.
 * </p>
 * <p>
 * It is only use one connection for the whole session.
 * </p>
 * 
 * @author Daniel Mecsei
 *
 */
public class ClamAVSessionClient extends AbstractClamavSessionClient<String> {

	public ClamAVSessionClient(String host, int port) throws IOException {
		super(InetAddress.getByName(host), port);
	}

	@Override
	public synchronized String sendCommand(SessionCommand command) throws IOException, ClamAVException {
		StringBuilder builder = new StringBuilder();
		InputStream inputStream = this.getInputStream();
		OutputStream outputStream = this.getOutputStream();
		
		command.getRequestByteArray(ClamAVSeparator.NULL).writeTo(outputStream);
		outputStream.flush();

		int opByte = -1;
		do {
			opByte = inputStream.read();
			builder.append((char) opByte);
		} while (opByte != -1 && opByte != '\0');
		
		if(builder.toString().endsWith("ERROR")) {
			throw new ClamAVException(builder.toString());
		}

		return builder.toString();
	}

}
