package hu.alphabox.clamav.client.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import hu.alphabox.clamav.client.ClamAVSeparator;

public abstract class SimpleCommand implements Command {

	protected abstract byte[] getCommand();
	
	@Override
	public ByteArrayOutputStream getRequestByteArray(ClamAVSeparator separator) throws IOException {
		byte[] command = getCommand();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(getCommand().length + 2);
		outputStream.write(separator.getPrefix());
		outputStream.write(command);
		outputStream.write(separator.getSeparator());
		return outputStream;
	}
	
}
