package javacl.lang.parser.expressions;

public abstract class NumericOperator extends Operator{

	@Override
	Object doEvaluate(Object o1, Object o2) throws ExpressionException{
		double n1 = 0, n2 = 0;
		
		try{
			n1 = Double.valueOf(o1.toString());
		}catch(NumberFormatException exp){}
		try{
			n2 = Double.valueOf(o2.toString());
		}catch(NumberFormatException exp){}
		
		return execute(n1, n2);
	}
	
	abstract Object execute(double n1,double n2);


}
