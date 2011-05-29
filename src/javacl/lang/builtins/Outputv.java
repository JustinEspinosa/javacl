package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.ParserUtils;
import javacl.lang.parser.ReadVariable;
import javacl.lang.parser.VariableType;

public class Outputv extends Builtin {

	@Override
	public String expand(Context parserContext) throws JavaclException {
		String varname = parserContext.getArgument().toString();
		
		VariableType var = ParserUtils.findVariable(parserContext, varname);
		
		if(var instanceof ReadVariable){
			System.out.println(((ReadVariable) var).get());
			return new String();
		}
		throw new JavaclException("Cannot read the content of "+varname);
	}



}
