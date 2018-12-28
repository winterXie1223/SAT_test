import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import soot.toolkits.graph.Block;


public class Graph<T> {
	public int vertices;  //¶¥µãÊý
	public Map<T, List<T>> graph = null;
	List<T> allBlocks = null;
	List<List<T>> paths = new ArrayList<>();
	public Graph(int vertices, List<T> allBlocks){
		this.vertices = vertices;
		this.allBlocks = allBlocks;
		this.graph = new HashMap<>();
	}
	
	public void addEdge(T block1, T block2){
		if (this.graph.get(block1) == null){
			List<T> edges = new ArrayList<>();
			edges.add(block2);
			this.graph.put(block1, edges);
		}else{
			this.graph.get(block1).add(block2);
		}
	}
	
	public void getAllPathsUtil(T block1, T block2, Map<T, Boolean> visited, Stack<T> path){
		visited.put(block1, true);
		path.add(block1);
		if (block1.equals(block2)){
			List<T> thePath = new ArrayList<>();
			for (int i = 0; i < path.size(); i++){
				thePath.add(path.get(i));
			}
			this.paths.add(thePath);
		}else{
			for (T block : this.graph.get(block1)){
				if (visited.get(block) == false){
					getAllPathsUtil(block, block2, visited, path);
					
				}
			}
		}
		path.pop();
		visited.put(block1, false);
	}
	
	public List<List<T>> getAllPath(T block1, T block2){
		Map<T, Boolean> visited = new HashMap<>();
		for (T block : this.allBlocks){
			visited.put(block, false);
		}
		Stack<T> path = new Stack<>();
		getAllPathsUtil(block1, block2, visited, path);
		return paths;
	}
	
	public int CyclomaticComplexity(){
		int edges = 0;
		for (T key : this.graph.keySet()){
			edges += this.graph.get(key).size();
		}
		return edges - this.vertices + 2;
	}
}
