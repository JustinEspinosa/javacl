package javacl.lang.environment;

import javacl.lang.JavaclException;

public class FileSystemException extends JavaclException {

	private static final long serialVersionUID = -4053169694937659439L;

	public FileSystemException(String msg) {
		super(msg);
	}
	
	public FileSystemException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
