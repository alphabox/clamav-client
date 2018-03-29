package hu.alphabox.clamav.client;

/**
 * 
 * The class contains the available separators for Clamd.
 * If your data/message contains one of the separators, you
 * can choose the other one.
 * 
 * @author Daniel Mecsei
 *
 */
public enum ClamAVSeparator {

	/**
	 * Store the new line character prefix and separator.
	 */
	NEW_LINE('n','\n' ),
	
	/**
	 * Store the null character prefix and separator.
	 */
	NULL('z','\0');
	
	/**
	 * The prefix of the command.
	 */
	private final char prefix;
	
	/**
	 * The separator between two command (end of the whole command).
	 */
	private final char separator;
	
	private ClamAVSeparator(char prefix, char separator) {
		this.prefix = prefix;
		this.separator = separator;
	}
	
	/**
	 * Returns with the prefix of the command.
	 * @return the prefix
	 */
	public char getPrefix() {
		return prefix;
	}
	
	/**
	 * Returns with the separator, end of the whole command.
	 * @return the separator
	 */
	public char getSeparator() {
		return separator;
	}
	
}
