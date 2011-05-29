package javacl.lang.parser.expressions;

public class Paranthesis {
	private boolean isClosed = true;
	
	public void open(){ isClosed = false; }
	public void close(){ isClosed = true; }
	public boolean isClosed(){ return isClosed; }
	
	public int getPriority(){
		if(isClosed)
			return 100;
		else
			return 0;
	}
}
