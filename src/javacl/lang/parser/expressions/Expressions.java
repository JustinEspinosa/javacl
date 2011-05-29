package javacl.lang.parser.expressions;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.TreeMap;

import javacl.lang.JavaclException;
import javacl.lang.parser.ParserUtils;

public class Expressions {

	static final Map<String,ExpressionNode> cache = new TreeMap<String, ExpressionNode>();


	public static ExpressionNode parseExpression(String expr) throws JavaclException{
		expr = expr.trim();
		
		if(cache.containsKey(expr))
			return cache.get(expr);
		try{
			ExpressionNode root = parse(expr);
			cache.put(expr, root);
		
			return root;
		}catch (IOException e) {
			throw new JavaclException("IO Error",e);
		}
	}


	private static ExpressionNode attachNode(ExpressionNode currentNode, ExpressionNode newNode) throws ExpressionException{
		
		if(currentNode.getPriority() < newNode.getPriority()){
			currentNode.setChild(newNode);
			return newNode;
		}
		
		ExpressionNode branch = currentNode;
		
		while(branch.getParent().getPriority() >= newNode.getPriority())
			branch = branch.getParent();
		
		ExpressionNode p = branch.getParent();
		newNode.setChild(branch);
		p.setChild(newNode);
			
		return newNode;
	}
	
	private static ExpressionNode foundword(StringBuilder bldr,ExpressionNode current) throws ExpressionException{
		if(bldr.length()>0){
			String tok = bldr.toString();
			
			Operator o = OperatorDirectory.find(tok.trim().toLowerCase());
			if(o==null)
				current = attachNode(current, new ExpressionNode(tok.trim()));
			else
				current = attachNode(current, new ExpressionNode(o));
								
		}
		return current;
	}
	
	public static String readExpanded(Reader script) throws IOException{
		int b;
		int level = 0;
		boolean escapeState  = false;
		StringBuilder bldr = new StringBuilder();
		
		while( level > -1 && (b = script.read()) != ParserUtils.eof ){
			
			switch(b){
			case ParserUtils.escapeChar:
				escapeState = true;
				break;
			case '[':
				if(!escapeState)
					level++;
				break;
			case ']':
				if(!escapeState)
					level--;
				break;
			}
			
			if(b!=ParserUtils.escapeChar)
				escapeState = false;
			
			if(level > -1)
				bldr.append((char)b);
		}
		
		return bldr.toString();

	}
	/**
	 * The expand is not done upon parsing which will allow a loop to cache its
	 * tree. as the expression of the loop is in an enclosure.
	 * @throws ExpressionException 
	 */
	private static ExpressionNode parse(String expr) throws IOException, ExpressionException{
		Reader r = new StringReader(expr);
		int b;
		boolean escapeState = false;
		StringBuilder bldr = new StringBuilder();
		
		ExpressionNode current = new ExpressionNode(null);
		ExpressionNode root = current;
		
		while( (b = r.read()) != ParserUtils.eof ){			
			switch(b){
			case ParserUtils.escapeChar:
				escapeState = true;
				bldr.append((char)b);
				break;
			case '[':
				bldr.append((char)b);
				if(!escapeState)
					bldr.append(readExpanded(r)+"]");
				break;
			case '(':
				current = foundword(bldr, current);
				bldr = new StringBuilder();
				
				{
					Paranthesis paran = new Paranthesis();
					current = attachNode(current, new ExpressionNode(paran));
					paran.open();
				}
				
				break;
			case ')':
				current = foundword(bldr, current);
				bldr = new StringBuilder();
				
				while( !(current.getToken() instanceof Paranthesis) )
					current = current.getParent();
				
				if( current==null || current==root )
					throw new ExpressionException("Confused.");
				
				((Paranthesis)current.getToken()).close();
				
				break;
			case ' ':
				current = foundword(bldr, current);
				bldr = new StringBuilder();
				break;
			default:
				{
					if(OperatorDirectory.find(String.valueOf((char)b))!=null){
						current = foundword(bldr, current);
						bldr = new StringBuilder();
						bldr.append((char)b);
						current = foundword(bldr, current);
						bldr = new StringBuilder();
						b = r.read();
					}
				}
				if(b!=ParserUtils.eof)
					bldr.append((char)b);
				break;
			}
			
			if(b!=ParserUtils.escapeChar)
				escapeState = false;
		}
		foundword(bldr, current);
		return root;
	}
	
}
