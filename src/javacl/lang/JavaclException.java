package javacl.lang;

import java.util.Vector;

public class JavaclException extends Exception{

	private static final long serialVersionUID = -266441339369878651L;
	private Vector<String> callTrace = new Vector<String>();

	public JavaclException(String msg) {
		super(msg);
	}

	public JavaclException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public void addTraceLine(String line){
		callTrace.add(line);
	}
	
	public void displayTrace(){
		for(String line:callTrace)
			System.out.println(line);
	}
	
}
