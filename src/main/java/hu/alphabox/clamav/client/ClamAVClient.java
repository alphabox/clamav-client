package hu.alphabox.clamav.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import hu.alphabox.clamav.client.command.Command;

/**
 * <p>
 * A simple ClamAV client for Clamd.
 * You can send every command (except IDSESSION and END command) for Clamd, and
 * you should get the response of the ClamAV daemon.
 * </p>
 * <p>
 * The class opens new connection for every command.
 * </p>
 * 
 * @author Daniel Mecsei
 *
 */
public class ClamAVClient {

	/**
	 * The domain or IP address of the ClamAV daemon.
	 */
	private String host;

	/**
	 * The port number of the ClamAV daemon.
	 */
	private int port;

	public ClamAVClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * Send a {@code Command} to ClamAV daemon with additional data (if the command has).
	 * It is send the command with NULL separator.
	 * 
	 * @param command a ClamAV command
	 * @return the response of the ClamAV daemon
	 * @throws IOException if an I/O error occurs.
	 * @throws ClamAVException if an error occurs about the ClamAV daemon.
	 */
	public String sendCommand(Command command) throws IOException, ClamAVException {
		return sendMessage(command.getRequestByteArray(ClamAVSeparator.NULL));
	}
	
	/**
	 * Send a {@code Command} to ClamAV daemon with additional data (if the command has).
	 * 
	 * @param command a ClamAV command
	 * @param separator a {@code ClamAVSeparator}
	 * @return the response of the ClamAV daemon
	 * @throws IOException if an I/O error occurs.
	 * @throws ClamAVException if an error occurs about the ClamAV daemon.
	 */
	public String sendCommand(Command command, ClamAVSeparator separator) throws IOException, ClamAVException {
		return sendMessage(command.getRequestByteArray(separator));
	}

	/**
	 * Send the {@code ByteArrayOutputStream} message for ClamAV daemon.
	 * It opens a new connection for every command (with try-with-resources statement).
	 * 
	 * @param message the ClamAV command with addition data
	 * @return the response of the ClamAV daemon
	 * @throws IOException if an I/O error occurs.
	 * @throws ClamAVException if an error occurs about the ClamAV daemon.
	 */
	protected String sendMessage(ByteArrayOutputStream message) throws IOException, ClamAVException {
		StringBuilder builder = new StringBuilder();
		try (Socket socket = new Socket(host, port)) {
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();

			message.writeTo(outputStream);
			outputStream.flush();

			int opByte = -1;
			while ((opByte = inputStream.read()) != -1) {
				builder.append((char) opByte);
			}
		}
		
		if(builder.toString().endsWith("ERROR")) {
			throw new ClamAVException(builder.toString());
		}

		return builder.toString();
	}
}
