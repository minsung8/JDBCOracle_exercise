package test03.objectCreateOrder;

public class MyChild extends MyParent {
	
	String name = "이순신";
	static String address = "경기도";
	
	// static 초기화 블럭 => 보통 환경설정에 사용 ( 한번만 실행되기 때문에 )
	static {
		System.out.println("### 1. 자식클래스 MyChild 의 static 초기화 블럭 실행됨(딱 한번만 실행됨) \n");
		address = "서울시 강남구 도곡동";
	}
	
	// instance 초기화 블럭
	{
		System.out.println("### 3. 자식클래스 MyChild 의 instance 초기화 블럭 실행됨 ### \n");
		name = "엄정화";
	}
	
	public MyChild() {
		System.out.println("### 4. 자식클래스 MyChild 의 default MyChild() 생성자 실행됨 ### \n");
		name = "몰라요";	
		}
	
	
}
