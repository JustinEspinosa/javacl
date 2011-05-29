package javacl.lang.parser.expressions;


import java.io.IOException;

import javacl.lang.JavaclException;
import javacl.lang.parser.Context;
import javacl.lang.parser.ParserUtils;

public class ExpressionNode {
	private ExpressionNode parent = null;
	private ExpressionNode left = null;
	private ExpressionNode right = null;
	private Object token;
	
	public ExpressionNode(Object tkn){
		token = tkn;
	}
	
	public Object getToken(){
		return token;
	}
	
	
	public ExpressionNode getParent(){
		return parent;
	}
	public ExpressionNode getRight(){
		return right;
	}
	public ExpressionNode getLeft(){
		return left;
	}	
	private void setParent(ExpressionNode p){
		if(parent!=null){
			if(parent.getLeft()==this)
				parent.setLeft(null);
			if(parent.getRight()==this)
				parent.setRight(null);
		}
		parent = p;
	}
	
	private void setRight(ExpressionNode r){
		right = r;
		if(r!=null)
			r.setParent(this);
	}
	
	private void setLeft(ExpressionNode l){
		left = l;
		if(l!=null)
			l.setParent(this);
	}
	
	public void setChild(ExpressionNode n) throws ExpressionException{
		if(left==null){
			setLeft(n);
			return;
		}

		if(right==null){
			setRight(n);
			return;
		}

		throw new ExpressionException("Syntax error");
	}
	
	public Object evaluate(Context c) throws JavaclException{
		try{
			if(token instanceof String)
				return ParserUtils.expand(c,(String)token);
			
			if(token instanceof Operator)
				return ((Operator)token).evaluate(left!=null?left.evaluate(c):null, right!=null?right.evaluate(c):right);
		
			if(left != null)
				return left.evaluate(c);
		}catch (IOException e) {
			throw new JavaclException("IO Error", e);
		}
		throw new ExpressionException("Cannot be evaluated");
	}
	
	public int getPriority(){
		if(token == null)
			return 0;
		
		if(token instanceof Operator)
			return ((Operator) token).getPriority();
		
		if(token instanceof Paranthesis)
			return ((Paranthesis) token).getPriority();
		
		return 100;
	}

}
