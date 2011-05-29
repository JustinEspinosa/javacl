package javacl.lang.parser;

import javacl.lang.JavaclException;

public class Routine extends Macro{

	public Routine(){super();}
	public Routine(VariableStack stack) {
		super(stack);
	}
	
	@Override
	public String expand(Context parserContext) throws JavaclException {
		parserContext.createRoutineContext();
		super.expand(parserContext);
		return parserContext.getRoutineContext().result();
	}

}
