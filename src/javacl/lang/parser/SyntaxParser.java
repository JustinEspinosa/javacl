package javacl.lang.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;

import javacl.lang.JavaclException;
import javacl.lang.environment.EnvironmentState;
import javacl.lang.environment.EnvironmentUtils;
import javacl.lang.environment.FsUtils;
import javacl.lang.parser.io.EofException;
import javacl.lang.parser.io.ExpanderReader;

public class SyntaxParser {
	
	private Context         taclContext;
	private ExpanderReader  input;
	private Writer          terminal;
	private int             interractiveCmdNo = 1;

	
	private SyntaxParser(ExpanderReader r, Writer w, Context c, boolean callback) throws SecurityException, NoSuchMethodException, JavaclException{
		taclContext = c;
		input = r;
		terminal = w; 
		if(callback)
			input.setPromptCallback(new Callback(this, SyntaxParser.class.getMethod("prompt")));
		if(terminal!=null)
			initshell();
	
	}
	private SyntaxParser(InputStream is, OutputStream os, Context c, boolean cb) throws SecurityException, NoSuchMethodException, JavaclException{
		this(new ExpanderReader(new InputStreamReader(is), c), new PrintWriter(os), c, cb);
	}
	public SyntaxParser(String text, Context c) throws SecurityException, NoSuchMethodException, JavaclException{
		this(new ExpanderReader(new StringReader(text), c), null, c, false);
	}
	
	public SyntaxParser(InputStream is, Context c) throws SecurityException, NoSuchMethodException, JavaclException{
		this(new ExpanderReader(new InputStreamReader(is), c), null,c, false);
	}
	
	public SyntaxParser(InputStream is, OutputStream os) throws SecurityException, NoSuchMethodException, JavaclException{
		this(is,os,(new EnvironmentState()).newContext(),true);
	}
	
	public SyntaxParser(InputStream is) throws SecurityException, NoSuchMethodException, JavaclException{
		this(is, (new EnvironmentState()).newContext());
	}
	

	private void initshell() throws JavaclException{
		System.out.println("JAVACL.");
		File f = new File("/etc/javacl");
		System.out.println("Running "+f.getAbsolutePath());
		if(f.exists())
			EnvironmentUtils.run(f,new Argument(""), taclContext);
		else
			System.out.println("not found.");
		
		f = new File(FsUtils.getDefaults(true),"javacl");
		System.out.println("Running "+f.getAbsolutePath());
		if(f.exists())
			EnvironmentUtils.run(f,new Argument(""), taclContext);
		else
			System.out.println("not found.");
		
	}
	
	public void run() throws JavaclException, IOException{
		
		try{
			while(true){
				Instruction inst = input.readBlock();
				if(inst!=null){
					String exp = inst.execute();
					if(exp!=null && exp.trim().length()>0)
					System.out.println("\nExpanded to:\n"+exp+"\n");
				}
			}
		}catch(EofException e){
		}
	}
	
	public void prompt() throws IOException, JavaclException{
		if(terminal!=null){
			VariableType prompter = ParserUtils.findVariable(taclContext, "_prompter");
			if(prompter instanceof ExpandVariable)
				((ExpandVariable) prompter).expand(taclContext);
			terminal.write(taclContext.getEnv().getPrefix());
			terminal.write(String.valueOf(interractiveCmdNo)+">");
			terminal.flush();
		}
	}
	
	public void runInteractive() throws IOException, JavaclException{
		
		if(terminal==null)
			throw new JavaclException("Sorry.");
		
		while(!taclContext.getEnv().getExitFlag()){
			try{
				prompt();
				Instruction inst = input.readBlock();
				if(inst!=null){
					interractiveCmdNo++;
					String exp = inst.execute();
					if(exp!=null && exp.trim().length()>0)
						System.out.println("\nExpanded to:\n"+exp+"\n");
				}
			}catch(JavaclException e){
				if(e instanceof EofException)
					throw e;
				
				System.out.println("*** JAVACL Exception:");
				System.out.println("***   "+e.getClass().getSimpleName());
				System.out.println("***     "+e.getMessage());
				e.displayTrace();
			}
		}
	}
	
}

