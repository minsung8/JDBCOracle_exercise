package test04.singletonPattern;

public class NoSingletonNumber {

	private int cnt = 0;
	
	public NoSingletonNumber() {}
	
	public int getNextNumber() {
		return ++cnt;
	}
	
}
