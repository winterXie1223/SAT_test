import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.ExceptionalGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;


public class Generate{
	public static void main(String[] args){
		final String path1 = "D:\\study materials\\软件测试\\MF1833032_解温特_软件分析第二次作业\\Test\\bin";
		//final String paht2 = "TriangleClass.Triangle";
		final String paht2 = "symbol.TestFor";
		
		String path = Scene.v().getSootClassPath();
		if (path != null){
			Scene.v().setSootClassPath(path + ".;" + path1);
		}
		SootClass the_class = Scene.v().loadClassAndSupport(paht2);
		the_class.setApplicationClass();
		List<SootMethod> methods = the_class.getMethods();
		
		for (int i = 0; i < methods.size(); i++){
			SootMethod method = methods.get(i);
			if (method.getName().equals("test")){
				Body body = method.retrieveActiveBody();
				UnitGraph graph = new BriefUnitGraph(body);
				DotGraph drawer = new CFGToDotGraph().drawCFG(graph, body);
				drawer.plot("D:\\study materials\\软件测试\\MF1833032_解温特_软件分析第二次作业\\Test\\symbol_for_out2.dot");
			} 
		}
		System.out.println("成功输出控制流图！");
	}
}
