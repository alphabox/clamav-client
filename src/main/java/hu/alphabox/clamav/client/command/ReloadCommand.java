package hu.alphabox.clamav.client.command;

/**
 * It asks the Clamd to reload the virus databases.
 * 
 * @author Daniel Mecsei
 *
 */
public class ReloadCommand extends SimpleCommand {

	private static final byte[] COMMAND = { 'R', 'E', 'L', 'O', 'A', 'D' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
