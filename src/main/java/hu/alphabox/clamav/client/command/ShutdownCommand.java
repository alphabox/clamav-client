package hu.alphabox.clamav.client.command;

/**
 * It is perform a clen exit to Clamd.
 * 
 * @author Daniel Mecsei
 *
 */
public class ShutdownCommand extends SimpleCommand {

	private static final byte[] COMMAND = { 'S', 'H', 'U', 'T', 'D', 'O', 'W', 'N' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
