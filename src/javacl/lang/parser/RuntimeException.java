package javacl.lang.parser;

import javacl.lang.JavaclException;

public class RuntimeException extends JavaclException{

	private static final long serialVersionUID = 1249049402188231682L;

	public RuntimeException(String msg) {
		super(msg);
	}
	
	public RuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
