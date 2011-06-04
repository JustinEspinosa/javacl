package javacl.lang.parser.io;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class ArgumentReader extends ConsistentEolReader {

	//sort manually (or in static init if you want)
	protected static final char[] separator = new char[]{'\t','\n','\r',' '};
	
	private static boolean wordChar(int c){
		return (c>0x2F && c<0x3A) || (c>0x40 && c<0x5B) || (c>0x60 && c<0x7B);
	}
	
	private int currentChar  = -2;
	private boolean consumed = true;
	
	public ArgumentReader(Reader wrapped) {
		super(wrapped);
	}
	
	protected void consume(){
		consumed = true;
	}
	
	public int readAndConsume() throws IOException{
		read();
		consume();
		return currentChar;
	}
	@Override
	public int read() throws IOException {
		if(consumed)
			currentChar = super.read();
		consumed = false;
		return currentChar;
	}
	
	protected final int current(){
		return currentChar;
	}

	public boolean eof(){
		return currentChar == -1;
	}
	
	public String readToken() throws IOException{
		StringBuilder builder = new StringBuilder();

		while( read()!=-1){
			if(Arrays.binarySearch(separator, (char)currentChar) > -1 && builder.length()>0){
				consume();
				return builder.toString();
			}else{
				if(wordChar(currentChar)){
					builder.append((char)currentChar);
					consume();
				}else{
					if(builder.length()>0){
						return builder.toString();
					}else{
						builder.append((char)currentChar);
						consume();
						return builder.toString();
					}
				}
					
			}
		}
		
		return builder.toString();
	}
}
