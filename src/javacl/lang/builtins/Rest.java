package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.RoutineContext;

/**
 * Currently no #argument...
 * @author justin
 *
 */
public class Rest extends Builtin {

	@Override
	public String expand(Context parserContext) throws JavaclException {
		RoutineContext rctx = parserContext.getRoutineContext();
		if(rctx==null)
			throw new JavaclException("No routine called");
		
		return rctx.getArgument().toString();
	}

	@Override
	public String getName() {
		return "rest";
	}

}
