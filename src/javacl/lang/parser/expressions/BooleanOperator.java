package javacl.lang.parser.expressions;

public abstract class BooleanOperator extends NumericOperator {

	@Override
	Object execute(double n1, double n2) {
		boolean b1 = (n1!=0?true:false);
		boolean b2 = (n2!=0?true:false);
		
		return boolcomp(b1,b2);
	}

	abstract Object boolcomp(boolean b1, boolean b2);

}
