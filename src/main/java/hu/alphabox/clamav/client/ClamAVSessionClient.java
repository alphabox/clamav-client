package hu.alphabox.clamav.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import hu.alphabox.clamav.client.command.SessionCommand;

public class ClamAVSessionClient extends AbstractClamavSessionClient {

	public ClamAVSessionClient(String host, int port) throws IOException {
		super(InetAddress.getByName(host), port);
	}

	public String sendCommand(SessionCommand command) throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStream inputStream = this.getInputStream();
		OutputStream outputStream = this.getOutputStream();
		outputStream.write(command.getRequestByteArray(ClamAVSeparator.NULL).toByteArray());
		outputStream.flush();

		int opByte = -1;
		do {
			opByte = inputStream.read();
			builder.append((char) opByte);
		} while (opByte != -1 && opByte != '\0');

		return builder.toString();
	}

}
