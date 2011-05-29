package javacl.lang.parser.io;

import javacl.lang.JavaclException;

public class EofException extends JavaclException {

	private static final long serialVersionUID = -4513577638038114358L;

	public EofException(String msg) {
		super(msg);
	}

	public EofException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
