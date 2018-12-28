import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;


/**
 * Block级的控制流程图深度优先遍历生成可执行路径
 * @author lichongyang
 *
 */
public class BlockTraverse implements Traverse<BlockGraph> {
	//private final String path1 = "E:/java/Test/src";
	//private final String path2 = "symbol.Test";
	
	private final String path1;
	private final String path2;
	private List<List<Block>> paths = new ArrayList<List<Block>>();
	private int cyclomaticComplexity = 0;
	
	public BlockTraverse(String path1, String path2){
		this.path1 = path1;
		this.path2 = path2;
		
	}
	
	@Override
	public BlockGraph generateGraph(){
		BlockGraph graph = null;
		String path = Scene.v().getSootClassPath();
		if (path != null){
			Scene.v().setSootClassPath(path + ".;" + path1);
		}
		SootClass the_class = Scene.v().loadClassAndSupport(path2);
		the_class.setApplicationClass();
		List<SootMethod> methods = the_class.getMethods();
		
		for (int i = 0; i < methods.size(); i++){
			SootMethod method = methods.get(i);
			if (method.getName().equals("test")){
				Body body = method.retrieveActiveBody();
				graph = new BriefBlockGraph(body);
				return graph;
			}
		}
		return graph;
	}
	
	@Override
	public void getPathsByDeepFirst(){
		BlockGraph graph = generateGraph();
		if (graph == null){
			System.out.println("没有控制流图");
			return;
		}
		List<Block> allBlocks = graph.getBlocks();
		int graphSize = allBlocks.size();
		Graph<Block> traverseGraph = new Graph<Block>(graphSize, allBlocks);
		// 构建图模型
		for (int i = 0; i < graphSize; i++){
			Block block1 = allBlocks.get(i);
			for (Block block2 : block1.getSuccs()){
				traverseGraph.addEdge(block1, block2);
			}
		}
		this.cyclomaticComplexity = CyclomaticComplexity(traverseGraph);
		Block head = graph.getHeads().get(0);  //多个入口点是取第一个
		Block exit = graph.getTails().get(0);
		this.paths = traverseGraph.getAllPath(head, exit);
	}

	@Override
	public void printAllPaths() {
		if (this.paths == null || this.paths.size() == 0){
			System.out.println("没有可行路径");
		}else{
			for (int i = 0; i < this.paths.size(); i++){
				System.out.println("path" + i);
				for (int j =  0; j < this.paths.get(i).size(); j++){
					System.out.print(this.paths.get(i).get(j));
				}
				System.out.println();
			}
		}
	}
	
	public List<List<Block>> getPaths() {
		return paths;
	}
	
	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}
	
	public int CyclomaticComplexity(Graph<Block> traverseGraph) {
		return traverseGraph.CyclomaticComplexity();
	}


}

