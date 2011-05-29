package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;

public class Unframe extends Builtin {

	@Override
	public String expand(Context parserContext) throws JavaclException {
		
		parserContext.getEnv().getHome().unframe();
		return new String();
	}

	@Override
	public String getName() {
		return "unframe";
	}

}
