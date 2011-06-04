package javacl.lang.environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Vector;

import javacl.lang.JavaclException;
import javacl.lang.parser.AppendVariable;
import javacl.lang.parser.Argument;
import javacl.lang.parser.Context;
import javacl.lang.parser.ExpandVariable;
import javacl.lang.parser.Macro;
import javacl.lang.parser.Routine;
import javacl.lang.parser.RuntimeException;
import javacl.lang.parser.Variable;
import javacl.lang.parser.WriteVariable;
import javacl.lang.parser.io.ArgumentReader;
import javacl.lang.parser.io.CmdArgumentReader;

public class EnvironmentUtils {

	
	public static String getEnv(String name){
		return System.getenv(name);
	}
	private static String get1stLine(File f) throws FileNotFoundException{
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String line1 = r.readLine();
			return line1;
		} catch (IOException e) {}
		return new String();
	}
	
	private static Class<? extends Variable> getFileType(File f){
		try{
			if(get1stLine(f).toLowerCase().startsWith("?tacl macro"))
				return Macro.class;
			if(get1stLine(f).toLowerCase().startsWith("?tacl routine"))
				return Routine.class;
		}catch(FileNotFoundException e){}
		return null;
	}
	
	private static boolean checkFile(File f, String text) throws FileNotFoundException{
		return get1stLine(f).toLowerCase().startsWith(text);
	}
	
	public static boolean isTaclRoutine(File f){
		try{
			return checkFile(f, "?tacl routine ");
		}catch(FileNotFoundException e){
			return false;
		}
	}
	
	public static boolean isTaclMacro(File f){
		try{
			return checkFile(f, "?tacl macro ");
		}catch(FileNotFoundException e){
			return false;
		}
	}
	
	public static boolean isTacl(File f) {
		try {
			return checkFile(f, "?tacl ");
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	public static File findExecutable(Vector<String> pmsl, String name){
		for(String pn : pmsl){
			File f = new File(pn,name);
				if(f.exists() && (f.canExecute() || isTacl(f)) )
					return f;
			
		}
		return null;
	}
	
	public static String load(File f) throws FileSystemException{
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader b = new BufferedReader(new FileReader(f));
			String line;
			while( (line=b.readLine())!=null){
				builder.append(line);
				builder.append('\n');
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			throw new FileSystemException("IO Error", e);
		}

		return builder.toString();
	}
	
	public static void runFile(File f,String arg, Context ctx) throws JavaclException{
		Class<? extends Variable> fType = getFileType(f);
		if(fType!=null){
			try {
				Variable ftr = fType.newInstance();
				if(ftr instanceof WriteVariable)
					((WriteVariable) ftr).set(load(f), ctx);
				
				if(ftr instanceof ExpandVariable)
					((ExpandVariable) ftr).expand(ctx.fork(new Argument(arg)));
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {}
			return;
		}
		throw new RuntimeException("Tacl file format unknown");
	}
	
	public static void executeProgram(File f, String arg, Context ctx, Variable outv) throws JavaclException{
		ProcessBuilder pb = new ProcessBuilder(new ArrayList<String>());
		StringBuilder builder = new StringBuilder();
		
		pb.command().add(f.getAbsolutePath());
		
		try{
			ArgumentReader r1 = new CmdArgumentReader(new StringReader(arg));
			r1.read();
			while(!r1.eof())
				pb.command().add(r1.readToken());
		}catch(IOException e){}
		
		pb.directory(new File(FsUtils.getDefaults(false)));
		pb.redirectErrorStream(true);
		
		//TODO Completion structure
		try {
			Process prs = pb.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(prs.getInputStream()));
			String line;
			
			while((line = r.readLine())!=null)
				if(outv==null)
					System.out.println(line);
				else{
					builder.append(line);
					builder.append('\n');
				}
			prs.waitFor();
		} catch (InterruptedException e) {
			throw new RuntimeException("Abend",e);
		} catch (IOException e) {
			throw new RuntimeException("Process creation failure",e);
		} finally {
			if(outv instanceof AppendVariable)
				((AppendVariable)outv).append(builder.toString(),ctx);
		}
		
	}
	
	public static void run(File f, Argument arg, Context ctx) throws JavaclException{
		run(f,arg,ctx,null);
	}
	
	public static void run(File f, Argument arg, Context ctx, Variable outv) throws JavaclException{
		if(isTacl(f))
			runFile(f, arg.getArgument().toString(), ctx);
		else		
			executeProgram(f, arg.getArgument().toString(), ctx, outv);
	}
	
	
}
