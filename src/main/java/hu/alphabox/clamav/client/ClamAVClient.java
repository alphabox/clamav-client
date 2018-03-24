package hu.alphabox.clamav.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import hu.alphabox.clamav.client.command.Command;

public class ClamAVClient {

	private String host;

	private int port;

	public ClamAVClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String sendCommand(Command command) throws IOException {
		return sendMessage(command.getRequestByteArray(ClamAVSeparator.NULL).toByteArray());
	}
	
	public String sendCommand(Command command, ClamAVSeparator separator) throws IOException {
		return sendMessage(command.getRequestByteArray(separator).toByteArray());
	}

	protected String sendMessage(byte[] message) throws IOException {
		StringBuilder builder = new StringBuilder();
		try (Socket socket = new Socket(host, port)) {
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();

			outputStream.write(message);
			outputStream.flush();

			int opByte = -1;
			while ((opByte = inputStream.read()) != -1) {
				builder.append((char) opByte);
			}
		}

		return builder.toString();
	}
}
