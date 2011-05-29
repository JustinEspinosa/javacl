package javacl.lang.parser.io;

import javacl.lang.JavaclException;

public class SyntaxErrorException extends JavaclException {

	private static final long serialVersionUID = 2566213025000010069L;

	public SyntaxErrorException(String msg) {
		super(msg);
	}

	public SyntaxErrorException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
