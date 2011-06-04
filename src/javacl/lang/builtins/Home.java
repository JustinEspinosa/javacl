package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.Directory;
import javacl.lang.parser.Variable;
import javacl.lang.parser.WriteVariable;

public class Home extends Builtin implements WriteVariable{

	@Override
	public String expand(Context parserContext) throws JavaclException {
		return parserContext.getEnv().getHome().fullPath();
	}

	@Override
	public void set(String text, Context parserContext) throws JavaclException {
		Variable v = parserContext.getEnv().findVariable(text);
		if(v instanceof Directory)
			parserContext.getEnv().setHome((Directory)v);
		else
			throw new JavaclException("no.");
	}

}
