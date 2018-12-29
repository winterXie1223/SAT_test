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


public class Test {
	public static void main(String[] args){
		final String path1 = "D:\\study materials\\�������\\MF1833032_������_��������ڶ�����ҵ\\Test\\bin";
		final String path2 = "symbol.Test";
		//BlockTraverse traverse = new BlockTraverse(path1, path2);
		//traverse.getPathsByDeepFirst();
		//System.out.print(traverse.getCyclomaticComplexity());
		//traverse.printAllPaths();
		UnitTraverse traverse1 = new UnitTraverse(path1, path2);
		traverse1.getPathsByDeepFirst();
		System.out.print("Ȧ���Ӷ�Ϊ��" + traverse1.getCyclomaticComplexity());
		traverse1.printAllPaths();
		SymbolicExecution execution = new SymbolicExecution();
		execution.setPaths(traverse1.getPaths());
		execution.execute();
	}
}
