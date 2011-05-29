package javacl.lang.parser.expressions;

import java.util.Map;
import java.util.TreeMap;

public class OperatorDirectory {
	private static Map<String, Operator> operators = new TreeMap<String, Operator>();
	
	static{
		registerOperator(new GreaterThanOperator());
		registerOperator(new SmallerThanOperator());
		registerOperator(new EqualsToOperator());
		registerOperator(new AndOperator());
		registerOperator(new OrOperator());
		registerOperator(new NotOperator());
		registerOperator(new AdditionOperator());
		registerOperator(new SubtractionOperator());
		registerOperator(new MultiplyOperator());
		registerOperator(new DivideOperator());
	}
	
	public static void registerOperator(Operator op){
		operators.put(op.toString(),op);
	}
	
	public static Operator find(String op){
		return operators.get(op);
	}
}
