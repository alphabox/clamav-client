package hu.alphabox.clamav.client.command;

/**
 * The Clamd returns with statistics about the scan queue,
 * contents of scan queue, and memory usage.
 * 
 * @author Daniel Mecsei
 *
 */
public class StatsCommand extends SimpleCommand implements SessionCommand {

	private static final byte[] COMMAND = { 'S', 'T', 'A', 'T', 'S' };

	@Override
	protected byte[] getCommand() {
		return COMMAND;
	}

}
