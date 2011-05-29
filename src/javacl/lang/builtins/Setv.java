package javacl.lang.builtins;

import java.util.regex.Matcher;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.ParserUtils;
import javacl.lang.parser.ReadVariable;
import javacl.lang.parser.VariableType;
import javacl.lang.parser.WriteVariable;

public class Setv extends Set {
	@Override
	public String expand(Context parserContext) throws JavaclException {
		
		String argStr = parserContext.getArgument().getArgument();
		Matcher matches = ParserUtils.word.matcher(argStr);
		
		if(matches.find()){
			VariableType var = ParserUtils.findVariable(parserContext,matches.group());
			if(var instanceof WriteVariable){
				if(matches.find()){
					VariableType var2 = ParserUtils.findVariable(parserContext,matches.group());
					if(var2 instanceof ReadVariable){
						((WriteVariable)var).set(((ReadVariable)var2).get(), parserContext);
						return new String();
					}
					throw new JavaclException("Cannot read that.");
				}
			}
		}
		throw new JavaclException("Cannot set this.");
	}
}
