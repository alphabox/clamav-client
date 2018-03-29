package hu.alphabox.clamav.client.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import hu.alphabox.clamav.client.ClamAVSeparator;

/**
 * The Clamd scan a stream of data. The stream is sent to clamd in one chunk,
 * after INSTREAM, on the same socket on which the command was sent.
 * This avoids the overhead of establishing new TCP connections and problems with NAT.
 * 
 * @author Daniel Mecsei
 *
 */
public class InStreamCommand extends SimpleCommand implements SessionCommand {

	private static final byte[] COMMAND = { 'I', 'N', 'S', 'T', 'R', 'E', 'A', 'M' };

	/**
	 * Store the message for ClamAV.
	 */
	private String message;

	public InStreamCommand(String message) {
		if ( message == null || message.isEmpty() ) {
			throw new IllegalArgumentException("The message cannot be empty or null value.");
		}
		this.message = message;
	}
	
	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

	@Override
	public ByteArrayOutputStream getRequestByteArray(ClamAVSeparator separator) throws IOException {
		ByteArrayOutputStream outputStream = super.getRequestByteArray(separator);
		outputStream.write(ByteBuffer.allocate(4).putInt(message.getBytes().length).array());
		outputStream.write(message.getBytes());
		outputStream.write(new byte[] { 0, 0, 0, 0 });
		return outputStream;
	}

}
