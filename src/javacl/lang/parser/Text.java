package javacl.lang.parser;

import javacl.lang.JavaclException;

public class Text extends Variable implements AppendVariable, ExpandVariable, ReadVariable, WriteVariable{

	public Text(){super();}
	
	public Text(VariableStack stack) {
		super(stack);
	}

	private StringBuilder contentText = new StringBuilder();
	
	@Override
	public void set(String text, Context parserContext) {
		contentText = new StringBuilder();
		contentText.append(text.trim());
	}

	@Override
	public String get() {
		return contentText.toString();
	}


	@Override
	public String expand(Context parserContext) throws JavaclException{
		return contentText.toString();
	}

	@Override
	public void append(String line, Context ctx) {
		contentText.append(line);
		contentText.append('\n');
	}

}
