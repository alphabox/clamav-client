package hu.alphabox.clamav.client;

public enum ClamAVSeparator {

	NEW_LINE('n','\n' ), NULL('z','\0');
	
	private final char prefix;
	
	private final char separator;
	
	private ClamAVSeparator(char prefix, char separator) {
		this.prefix = prefix;
		this.separator = separator;
	}
	
	public char getPrefix() {
		return prefix;
	}
	
	public char getSeparator() {
		return separator;
	}
	
}
