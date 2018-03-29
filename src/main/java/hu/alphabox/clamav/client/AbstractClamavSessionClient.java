package hu.alphabox.clamav.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import hu.alphabox.clamav.client.command.SessionCommand;
import hu.alphabox.clamav.client.command.SimpleCommand;

/**
 * <p>
 * A simple abstract class which store the connection of a session client, and
 * add some helper method to handle the {@code Socket} and message sending.
 * </p>
 * <p>
 * It contains the implementation of IDSESSION and END command.
 * </p>
 * 
 * @author Daniel Mecsei
 *
 * @param <T> the type of the result of {@code sendCommand}
 */
public abstract class AbstractClamavSessionClient<T> implements AutoCloseable {

	/**
	 * A connection to ClamAV daemon.
	 */
	private final Socket socket;

	/**
	 * The input stream of the ClamAV daemon.
	 */
	private final InputStream inputStream;

	/**
	 * The output stream of the ClamAV daemon.
	 */
	private final OutputStream outputStream;
	
	/**
	 * Send a {@code Command} to ClamAV daemon with additional data (if the command has).
	 * It is not open a new connection for every command, it is use one connection
	 * for the whole session.
	 * 
	 * @param command a ClamAV command
	 * @return a T type result
	 * @throws IOException if an I/O error occurs.
	 * @throws ClamAVException if an error occurs about the ClamAV daemon.
	 */
	public abstract T sendCommand(SessionCommand command) throws IOException, ClamAVException;

	protected AbstractClamavSessionClient(InetAddress address, int port) throws IOException {
		this.socket = new Socket(address, port);
		this.inputStream = socket.getInputStream();
		this.outputStream = socket.getOutputStream();

		sendCommandWithoutResponse(new IdSessionCommand());
	}

	/**
	 * Send a {@code Command} to ClamAV daemon, where we do not expect any response.
	 * (Except in async mode, where we do not wait for response and return immediately.)
	 * 
	 * @param command a ClamAV command
	 * @throws IOException if an I/O error occurs.
	 */
	protected void sendCommandWithoutResponse(SessionCommand command) throws IOException {
		command.getRequestByteArray(ClamAVSeparator.NULL).writeTo(outputStream);
		outputStream.flush();
	}

	/**
	 * Returns the socket of the connection for ClamAV daemon.
	 * @return a socket
	 */
	protected Socket getSocket() {
		return socket;
	}

	/**
	 * Returns the input stream of the socket for ClamAV daemon.
	 * @return the input stream
	 */
	protected InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Returns the output stream of the socket for ClamAV daemon.
	 * @return the output stream
	 */
	protected OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void close() throws Exception {
		sendCommandWithoutResponse(new EndCommand());
		socket.close();
	}

	/**
	 * Represents the IDSESSION command for ClamAV daemon.
	 * We use it only when we create a new connection for a session.
	 * 
	 * @author Daniel Mecsei
	 *
	 */
	protected static class IdSessionCommand extends SimpleCommand implements SessionCommand {

		private static final byte[] COMMAND = { 'I', 'D', 'S', 'E', 'S', 'S', 'I', 'O', 'N' };

		@Override
		protected byte[] getCommand() {
			return COMMAND;
		}

	}

	/**
	 * Represents the END command for ClamAV daemon.
	 * We use it only when we want to close the session connection for the daemon.
	 * 
	 * @author Daniel Mecsei
	 *
	 */
	protected static class EndCommand extends SimpleCommand implements SessionCommand {

		private static final byte[] COMMAND = { 'E', 'N', 'D' };

		@Override
		protected byte[] getCommand() {
			return COMMAND;
		}
	}

}
