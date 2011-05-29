package javacl.lang.environment;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javacl.lang.parser.Argument;
import javacl.lang.parser.Context;
import javacl.lang.parser.Directory;
import javacl.lang.parser.ParserUtils;
import javacl.lang.parser.RoutineContext;
import javacl.lang.parser.VariableStack;

public class EnvironmentState {
	
	private class ContextImpl implements Context{
		private Argument argument;
		private RoutineContext rc = null;
		
		private ContextImpl(Argument arg){
			argument = arg;
		}

		@Override
		public EnvironmentState getEnv(){
			return EnvironmentState.this;
		}
		
		@Override
		public Argument getArgument() {
			return argument;
		}
		
		@Override
		public Context fork(Argument arg){
			ContextImpl r = new ContextImpl(arg);
			r.rc = rc;
			return r;
		}

		@Override
		public RoutineContext getRoutineContext() {
			return rc;
		}

		@Override
		public void createRoutineContext() {
			rc = new RoutineContext(getArgument());
		}
	}
	
	private String prefix = "";
	private boolean exitflag = false;
	private Vector<String> pmSearchList = new Vector<String>();
    private Vector<Directory> searchList = new Vector<Directory>();
	private Directory home;
	private Directory root = new Directory(new VariableStack(":"));
	
	public EnvironmentState(){
		home = root;
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	public void setPrefix(String prfx){
		prefix = prfx.trim();
		if(prefix.length()>0)
			prefix += " ";
	}
	
	public Vector<Directory> getSearchList(){
		return searchList;
	}
	
	public Directory getHome(){
		return home;
	}
	
	public Directory getRoot(){
		return root;
	}
	
	public boolean getExitFlag(){
		return exitflag;
	}
	
	public void setExitFlag(boolean exit){
		exitflag = exit;
	}
	public Vector<String> getPmSearchList(){
		return pmSearchList;
	}
	public String getPmSearchListAsString(){
		StringBuilder builder = new StringBuilder();
		for(String path : pmSearchList){
			builder.append(path);
			if(path != pmSearchList.lastElement())
				builder.append(' ');
		}
		return builder.toString();
	}
	
	private StringBuilder gotASubvol(StringBuilder w){
		pmSearchList.add(w.toString());
		return new StringBuilder();
	}
	
	public void setPmSearchList(String slAsStr){
		pmSearchList.clear();
		
		Reader r = new StringReader(slAsStr);
		StringBuilder w = new StringBuilder();
		int c; boolean skip = false;
		
		try {
			while( (c = r.read()) != -1 ){
				if( c=='\"' ) skip = !skip;
				if( !skip && c==' ' )
					w = gotASubvol(w);
				else
					w.append((char)c);
			}
		} catch (IOException e) {/*Never happens on strings*/}
		gotASubvol(w);
	}
	
	public Context newContext(){
		Map<String, String> map = new TreeMap<String,String>();
		map.put(ParserUtils.defaultEnclosure, "");
		return new ContextImpl(new Argument(map));
	}
}

