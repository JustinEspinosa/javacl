package javacl.lang.builtins;

import java.util.TreeMap;

public class BuiltinDirectory {
	private static TreeMap<String,Builtin> builtins = new TreeMap<String,Builtin>();
	
	static{
		registerBuiltin(new Set());
		registerBuiltin(new Setv());
		registerBuiltin(new Push());
		registerBuiltin(new Output());
		registerBuiltin(new Outputv());
		registerBuiltin(new Pop());
		registerBuiltin(new Def());
		registerBuiltin(new Frame());
		registerBuiltin(new Unframe());
		registerBuiltin(new Rest());
		registerBuiltin(new If());
		registerBuiltin(new Compute());
		registerBuiltin(new Loop());
		registerBuiltin(new Builtins());
		registerBuiltin(new Variables());
		registerBuiltin(new Result());
		registerBuiltin(new Defaults());
		registerBuiltin(new XFileInfo());
		registerBuiltin(new Exit());
		registerBuiltin(new PmSearchList());
		registerBuiltin(new Prefix());
	}
	
	
	public static Builtin[] builtins(){
		return builtins.values().toArray(new Builtin[0]);
	}
	
	public static void registerBuiltin(Builtin builtin){
		builtins.put(builtin.getName().toLowerCase(),builtin);
	}
	
	public static Builtin getBuiltin(String builtInName){
		if(builtInName.length() < 1)
			return null;
		
		if(builtInName.charAt(0) == '#')
			return builtins.get(builtInName.toLowerCase().substring(1));
		
		return null;
	}
}
