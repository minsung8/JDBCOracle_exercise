package test04.singletonPattern;

public class Main {
	
	public void a_method() {
		
		NoSingletonNumber aObj = new NoSingletonNumber();
		
		System.out.println("aObj =>" + aObj);
		System.out.println("a_method() 메소드에서 cnt값 호출 : " + aObj.getNextNumber());
		
	}
	
	public void b_method() {
		
		NoSingletonNumber bObj = new NoSingletonNumber();
		
		System.out.println("bObj =>" + bObj);
		System.out.println("b_method() 메소드에서 cnt값 호출 : " + bObj.getNextNumber());
		
	}
	
	public void c_method() {
		
		NoSingletonNumber cObj = new NoSingletonNumber();
		
		System.out.println("cObj =>" + cObj);
		System.out.println("c_method() 메소드에서 cnt값 호출 : " + cObj.getNextNumber());
		
	}
	
	public void d_method() { 
		
		// SingletonNumber dObj = new SingletonNumber();	// 생성자의 접근제한자가 private이므로 생성불가
		SingletonNumber dObj = SingletonNumber.getInstance(); 			// static 메서드 호출 
		System.out.println("dObj =>" + dObj);
		System.out.println("d_method() 메소드에서 cnt값 호출 : " + dObj.getNextNumber());
		
	}
	
	
	public void e_method() {
		
		// SingletonNumber dObj = new SingletonNumber();	// 생성자의 접근제한자가 private이므로 생성불가
		SingletonNumber eObj= SingletonNumber.getInstance(); 			// static 메서드 호출 
		System.out.println("eObj =>" + eObj);
		System.out.println("e_method() 메소드에서 cnt값 호출 : " + eObj.getNextNumber());
		
	}
	
	public void f_method() {
		
		// SingletonNumber dObj = new SingletonNumber();	// 생성자의 접근제한자가 private이므로 생성불가
		SingletonNumber fObj = SingletonNumber.getInstance(); 			// static 메서드 호출 
		System.out.println("fObj =>" + fObj);
		System.out.println("f_method() 메소드에서 cnt값 호출 : " + fObj.getNextNumber());
		
	}

	

	public static void main(String[] args) {

		Main ma = new Main();
		
		ma.a_method();
		
		ma.b_method();
		
		ma.c_method();
		
		System.out.println("-------------------------------");
		
		ma.d_method();
		
		ma.e_method();
		
		ma.f_method();
		
	}

}