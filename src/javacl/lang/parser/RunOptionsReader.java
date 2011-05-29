package javacl.lang.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.TreeMap;

import javacl.lang.parser.io.ArgumentReader;

public class RunOptionsReader {
	
	private ArgumentReader reader;
	private StringBuilder  remains = new StringBuilder();
	
	public RunOptionsReader(String arg){
		reader = new ArgumentReader(new StringReader(arg));
	}
	
	public Map<String, String> getOptions(){
		Map<String, String> args = new TreeMap<String, String>();
		try{
			String a = null,b = null;
			String token = reader.readToken();
			if(token.equals("/")){
				while(!(token = reader.readToken()).equals("/")){
					if(token.equals(",")){
						if(b==null)
							b = new String();
						if(a!=null)
							args.put(a.toLowerCase(), b);
						a=null;
						b=null;
					}else{
						if(a == null)
							a = token;
						else
							b = token;
					}
				}
				if(b==null)
					b = new String();
				if(a!=null)
					args.put(a.toLowerCase(), b);
			}else{
				remains.append(token);
			}
		}catch(IOException e){}
		return args;
	}
	public String getRemainingBytes(){
		int c;
		
		try {
			while((c=reader.readAndConsume())!=-1){
				remains.append((char)c);
			}
		} catch (IOException e) {}
		
		return remains.toString().trim();
	}
}
