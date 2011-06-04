package javacl.lang.parser.expressions;

public class SubtractionOperator extends NumericOperator{

	@Override
	Object execute(double n1,double n2){
		return new Double(n1 - n2);
	}
	
	@Override
	Object doEvaluate(Object o1) throws ExpressionException{
		double n1 = 0;
		
		try{
			n1 = Double.valueOf(o1.toString());
		}catch(NumberFormatException exp){}
		
		return new Double(-n1);
	}
	
	@Override
	public String toString() {
		return "-";
	}

	@Override
	public int getPriority() {
		return 35;
	}


}
