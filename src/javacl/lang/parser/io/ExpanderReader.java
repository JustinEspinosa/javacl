package javacl.lang.parser.io;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javacl.lang.JavaclException;
import javacl.lang.parser.Argument;
import javacl.lang.parser.Callback;
import javacl.lang.parser.Context;
import javacl.lang.parser.Instruction;
import javacl.lang.parser.ParserUtils;

public class ExpanderReader extends PusherReader {
	
	private static class ResetSwitch{
		private boolean defVal = false;
		private boolean value = defVal;
		private boolean reset = false;
		private ResetSwitch(boolean defaultValue){
			defVal = defaultValue;
			value  = defVal;
		}		
		private ResetSwitch(){
		}
		private void set(){
			value = !defVal;
		}
		private boolean get(){
			return value;
		}
		private void reset(){
			if(reset){
				value = defVal;
				reset = false;
			}else{
				if(value != defVal)
					reset = true;
			}
		}
	}
	
	/*
	 * Will I ever be able to write a parser that uses less variables...
	 */
	private boolean             isBracketed = false;
	private Context             expandContext = null;
	private Callback            promptCallBack = null;
	
	private boolean             startup;
	private boolean             stop;
	private int                 bracketLevel;
	private boolean             seenSeparator;
	private boolean             seenEnclosure;
	private boolean             openEnclosure;
	private boolean             commented;
	private ResetSwitch         escapeNext;
	private ResetSwitch         eolBreak;
	private ResetSwitch         comment;
	private int                 currentChar;
	private StringBuilder       builder;
	private String              enclosure;
	private String              command;
	private Map<String, String> argEnclosures;

	public ExpanderReader(Reader wrapped, Context expCtx) {
		this(wrapped,expCtx,false, null);
	}

	public ExpanderReader(Reader wrapped, Context expCtx, boolean bracketed) {
		this(wrapped,expCtx,bracketed, null);
	}
	
	public ExpanderReader(Reader wrapped, Context expCtx, Callback prompt) {
		this(wrapped,expCtx,false, prompt);
	}
	
	public ExpanderReader(Reader wrapped, Context expCtx, boolean bracketed, Callback prompt) {
		super(wrapped);
		expandContext  = expCtx;
		isBracketed    = bracketed;
		promptCallBack = prompt;
		resetParserState();
	}
	
	public void setPromptCallback(Callback cb){
		promptCallBack = cb;
	}
	
	private void resetParserState(){
		startup = true;
		stop = false;
		bracketLevel = 0;
		seenSeparator = false;
		seenEnclosure = false;
		openEnclosure = false;
		escapeNext = new ResetSwitch();
		eolBreak   = new ResetSwitch();
		comment    = new ResetSwitch();
		currentChar = -1;
		builder = null;
		enclosure = ParserUtils.defaultEnclosure;
		command = null;
		argEnclosures = new TreeMap<String, String>();
	}
	
	private boolean detectResetSwitch(char c, ResetSwitch sw){
		if(currentChar == c){
			sw.set();
			return false;
		}
		return true;
	}
	
	private boolean detectEolBreak(){
		return detectResetSwitch('&', eolBreak);
	}
	
	private boolean detectEscape(){
		return detectResetSwitch('~', escapeNext);
	}
	
	private boolean detectEol(){
		if(currentChar == '\n'){
			commented = false;
			
			if(!eolBreak.get() && !isBracketed){
				commandFound();
				stop = true;
				return false;
			}
			if(promptCallBack!=null){
				try {
					promptCallBack.call();
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {}
			}
		}
		return !eolBreak.get();
	}
	
	private boolean detectLeftBracket() throws JavaclException, IOException{
		if(currentChar == '[' && !escapeNext.get()){
			if(seenEnclosure){
				bracketLevel++;
				return true;
			}
			
			ExpanderReader expander = new ExpanderReader(this, expandContext,true, promptCallBack);
			push(expander.readBlock().execute());
			return false;
		}
		return true;
	}
	
	private boolean bracketEnd() throws SyntaxErrorException{
		
		if(isBracketed){
			commandFound();
			stop = true;
		}else throw new SyntaxErrorException("Bracket confusion.");
			
		return false;
	}
	
	private boolean detectRightBracket() throws SyntaxErrorException{
		if(currentChar == ']' && !escapeNext.get()){
			if(seenEnclosure){
				bracketLevel--;
				if(bracketLevel<0)
					return bracketEnd();
			}else{
				return bracketEnd();
			}
		}
		return true;
	}
	
	private void commandFound(){
		if(command==null){
			command       = builder.toString().trim();
			builder       = new StringBuilder();
		}
	}
	
	private boolean detectSemiColon(){
		if(currentChar==';' && escapeNext.get()){
			commented = false;
			
			if(!isBracketed){
				commandFound();
				stop = true;
				return false;
			}
		}
		return true;
	}
	
	private boolean detectSeparator(){
		if(startup)
			return true;
		//TODO More separators exists.
		if( !seenSeparator && Arrays.binarySearch(ParserUtils.separator,(char)currentChar) > -1 ){
			seenSeparator = true;
			commandFound();
			return false;
		}
		return true;
	}
	
	private boolean detectEnclosure(){
		if( currentChar == '|' && bracketLevel == 0 && !escapeNext.get()){
			if(!seenEnclosure)
				seenEnclosure = true;
			
			if(openEnclosure){
				enclosure = builder.toString().trim();
			}else{
				argEnclosures.put(enclosure, builder.toString().trim());
			}
			
			builder       = new StringBuilder();
			openEnclosure = !openEnclosure;
			
			return false;
		}
		return true;
	}
	
	private void resetState(){
		escapeNext.reset();
		eolBreak.reset();
		comment.reset();
	}
	
	@Override
	public int read() throws IOException {
		currentChar = super.read();
		return currentChar;
	}
	
	private boolean detectComment(){
		if(currentChar=='='){
			if(comment.get())
				commented = true;
			else
				comment.set();
			return false;
		}
		return !commented;
	}
	
	private boolean detectEof() throws EofException{
		if(currentChar==-1){
			if(startup)
				throw new EofException("EOF!");
			else{
				commandFound();
				stop = true;
			}
			return false;
		}
		if(Arrays.binarySearch(ParserUtils.separator,(char)currentChar) < 0 )
			startup = false;
		
		return true;
	}
	
	public Instruction readBlock() throws JavaclException, IOException{
		builder = new StringBuilder();
		
		while(!stop){
			resetState();
			read();
			
			if( detectEof()          )
			if( detectEscape()       )
			if( detectEolBreak()     )
			if( detectLeftBracket()  )
			if( detectRightBracket() )
			if( detectEnclosure()    )
			if( detectSemiColon()    )
			if( detectSeparator()    )
			if( detectEol()          )
			if( detectComment()      )
			builder.append((char)currentChar);
		}
		
		if(command==null){
			resetParserState();
			return null;
		}
		
		if(command.length()==0){
			resetParserState();
			return null;
		}
		
		argEnclosures.put(enclosure, builder.toString().trim());
		
		Instruction instr = new Instruction(command, new Argument(argEnclosures),expandContext);
		
		resetParserState();
		
		return instr;
	}

}
