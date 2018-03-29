package hu.alphabox.clamav.client.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import hu.alphabox.clamav.client.ClamAVSeparator;

/**
 * 
 * A simple abstract class with a default implementation of {@link Command}.
 * It returns with a {@code ByteArrayOutputStream}, which contains the expected command.
 * <p>
 * If a child class want to send more information to Clamd (such as {@link InStreamCommand},
 * than it will be override the {@code getRequestByteArray()} method and add additional information
 * to the message.
 * </p>
 * 
 * @author Daniel Mecsei
 *
 */
public abstract class SimpleCommand implements Command {

	protected abstract byte[] getCommand();
	
	@Override
	public ByteArrayOutputStream getRequestByteArray(ClamAVSeparator separator) throws IOException {
		byte[] command = getCommand();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(command.length + 2);
		outputStream.write(separator.getPrefix());
		outputStream.write(command);
		outputStream.write(separator.getSeparator());
		return outputStream;
	}
	
}
