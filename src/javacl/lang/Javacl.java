package javacl.lang;


import javacl.lang.parser.SyntaxParser;

public class Javacl {
	
	public static void main(String[] args){
		
		try {
			SyntaxParser javaclParser = new SyntaxParser(System.in,System.out);
			javaclParser.runInteractive();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
