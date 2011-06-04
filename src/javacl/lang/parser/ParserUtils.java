package javacl.lang.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

import javacl.lang.JavaclException;
import javacl.lang.builtins.BuiltinDirectory;
import javacl.lang.parser.io.ExpanderReader;

public class ParserUtils {
	
	public static final Pattern word = Pattern.compile("\\S+");
	public static final Pattern tag  = Pattern.compile("\\|\\s+(\\W+)\\s*\\|");
	
	public static final String defaultEnclosure = "<default>";
	//Keep that one sorted.
	public static final char[] separator = {'\t',' '};
	
	public static final char   continueChar = '&';
	public static final char   escapeChar = '~';
	public static final int    eof = -1;
	
	public static String expand(Context c, String data) throws JavaclException, IOException{
		data = data.trim();
		if(data.startsWith("[")){
			data = data.substring(1);
			if(data.length()>0);
				data = data.substring(0,data.length()-1);
			ExpanderReader r = new ExpanderReader(new StringReader(data), c, true);
			return r.readBlock().execute();
		}
		return data;
	}
	
	
	public static VariableType findVariable(Context c,String name) throws JavaclException{
		
		if(name.length()>0 && name.charAt(0)=='#'){
			//# indicates a built-in variable
			VariableType builtin = BuiltinDirectory.getBuiltin(name);
			if(builtin!=null)
				return builtin;
			
			throw new RuntimeException("No such builtin: "+name);
		}

		//OK, then look for it in the user variables
		VariableType var = c.getEnv().findVariable(name);
		if(var!=null)
			return var;
		
		return null;
	}
	
	
	public static String spaceList(Object ... list){
		StringBuilder bldr = new StringBuilder();
		
		for(Object o: list){
			bldr.append(o.toString());
			bldr.append(" ");
		}
			
		return bldr.toString().trim();
	}

}
