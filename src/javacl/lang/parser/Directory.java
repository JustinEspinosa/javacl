package javacl.lang.parser;

import java.util.TreeMap;

import javacl.lang.JavaclException;

public class Directory extends Variable {

	TreeMap<String,VariableStack> content = new TreeMap<String, VariableStack>();
	private int currentframe;
	
	public Directory(VariableStack stack) {
		super(stack);
	}

	public void frame() {
		currentframe++;
	}

	public void unframe() {
		
		//pop everything that was pushed in that frame
		for(VariableStack stack: content.values() )
			while(stack.current()!=null && stack.current().getFrame() == currentframe)
				stack.pop();

		currentframe--;
	}
	
	private VariableStack getStack(String name){
		name = name.toLowerCase();
		
		VariableStack stack = new VariableStack(name);
		
		if(content.containsKey(name))
			stack = content.get(name);
		else
			content.put(name,stack);
		
		return stack;
	}
	
	public Variable push(String name, Class<? extends Variable> cl) throws JavaclException{
		return getStack(name).push(cl,currentframe);
	}
	
	public Variable push(String name) throws JavaclException{
		return getStack(name).push(currentframe);
	}
	
	public void pop(String name){
		name = name.toLowerCase();
		
		if(content.containsKey(name)){
			VariableStack stack = content.get(name);
			stack.pop();
			if(stack.empty())
				content.remove(name);
		}
	}
	
	public Variable getByShortName(String name){
		name = name.toLowerCase();
		
		Variable found = null;
		if(content.containsKey(name))
			found = content.get(name).current();
		
		return found;
	}

	public String[] variables() {
		return content.keySet().toArray(new String[0]);
	}

}
