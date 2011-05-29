package javacl.lang.builtins;

import java.io.IOException;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.ParserUtils;
import javacl.lang.parser.SyntaxParser;
import javacl.lang.parser.expressions.ExpressionNode;
import javacl.lang.parser.expressions.Expressions;

public class If extends Builtin {
	
	@Override
	public String expand(Context parserContext) throws JavaclException {

		
		String expr = parserContext.getArgument().getEnclosure(ParserUtils.defaultEnclosure);
		if(expr == null)
			throw new JavaclException("Syntax not good in if");
		
		ExpressionNode tree = Expressions.parseExpression(expr);
		
		String thenc = parserContext.getArgument().getEnclosure("then");
		String elsec = parserContext.getArgument().getEnclosure("else");
		
		Object eval = tree.evaluate(parserContext);
		double result = 0;
		try{
			result = Double.valueOf(eval.toString());
		}catch(NumberFormatException exp){}
		
		SyntaxParser parser = null;

		try {
			
			if(result == 0){
				if(elsec!=null)
					parser = new SyntaxParser(elsec, parserContext);
			}else{
				if(thenc!=null)
					parser = new SyntaxParser(thenc, parserContext);			
			}
			if(parser!=null)
				parser.run();
			
		}catch (IOException e) {
			throw new JavaclException("IO Error", e);
		}catch (SecurityException e1){	
		}catch (NoSuchMethodException e1) {}
		
		return new String();
	}

	@Override
	public String getName() {
		return "if";
	}

}
