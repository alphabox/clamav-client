package hu.alphabox.clamav.client.command;

public class VersionCommand extends SimpleCommand implements SessionCommand {

	private static final byte[] COMMAND = { 'V', 'E', 'R', 'S', 'I', 'O', 'N' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
