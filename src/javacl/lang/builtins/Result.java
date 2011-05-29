package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.RoutineContext;

public class Result extends Builtin {

	@Override
	public String expand(Context parserContext) throws JavaclException {
		RoutineContext rctx = parserContext.getRoutineContext();
		if(rctx==null)
			throw new JavaclException("No routine called");
		
		rctx.result(parserContext.getArgument().toString());
		
		return new String();
	}

	@Override
	public String getName() {
		return "result";
	}

}
