package hu.alphabox.clamav.client.command;

/**
 * It checks the server's state.
 * It should reply with "PONG".
 * 
 * @author Daniel Mecsei
 *
 */
public class PingCommand extends SimpleCommand {

	private static final byte[] COMMAND = { 'P', 'I', 'N', 'G' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
