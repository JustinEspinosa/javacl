package javacl.lang.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Simple callback for no-arg and void
 * @author tkes3
 *
 */
public class Callback {
	private Object targetInst;
	private Method targetMethod;
	
	public Callback(Object o, Method m){
		targetInst = o;
		targetMethod = m;
	}
	
	public void call() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		targetMethod.invoke(targetInst);
	}
}
