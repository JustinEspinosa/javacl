package javacl.lang.parser;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;

import javacl.lang.JavaclException;
import javacl.lang.environment.EnvironmentUtils;
import javacl.lang.environment.FileSystemException;
import javacl.lang.environment.FsUtils;

public class Instruction {
	private String  command;
	private Argument argument;
	private Context  ctx;
	
	public Instruction(String cmd, Argument arg, Context c){
		command  = cmd.trim().toLowerCase();
		argument = arg;
		ctx = c;
	}
	
	public String getCommand(){
		return command;
	}
	
	public Argument getArgument(){
		return argument;
	}
	
	/**
	 * RUN is a special construct. 
	 * It is not a variable, nor a '#'ized builtin.
	 * It always takes over a #push-ed run.
	 * @return
	 * @throws JavaclException
	 */
	private String run() throws JavaclException{
		
		String argument = getArgument().toString();
		Matcher m = ParserUtils.word.matcher(argument);
		if(m.find()){
			String restArgument = argument.substring(m.end()).trim();
			
			RunOptionsReader reader = new RunOptionsReader(restArgument);
			Map<String,String> runOpt = reader.getOptions();
			restArgument = reader.getRemainingBytes();
			
			Variable outv = null;
			if(runOpt.containsKey("outv")){
				VariableType v = ParserUtils.findVariable(ctx, runOpt.get("outv"));
				if(v instanceof Variable)
					outv = (Variable)v;
			}
			
			File f = new File(m.group());
			if(!f.isAbsolute())
				f = new File(FsUtils.getDefaults(false),m.group());
			
			if(!f.exists())
				f = EnvironmentUtils.findExecutable(ctx.getEnv().getPmSearchList(), m.group());
			
			if(f!=null){
				EnvironmentUtils.run(f, new Argument(restArgument), ctx, outv);
				return new String();
			}
		}
		throw new FileSystemException("File not found");
	}
	
	public String execute() throws JavaclException{
		
		if(command.startsWith("?"))
			return new String();
		if(command.equals("run"))
			return run();

		VariableType var = ParserUtils.findVariable(ctx, getCommand());
		if(var instanceof ExpandVariable)
			return ((ExpandVariable) var).expand(ctx.fork(getArgument()));
		
		
		File f = EnvironmentUtils.findExecutable(ctx.getEnv().getPmSearchList(), command);
		if(f!=null){
			EnvironmentUtils.run(f, argument, ctx);
			return new String();
		}
		
		throw new RuntimeException(getCommand()+" is not a variable, nor a runnable file in #pmsearchlist");
	}

}
