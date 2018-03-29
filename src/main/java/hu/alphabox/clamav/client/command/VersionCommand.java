package hu.alphabox.clamav.client.command;

/**
 * Returns with program and database versions.
 * @author Daniel Mecsei
 *
 */
public class VersionCommand extends SimpleCommand implements SessionCommand {

	private static final byte[] COMMAND = { 'V', 'E', 'R', 'S', 'I', 'O', 'N' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
