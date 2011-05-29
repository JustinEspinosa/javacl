package javacl.lang.parser;

import javacl.lang.JavaclException;

public interface ExpandVariable {
	public String expand(Context parserContext) throws JavaclException;
}
