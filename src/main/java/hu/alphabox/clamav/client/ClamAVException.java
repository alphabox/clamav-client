package hu.alphabox.clamav.client;

/**
 * The class {@code ClamAVException} represents a problem about the ClamAV daemon.
 * If the end of the response equals with word ERROR, than we throw a new
 * {@code ClamAVException} with the response as message.
 * 
 * @author Daniel Mecsei
 *
 */
public class ClamAVException extends Exception {

	private static final long serialVersionUID = 3965674910568956829L;

	public ClamAVException(String message) {
		super(message);
	}
	
}
