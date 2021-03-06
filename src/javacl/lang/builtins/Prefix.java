package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.WriteVariable;

public class Prefix extends Builtin implements WriteVariable {

	
	@Override
	public String expand(Context parserContext) throws JavaclException {
		return parserContext.getEnv().getPrefix();
	}

	@Override
	public void set(String text, Context parserContext) throws JavaclException {
		parserContext.getEnv().setPrefix(text);
	}

}
