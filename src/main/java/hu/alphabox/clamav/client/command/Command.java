package hu.alphabox.clamav.client.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import hu.alphabox.clamav.client.ClamAVSeparator;

/**
 * 
 * It represents a ClamAV command.
 * You can send every command with the simple {@code ClamAVClient}, except
 * the IDSESSION and the END command, which we use in the session based
 * clients.
 * 
 * @author Daniel Mecsei
 *
 */
public interface Command {

	/**
	 * 
	 * @param separator an {@code ClamAVSeparator}
	 * @return the requested command in a byte array
	 * @throws IOException if an I/O error occurs.
	 */
	public ByteArrayOutputStream getRequestByteArray(ClamAVSeparator separator) throws IOException;
	
}
