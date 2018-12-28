import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

/**
 * Unit级的控制流程图深度优先遍历生成可执行路径
 * @author lichongyang
 *
 */
public class UnitTraverse implements Traverse<UnitGraph>{
	
	private final String path1;
	private final String path2;
	private List<List<Unit>> paths = new ArrayList<List<Unit>>();
	private int cyclomaticComplexity = 0;
	

	public UnitTraverse(String path1, String path2){
		this.path1 = path1;
		this.path2 = path2;
	}

	@Override
	public void getPathsByDeepFirst() {
		UnitGraph graph = generateGraph();
		if (graph == null){
			System.out.println("没有控制流图");
			return;
		}
		List<Unit> allUnits = getUnits(graph);
		int graphSize = allUnits.size();
		//System.out.print(allUnits.get(5));
		Graph<Unit> traverseGraph = new Graph<Unit>(graphSize, allUnits);
		for (int i = 0; i < graphSize; i++){
			Unit unit1 = allUnits.get(i);
			for (Unit unit2 : graph.getSuccsOf(unit1)){
				traverseGraph.addEdge(unit1, unit2);
			}
		}
		this.cyclomaticComplexity = CyclomaticComplexity(traverseGraph);
		Unit head = graph.getHeads().get(0);
		//System.out.print(head);
		Unit exit = graph.getTails().get(0);
		this.paths = traverseGraph.getAllPath(head, exit);
		//System.out.print(exit);
	}
	
	public List<Unit> getUnits(UnitGraph graph){
		Iterator<Unit> allUnits = graph.iterator();
		List<Unit> units = new ArrayList<>();
		while(allUnits.hasNext()){
			units.add(allUnits.next());
		}
		return units;
		
	}

	@Override
	public UnitGraph generateGraph() {
		UnitGraph graph = null;
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
				graph = new BriefUnitGraph(body);
				return graph;
			}
		}
		return graph;
	}

	@Override
	public void printAllPaths() {
		if (this.paths == null || this.paths.size() == 0){
			System.out.println("没有可行路径");
		}else{
			for (int i = 0; i < this.paths.size(); i++){
				System.out.println("path" + i);
				for (int j =  0; j < this.paths.get(i).size(); j++){
					if (j != 0){
						System.out.print("->");
					}
					System.out.print(this.paths.get(i).get(j));
				}
				System.out.println();
			}
		}
	}
	
	public List<List<Unit>> getPaths() {
		return paths;
	}
	
	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	public int CyclomaticComplexity(Graph<Unit> traverseGraph) {
		return traverseGraph.CyclomaticComplexity();
	}
	
}
