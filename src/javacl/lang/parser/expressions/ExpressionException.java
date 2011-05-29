package javacl.lang.parser.expressions;

import javacl.lang.JavaclException;

public class ExpressionException extends JavaclException {

	private static final long serialVersionUID = 2563436549943103080L;

	public ExpressionException(String msg) {
		super(msg);
	}

	public ExpressionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
