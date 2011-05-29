package javacl.lang.parser.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javacl.lang.parser.io.ConsistentEolReader;

public class PusherReader extends ConsistentEolReader {

	private PusherReader pushedData = null;
	
	public PusherReader(Reader wrapped) {
		super(wrapped);
	}

	public void push(String push){
		if(pushedData==null)
			pushedData = new PusherReader(new StringReader(push));
		else
			pushedData.push(push);
	}
	
	@Override
	public int read() throws IOException {
		if(pushedData!=null){
			int r = pushedData.read();
			if(r==-1){
				pushedData = null;
				return super.read();
			}
			return r;
		}
		return super.read();
	}
}
