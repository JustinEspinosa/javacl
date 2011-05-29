package javacl.lang.parser.io;

import java.io.IOException;
import java.io.Reader;

public class ConsistentEolReader extends Reader {
	
	private Reader internalReader;
	private int lastChar = -1;

	public ConsistentEolReader(Reader wrapped){
		internalReader = wrapped;
	}
	
	@Override
	public void close() throws IOException {
		internalReader.close();
	}
	
	private boolean iseol(int b){
		return (b==10 || b==13);
	}
	
	@Override
	public int read() throws IOException {
		int b = internalReader.read();
		
		if(iseol(b) && iseol(lastChar) && b!=lastChar)
			 b = internalReader.read();
		
		lastChar = b;
		
		if(iseol(b))
			b = (int)'\n';
		
		return b;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int n=0;
		for(n=0;n<len;n++){
			int b = read();
			if(b != -1)
				cbuf[off+n] = (char)b;
		}
		return n;
	}

}
