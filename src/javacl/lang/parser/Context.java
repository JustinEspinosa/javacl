package javacl.lang.parser;

import javacl.lang.environment.EnvironmentState;

public interface Context {
	
	public Argument         getArgument();
	public EnvironmentState getEnv();
	public RoutineContext   getRoutineContext();
	public Context          fork(Argument arg);
	public void             createRoutineContext();

}
