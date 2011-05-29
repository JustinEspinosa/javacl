package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.ParserUtils;

public class Variables extends Builtin {

	@Override
	public String expand(Context parserContext) throws JavaclException {
		Object[] variables = parserContext.getEnv().getHome().variables();
		return ParserUtils.spaceList(variables);
	}

	@Override
	public String getName() {
		return "variables";
	}

}
