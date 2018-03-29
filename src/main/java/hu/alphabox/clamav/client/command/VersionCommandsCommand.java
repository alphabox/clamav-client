package hu.alphabox.clamav.client.command;

/**
 * Returns with program and database versions, followed by "| COMMANDS:"
 * and a space-delimited list of supported commands. Clamd < 0.95 will recognize
 * this as the VERSION command, and reply only with their version, without the commands list.
 * 
 * @author Daniel Mecsei
 *
 */
public class VersionCommandsCommand extends SimpleCommand {

	private static final byte[] COMMAND = { 'V', 'E', 'R', 'S', 'I', 'O', 'N', 'C', 'O', 'M', 'M', 'A', 'N', 'D', 'S' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
