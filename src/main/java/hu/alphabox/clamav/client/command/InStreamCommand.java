package hu.alphabox.clamav.client.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import hu.alphabox.clamav.client.ClamAVSeparator;

public class InStreamCommand implements Command, SessionCommand {

	private static final byte[] COMMAND = { 'I', 'N', 'S', 'T', 'R', 'E', 'A', 'M' };

	private String message;

	public InStreamCommand(String message) {
		this.message = message;
	}

	@Override
	public ByteArrayOutputStream getRequestByteArray(ClamAVSeparator separator) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(COMMAND.length + 2 + message.getBytes().length);
		outputStream.write(separator.getPrefix());
		outputStream.write(COMMAND);
		outputStream.write(separator.getSeparator());
		outputStream.write(ByteBuffer.allocate(4).putInt(message.getBytes().length).array());
		outputStream.write(message.getBytes());
		outputStream.write(new byte[] { 0, 0, 0, 0 });
		return outputStream;
	}

}
