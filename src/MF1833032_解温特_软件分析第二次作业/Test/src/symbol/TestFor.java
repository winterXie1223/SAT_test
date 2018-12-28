package symbol;

public class TestFor {
	public int test(int x, int y){
		for (int i = 0; i < y; i++){
			x += i;
		}
		return x;
	}
}
