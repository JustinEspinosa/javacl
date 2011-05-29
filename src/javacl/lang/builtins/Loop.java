package javacl.lang.builtins;


import java.io.IOException;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.SyntaxParser;
import javacl.lang.parser.expressions.ExpressionNode;
import javacl.lang.parser.expressions.Expressions;

public class Loop extends Builtin {
	
	
	private static boolean condition(ExpressionNode tree, Context parserContext) throws JavaclException{
		Object eval = tree.evaluate(parserContext);
		double result = 0;
		try{
			result = Double.valueOf(eval.toString());
		}catch(NumberFormatException exp){}
		
		return (result!=0);
	}
	
	@Override
	public String expand(Context parserContext) throws JavaclException {
		
		
		boolean compto = true;
		
		String expr = parserContext.getArgument().getEnclosure("while");
		if(expr == null){
			expr = parserContext.getArgument().getEnclosure("until");
			compto = false;
		}
		
		if(expr == null)
			throw new JavaclException("While or until expected");
		
		String dos = parserContext.getArgument().getEnclosure("do");
		if(dos == null)
			throw new JavaclException("Do expected");
		
		ExpressionNode tree = Expressions.parseExpression(expr);
		
		try{
			SyntaxParser parser = new SyntaxParser(dos, parserContext);
			
			if(compto){
				while( condition(tree, parserContext) ){
					parser.run();
					parser = new SyntaxParser(dos, parserContext);
				}
			}else{
				do {
					parser.run();
					parser = new SyntaxParser(dos, parserContext);
				}while(!condition(tree, parserContext));
			}
		}catch(IOException e){
			throw new JavaclException("IO Error", e);
		} catch (SecurityException e1){	
		} catch (NoSuchMethodException e1) {}
		
		return new String();
	}

	@Override
	public String getName() {
		return "loop";
	}

}
