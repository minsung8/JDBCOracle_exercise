package test04.singletonPattern;

public class SingletonNumber {
	/*
	 	1. private 변수로 자기 자신의 클래스 인스턴스를 가지도록 해야 한다.
	 	접근제한자가 private 이므로 외부클래스에서는 직접적으로 접근이 불가하다.
	 	또한 static 변수로 지정하여 SingletonNumber 클래스를 사용할 때
	 	객체생성은 딱 1번만 생성되도록 해야 한다
	 	
	 	2. 생성자에 접근제한자를 private으로 지정하여, 외부에서 절대로 인스턴스를 생성하지 못하도록 막는다.
	 	
	 	3. static 메소드를 생성 하여 외부에서 해당 클래스의 객체를 사용할 수 있도록 해준다.
	 */ 
	
	// static 변수 => 첫번째로 호출
	private static SingletonNumber singleton = null;
	
	private int cnt = 0;
	
	// static 초기화 블록 => 두번째로 호출
	static {
		// static 초기화 블럭은 해당 클래스가 객체로 생성되기전에 먼저 실행되어지며, 딱 한번만 호출되어지고
		// 다음에 새로운 객체를 생성하더라도 static 초기화 블럭은 호출이 안되어진다.
		singleton = new SingletonNumber();
		
		System.out.println(">>> SingletonNumber 클래스의 static 초기화 블럭 호출 <<< ");
	}
	
	private SingletonNumber() {}
	
	public static SingletonNumber getInstance() {
		return singleton;
	}
	
	
	public int getNextNumber() {
		return ++cnt;
	}
	
}