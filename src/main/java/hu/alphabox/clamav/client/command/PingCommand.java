package hu.alphabox.clamav.client.command;

public class PingCommand extends SimpleCommand {

	private static final byte[] COMMAND = { 'P', 'I', 'N', 'G' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
