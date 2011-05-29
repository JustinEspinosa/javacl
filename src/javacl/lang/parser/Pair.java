package javacl.lang.parser;

public class Pair<T> {
	public T first;
	public T second;
	public Pair(){
		first = null;
		second = null;
	}
	public Pair(T f, T s){
		first  = f;
		second = s;
	}
}
