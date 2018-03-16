package hu.alphabox.clamav.client.command;

public class VersionCommandsCommand extends SimpleCommand {

	private static final byte[] COMMAND = { 'V', 'E', 'R', 'S', 'I', 'O', 'N', 'C', 'O', 'M', 'M', 'A', 'N', 'D', 'S' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
