package hu.alphabox.clamav.client.command;

/**
 * 
 * Classes that implement this interface will can be send in a ClamAV session.
 * With a session, you do not have to open new TCP connection for every
 * command that you want to send for Clamd.
 * 
 * @author Daniel Mecsei
 *
 */
public interface SessionCommand extends Command {

}
