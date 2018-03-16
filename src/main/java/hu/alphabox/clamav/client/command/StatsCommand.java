package hu.alphabox.clamav.client.command;

public class StatsCommand extends SimpleCommand implements SessionCommand {

	private static final byte[] COMMAND = { 'S', 'T', 'A', 'T', 'S' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
