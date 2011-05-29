package javacl.lang.parser.expressions;

public class SmallerThanOperator extends NumericOperator{
	
	@Override 
	Object execute(double n1,double n2){
		return new Boolean(n1 < n2);
	}
	
	@Override
	public String toString() {
		return "<";
	}

	@Override
	public int getPriority() {
		return 30;
	}
	
}
