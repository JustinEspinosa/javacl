package javacl.lang.parser;

import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;
import java.util.Vector;

import javacl.lang.JavaclException;

public class VariableStack {
	private Vector<Variable> variables = new Vector<Variable>();
	private String variableName;
	
	public VariableStack(String name){
		variableName = name;
	}
	
	public String getName(){
		return variableName;
	}
	
	public boolean empty(){
		return variables.isEmpty();
	}
	
	public Variable current(){
		try{
			return variables.lastElement();
		}catch(NoSuchElementException e){
			return null;
		}
	}
	
	public void pop(){
		if(variables.size()>0)
			variables.remove(variables.size()-1);
	}
	
	public Variable push(Class<? extends Variable> cl,int frame) throws JavaclException{
		try{
			Constructor<? extends Variable> constr = cl.getConstructor(VariableStack.class);
			Variable var = constr.newInstance(this);
			variables.add(var);
			var.setFrame(frame);
			return var;
		}catch(Exception e){
			throw new JavaclException("Internal parser failure.", e);
		}
	}
	
	public Variable push(int frame) throws JavaclException{
		return push(Text.class, frame);
	}
	
}
