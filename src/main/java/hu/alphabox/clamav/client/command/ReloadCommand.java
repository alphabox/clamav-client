package hu.alphabox.clamav.client.command;

public class ReloadCommand extends SimpleCommand {

	private static final byte[] COMMAND = { 'R', 'E', 'L', 'O', 'A', 'D' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
