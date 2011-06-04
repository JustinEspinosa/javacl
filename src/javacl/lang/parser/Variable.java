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
	
	public Directory getParent(){
		return getStack().getDirectory();
	}
	
	public String fullPath(){
		if(getParent()==null)
			return toString();
		
		return getParent().fullPath() + this;
	}
	
	@Override
	public String toString() {
		return getStack().getName()+"."+getStack().getIndex(this);
	}

}
