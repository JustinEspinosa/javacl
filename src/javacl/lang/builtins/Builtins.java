package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.ParserUtils;

public class Builtins extends Builtin {

	@Override
	public String expand(Context parserContext) throws JavaclException {
		Object[] builtins = BuiltinDirectory.builtins();
		return ParserUtils.spaceList(builtins);
	}

	@Override
	public String getName() {
		return "builtins";
	}

}
