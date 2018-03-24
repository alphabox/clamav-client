package hu.alphabox.clamav.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import hu.alphabox.clamav.client.command.SessionCommand;
import hu.alphabox.clamav.client.command.SimpleCommand;

public abstract class AbstractClamavSessionClient<T> implements AutoCloseable {

	private final Socket socket;

	private final InputStream inputStream;

	private final OutputStream outputStream;
	
	public abstract T sendCommand(SessionCommand command) throws IOException;

	protected AbstractClamavSessionClient(InetAddress address, int port) throws IOException {
		this.socket = new Socket(address, port);
		this.inputStream = socket.getInputStream();
		this.outputStream = socket.getOutputStream();

		sendCommandWithoutResponse(new IdSessionCommand());
	}

	protected void sendCommandWithoutResponse(SessionCommand command) throws IOException {
		outputStream.write(command.getRequestByteArray(ClamAVSeparator.NULL).toByteArray());
		outputStream.flush();
	}

	protected Socket getSocket() {
		return socket;
	}

	protected InputStream getInputStream() {
		return inputStream;
	}

	protected OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void close() throws Exception {
		sendCommandWithoutResponse(new EndCommand());
		socket.close();
	}

	protected static class IdSessionCommand extends SimpleCommand implements SessionCommand {

		private static final byte[] COMMAND = { 'I', 'D', 'S', 'E', 'S', 'S', 'I', 'O', 'N' };

		@Override
		protected byte[] getCommand() {
			return COMMAND;
		}

	}

	protected static class EndCommand extends SimpleCommand implements SessionCommand {

		private static final byte[] COMMAND = { 'E', 'N', 'D' };

		@Override
		protected byte[] getCommand() {
			return COMMAND;
		}
	}

}
