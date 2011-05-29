package javacl.lang.builtins;

import java.io.File;
import java.util.Date;

import javacl.lang.JavaclException;
import javacl.lang.environment.FsUtils;
import javacl.lang.parser.Context;

public class XFileInfo extends Builtin {

	private static String fill(char c, int len){
		StringBuilder builder = new StringBuilder();
		while(builder.length()<len)
			builder.append(c);
		
		return builder.toString();
	}

	private static String fixedLen(long n, int len){
		return fixedLen(n, len, ' ');
	}
	private static String fixedLen(String str, int len){
		return fixedLen(str, len, ' ');
	}
	private static String fixedLen(long n, int len, char fill){
		return fixedLen(String.valueOf(n), len, fill);
	}
	private static String fixedLen(String str, int len, char fill){
		if(str.length()>len)
			return str.substring(0,len);
		
		return str + fill(fill, len-str.length());
	}
	private static void display(StringBuilder b){
		display(b.toString());
	}
	private static void display(String s){
		System.out.println(s);
	}
	private static void showHead(){
		StringBuilder line = new StringBuilder();
		line.append(fixedLen("Name", 32));      //0
		line.append(fixedLen("Eof", 16));       //32
		line.append(fixedLen("Mod. Date", 20)); //48
		
		display(line);
	}
	
	private static void showOne(File f){
		StringBuilder line = new StringBuilder();
		line.append(fixedLen(f.getName(), 31));    //0
		line.append(' ');                          //31
		if(f.isDirectory())
			line.append(fixedLen("Subvolume", 15));//32
		else
			line.append(fixedLen(f.length(), 15)); //32
		line.append(' ');                          //47
		String lmt = (new Date(f.lastModified())).toString();
		line.append(fixedLen(lmt, 20));            //48
		display(line);
	}

	
	@Override
	public String expand(Context parserContext) throws JavaclException {
		
		File[] files = FsUtils.getFiles(FsUtils.getDefaults(false), parserContext.getArgument().getArgument());
		
		showHead();
		display("");
		
		for(File f:files)
			showOne(f);
		
		display("");
		display(files.length + " item(s) found.");
		
		return new String();
	}

	@Override
	public String getName() {
		return "xfileinfo";
	}

}
