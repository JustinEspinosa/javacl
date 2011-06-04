package javacl.lang.parser.io;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class CmdArgumentReader extends ArgumentReader{
	private boolean stop;
	private boolean start;
	private boolean quote;
	
	public CmdArgumentReader(Reader wrapped) {
		super(wrapped);
		resetState();
	}

	private void resetState(){
		stop  = false;
		start = true;
		quote = false;
	}
	
	private boolean detectSeparator(){
		if(!quote && Arrays.binarySearch(separator, (char)current()) > -1){
			if(!start)
				stop = true;
			return false;
		}
		return true;
	}
	
	private boolean detectQuote(){
		if(current()=='"')
			quote = !quote;
		return true;
	}
	
	private boolean detectEof(){
		if(current()==-1){
			stop = true;
			return false;
		}
		return true;
	}
	
	@Override
	public String readToken() throws IOException {
		StringBuilder builder = new StringBuilder();
		
		while(!stop){
			read();
			if(detectSeparator())
			if(detectQuote()    )
			if(detectEof()      ){
				if(start) start = false;
				builder.append((char)current());
			}
			consume();
		}
		
		resetState();
		
		return builder.toString().trim();
	}
}
