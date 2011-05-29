package javacl.lang.parser;

import javacl.lang.JavaclException;

public interface WriteVariable {
	public void set(String text, Context parserContext) throws JavaclException;
}
