package javacl.lang.parser;

public class RoutineContext {
	private Argument argument;
	//StringBuilder?
	private String   result   = "";
	
	public RoutineContext(Argument arg){
		argument = arg;
	}
	
	public Argument getArgument(){
		return argument;
	}
	
	public String result(){
		return result.trim();
	}
	
	public void result(String s){
		result +=" "+s;
	}
}
