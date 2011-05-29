package javacl.lang.environment;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FsUtils {
	
	private static String cwd;
	
	public static class TemplateFilenameFilter implements FilenameFilter{
		private Pattern nameRegex;
		
		public TemplateFilenameFilter(String tmpl){
			tmpl = tmpl.replaceAll("(\\*|\\?)", ".$1");
			nameRegex = Pattern.compile(tmpl);
		}
		
		@Override
		public boolean accept(File f, String name) {
			if(nameRegex.pattern().length()==0)
				return true;
			
			Matcher m = nameRegex.matcher(name);
			return m.matches();
		}
		
	}
	
	static{
		cwd = getSysCwd();
	}
	
	
	private static String getSysCwd(){
		return (new File("")).getAbsolutePath();
	}
	
	private static String getHome(){
		return System.getProperty("user.home");
	}	
	
	public static String getDefaults(boolean saved){
		if(saved)
			return getHome();
		
		return cwd;
	}
	
	public static void setDefaults(String path) throws FileSystemException{
		
		File f = new File(path);
		if(!f.isAbsolute())
			f = new File(cwd,path);
		try{
			if(f.exists() && f.isDirectory())
				cwd = f.getCanonicalPath();
			else
				throw new FileSystemException(path+" is not an existing subvolume");
		}catch(IOException e){
			throw new FileSystemException("IO Error", e);			
		}
	}
	
	public static File[] getFiles(String path, String template) throws FileSystemException{
		File dir = new File(path);
		
		if(dir.isDirectory()){
			return dir.listFiles(new TemplateFilenameFilter(template));
		}else{
			//  ;-P
			throw new FileSystemException("Fs Error -83 IYF!");
		}
		
	}

}
