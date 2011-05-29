package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.WriteVariable;

public class Exit extends Builtin implements WriteVariable {

	@Override
	public String expand(Context parserContext) throws JavaclException {
		boolean e = parserContext.getEnv().getExitFlag();
		return (e?"-1":"0");
	}

	@Override
	public String getName() {
		return "exit";
	}

	@Override
	public void set(String text, Context parserContext) throws JavaclException {
		int n = 0;
		try{
			n = (int)Double.parseDouble(text);
		}catch (NumberFormatException e) {}
		
		boolean b = (n != 0);
		parserContext.getEnv().setExitFlag(b);
	}

}
