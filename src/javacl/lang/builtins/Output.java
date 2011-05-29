package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;

public class Output extends Builtin{

	
	@Override
	public String expand(Context parserContext) throws JavaclException {
		String text = parserContext.getArgument().toString();
		System.out.println(text);
		return new String();
	}

	@Override
	public String getName() {
		return "output";
	}

}
