package javacl.lang.parser;


import java.util.Map;
import java.util.TreeMap;

public class Argument {
	private Map<String, String> argument;
	public Argument(Map<String, String> arg){
		argument = arg;
	}
	
	public Argument(String arg) {
		argument = new TreeMap<String, String>();
		argument.put(ParserUtils.defaultEnclosure, arg);
	}

	public String getArgument(){
		return argument.get(ParserUtils.defaultEnclosure);
	}
	
	public String getEnclosure(String name){
		return argument.get(name);
	}
	
	@Override
	public String toString() {
		return getArgument().trim();
	}
}
