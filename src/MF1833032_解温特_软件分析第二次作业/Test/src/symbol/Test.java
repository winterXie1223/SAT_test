package symbol;

public class Test {
	public int test(int x, int y){
		int z;
		if (x > 0){
			x += 10;
			z = x / 5;
		}else{
			z = x / 5 + 2;
			x += 10;
		}
		z += y;
		return z;
	}
}
