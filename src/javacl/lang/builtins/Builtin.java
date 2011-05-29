package javacl.lang.builtins;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.ExpandVariable;
import javacl.lang.parser.VariableType;

public abstract class Builtin implements VariableType, ExpandVariable {

	@Override
	public abstract String expand(Context parserContext) throws JavaclException;
	/**
	 * Does not include the hash
	 * @return the name without the #
	 */
	public String getName(){
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	@Override
	public String toString() {
		return "#"+getName();
	}
}
