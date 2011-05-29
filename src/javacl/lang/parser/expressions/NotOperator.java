package javacl.lang.parser.expressions;

public class NotOperator extends Operator{

	@Override
	Object doEvaluate(Object o1) throws ExpressionException{
		double n1 = 0;
		
		try{
			n1 = Double.valueOf(o1.toString());
		}catch(NumberFormatException exp){}
		
		return new Boolean( (n1!=0?false:true) );
	}
	
	@Override
	public int getPriority() {
		return 27;
	}

	@Override
	public String toString() {
		return "not";
	}

}
