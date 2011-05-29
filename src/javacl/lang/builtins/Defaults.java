package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.environment.FsUtils;
import javacl.lang.parser.Context;
import javacl.lang.parser.WriteVariable;

public class Defaults extends Builtin implements WriteVariable {

	@Override
	public String expand(Context parserContext) throws JavaclException {
		return FsUtils.getDefaults(false);
	}

	@Override
	public String getName() {
		return "defaults";
	}

	@Override
	public void set(String text, Context parserContext) throws JavaclException {
		FsUtils.setDefaults(text.trim());
	}

}
