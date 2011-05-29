package javacl.lang.parser.expressions;

public class OrOperator extends BooleanOperator {

	@Override
	Object boolcomp(boolean b1, boolean b2) {
		return new Boolean(b1 || b2);
	}

	@Override
	public int getPriority() {
		return 25;
	}
	
	@Override
	public String toString() {
		return "or";
	}

}
