package hu.alphabox.clamav.client.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import hu.alphabox.clamav.client.ClamAVSeparator;

public interface Command {

	public ByteArrayOutputStream getRequestByteArray(ClamAVSeparator separator) throws IOException;
	
}
