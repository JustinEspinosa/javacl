package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.expressions.ExpressionNode;
import javacl.lang.parser.expressions.Expressions;

public class Compute extends Builtin {
	
	@Override
	public String expand(Context parserContext) throws JavaclException {

		ExpressionNode tree = Expressions.parseExpression(parserContext.getArgument().toString());
		
		Object eval = tree.evaluate(parserContext);
		
		return eval.toString();
	}

	@Override
	public String getName() {
		return "compute";
	}

}
