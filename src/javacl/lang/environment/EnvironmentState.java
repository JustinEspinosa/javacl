package javacl.lang.environment;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javacl.lang.JavaclException;
import javacl.lang.parser.Argument;
import javacl.lang.parser.Context;
import javacl.lang.parser.Directory;
import javacl.lang.parser.ParserUtils;
import javacl.lang.parser.RoutineContext;
import javacl.lang.parser.Text;
import javacl.lang.parser.Variable;
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
    private Vector<Directory> useList = new Vector<Directory>();
	private Directory home;
	private Directory root = new Directory(new VariableStack(null,""));
	
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
	
	public Vector<Directory> getUselist(){
		return useList;
	}
	
	public String getUselistAsString(){
		return ParserUtils.spaceList(useList.toArray());
	}
	
	public void setUseList(String ulAsStr) throws JavaclException{
		useList.clear();
		
		Reader r = new StringReader(ulAsStr);
		StringBuilder w = new StringBuilder();
		int c = 0;
		
		try {
			while( c != -1 ){
				c = r.read();
				if( c==-1 || c==' ' ){
					Variable v = findVariable(w.toString());
					if(v instanceof Directory)
						useList.add((Directory)v);
					else
						throw new JavaclException(w.toString()+" is not a directory.");
					w = new StringBuilder();
				}else
					w.append((char)c);
			}
		} catch (IOException e) {/*Never happens on strings*/}
	}
	
	public void setHome(Directory d){
		home = d;
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
		return ParserUtils.spaceList(pmSearchList.toArray());
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
	
	public String variablePath(String name){
		return variablePath(getHome(),name);
	}
	
	public String variablePath(Directory from,String name){
		if(name.startsWith(":"))
			return name;
		
		return from.fullPath()+name;
	}
	
	public String variableName(String fullpath){
		return fullpath.substring(fullpath.lastIndexOf(':')+1);
	}

	public  Variable push(String name) throws JavaclException{
		return push(name,Text.class);
	}
	public  Variable push(String name, Class<? extends Variable> cl) throws JavaclException{
		String fp = variablePath(name);
		Variable dir = getVariable(fp, true);
		if(dir instanceof Directory)
			return ((Directory) dir).push(variableName(fp), cl);
		throw new JavaclException("Impossible path.");
	}
	
	private Variable getVariable(String fullpath) throws JavaclException{
		return getVariable(fullpath,false);
	}
	
	private Variable getVariable(String fullpath, boolean pushpath) throws JavaclException{
		if(!fullpath.startsWith(":"))
			return null;

		
		String    pname = "";
		Directory parent = getRoot();
		Variable  v = parent;
		
		StringBuilder builder = new StringBuilder();
		StringReader  r = new StringReader(fullpath);
		int c = 0;
		
		try{
			while( c !=-1 ){
				c = r.read();
				if(c == ':' || c==-1){
					if(builder.length()>0){
						if(pushpath){
						    if(v == null)
						    	v = parent.push(pname, Directory.class);
						    if(v instanceof Directory)
						    	parent = (Directory)v;
						    
							pname  = builder.toString();
						}
						
						if(v instanceof Directory){
							v = ((Directory) v).getChild(builder.toString());							
							builder = new StringBuilder();
						}else{
							return null;
						}
					}
				}else{
					builder.append((char)c);
				}
			}
		}catch(IOException e){
			return null;
		}
		
		if(v==null && pushpath)
			return parent;
		
		return v;
	}
	
	public Variable findVariable(String name) throws JavaclException{
		String path = variablePath(name);
		Variable v = getVariable(path);
		if(v!=null)
			return v;
		
		Vector<Directory> dlist = getUselist();
		for(Directory dir : dlist){
			path = variablePath(dir, name);
			v = getVariable(path);
			if(v!=null)
				return v;
		}
		return null;
	}
	
	
	public Context newContext(){
		Map<String, String> map = new TreeMap<String,String>();
		map.put(ParserUtils.defaultEnclosure, "");
		return new ContextImpl(new Argument(map));
	}
}

