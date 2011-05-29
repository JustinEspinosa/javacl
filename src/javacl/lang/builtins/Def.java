package javacl.lang.builtins;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.Macro;
import javacl.lang.parser.ParserUtils;
import javacl.lang.parser.Routine;
import javacl.lang.parser.Text;
import javacl.lang.parser.Variable;
import javacl.lang.parser.WriteVariable;

public class Def extends Builtin {

	private static Map<String,Class<? extends Variable>> types = new TreeMap<String, Class<? extends Variable>>();
	
	static{
		types.put("text", Text.class);
		types.put("routine", Routine.class);
		types.put("macro", Macro.class);
		//...
	}
	
	@Override
	public String expand(Context parserContext) throws JavaclException {
		
		String cmd = parserContext.getArgument().getEnclosure(ParserUtils.defaultEnclosure);
		if(cmd == null)
			throw new JavaclException("Syntax not good.");
		
		Matcher m = ParserUtils.word.matcher(cmd);
		
		String name;
		if(!m.find())
			throw new JavaclException("No name");
		
		name = m.group();
		
		Class<? extends Variable> cl;
		if(!m.find())
			throw new JavaclException("No type");
		
		cl = types.get(m.group().toLowerCase());
		if(cl==null)
			throw new JavaclException("Unknown type ");
		
		String text = parserContext.getArgument().getEnclosure("body");
		if(text==null)
			throw new JavaclException("No body");
		
		parserContext.getEnv().getHome().push(name, cl);
		
		Variable var = parserContext.getEnv().getHome().getByShortName(name);
		
		if(var instanceof WriteVariable)
			((WriteVariable) var).set(text, parserContext);
		
		return new String();
	}

	@Override
	public String getName() {
		return "def";
	}

}
