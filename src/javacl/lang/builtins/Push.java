package javacl.lang.builtins;

import java.util.regex.Matcher;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.ParserUtils;

public class Push extends Builtin {

	
	@Override
	public String expand(Context parserContext) throws JavaclException {
		String argumentList = parserContext.getArgument().getArgument();
		
		Matcher matches = ParserUtils.word.matcher(argumentList);
		
		while(matches.find()){
			parserContext.getEnv().push(matches.group());
		}
		
		return new String();
	}

	@Override
	public String getName() {
		return "push";
	}

}
