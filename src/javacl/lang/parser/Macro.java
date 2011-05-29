package javacl.lang.parser;

import java.io.IOException;

import javacl.lang.JavaclException;

public class Macro extends Text  {

	public Macro(){super();}
	public Macro(VariableStack stack) {
		super(stack);
	}

	@Override
	public String expand(Context parserContext) throws JavaclException {
		
		try {
			
			SyntaxParser parser = new SyntaxParser(get(), parserContext);
			parser.run(); 
			
		}catch(IOException e){
			throw new JavaclException("IO Error",e);
		} catch (SecurityException e1){	
		} catch (NoSuchMethodException e1) {}
	
		return new String();
	}
	

}
