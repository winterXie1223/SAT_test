import java.util.List;

import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;


public interface Traverse<T> {
	public void getPathsByDeepFirst();
	public T generateGraph();
	public void printAllPaths();
	
}
