package javacl.lang.parser.expressions;

public abstract class Operator {
	public final Object evaluate(Object o1,Object o2) throws ExpressionException{
		Object r = null;
		
		if(o1 == null && o2 == null)
			throw new ExpressionException("Invalid number of operands");
		
		if(o1 != null && o2 == null)
			r = doEvaluate(o1);

		if(o1 == null && o2 != null)
			r = doEvaluate(o2);
		
		if(o1 != null && o2 != null)
			r = doEvaluate(o1,o2);
		
		if(r instanceof Boolean)
			return (((Boolean) r).booleanValue()?new Integer(-1):new Integer(0));
		if(r instanceof Double){
			if(((Double) r).doubleValue() == ((Double) r).longValue())
				return new Long(((Double) r).longValue());
		}
		return r;
	}
	Object doEvaluate(Object o1,Object o2) throws ExpressionException{
		throw new ExpressionException("Invalid number of operands");
	}
	Object doEvaluate(Object o) throws ExpressionException{
		throw new ExpressionException("Invalid number of operands");
	}
	public abstract int getPriority();
}
