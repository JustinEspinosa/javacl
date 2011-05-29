package javacl.lang.parser;

public class Variable implements VariableType{
	private VariableStack myStack = null;
	private int frame = 0;

	/**
	 * An anonymous one.
	 */
	public Variable(){}
	
	public Variable(VariableStack stack){
		myStack = stack;
	}
	
	public VariableStack getStack(){
		return myStack;
	}
	
	public void setFrame(int f){
		frame = f;
	}
	public int getFrame(){
		return frame;
	}
	

}
