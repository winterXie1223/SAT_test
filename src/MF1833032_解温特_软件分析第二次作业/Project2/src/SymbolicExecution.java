import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soot.Unit;
import soot.Value;


public class SymbolicExecution {
	private List<List<Unit>> paths = new ArrayList<List<Unit>>();
	
	public SymbolicExecution(){
		
	}

	public void setPaths(List<List<Unit>> paths) {
		this.paths = paths;
	}
	
	public void execute(){
		//List<Unit> path = paths.get(1);
		//Map<String, Node> midResult = new HashMap<>();
		List<String> result = new ArrayList<>();
		for (List<Unit> path : paths){
			Map<String, Node> midResult = new HashMap<>();
			for (int i = 1; i <= path.size(); i++){
				parse(path.get(i), midResult, path, result);
			}
		}
		/**
		for (String key : midResult.keySet()) {  
		    System.out.println("Key = " + key);  
		    System.out.println(midResult.get(key));
		}  
		//System.out.print(result.get("x"));
		 * **/
		System.out.print(result);
	}
	
	public void parse(Unit unit, Map<String, Node> midResult, List<Unit> path, List<String> result){
		if (unit.toString().contains("parameter")){
			//System.out.println(unit.toString());
			String parameter = unit.toString().split(":=")[0].replace(" ", "");
			String value = unit.toString().split(":=")[1].split(":")[0].replace(" ", "");
			String type = unit.toString().split(":=")[1].split(":")[1].replace(" ", "");
			Node node = new Node();
			node.setValue(value);
			node.setType(type);
			midResult.put(parameter, node);
		}
		else if (unit.toString().contains(">")){
			List<String> unitChar =  java.util.Arrays.asList(unit.toString().split(" "));
			String parameter = unitChar.get(unitChar.indexOf(">") - 1);
			String value = unitChar.get(unitChar.indexOf(">") + 1);
			int index = path.indexOf(unit);
			Unit nextUnit = path.get(index + 1);
			//System.out.println(nextUnit);
			List<String> nextUnitChar =  java.util.Arrays.asList(nextUnit.toString().split(" "));
			if (nextUnitChar.get(0).equals("nop")){
				Node node = new Node();
				String parameterValue = midResult.get(parameter).getValue();
				node.setValue(parameterValue + ">" + value);
				node.setType("constraint");
				midResult.put(parameterValue, node);
			}else if (nextUnitChar.get(0).equals("goto")){
				//System.out.println(unitChar);
				Node node = new Node();
				String parameterValue = midResult.get(parameter).getValue();
				node.setValue(parameterValue + "<=" + value);
				node.setType("constraint");
				midResult.put(parameterValue, node);
			}
		}
		else if (unit.toString().contains("<")){
			List<String> unitChar =  java.util.Arrays.asList(unit.toString().split(" "));
			String parameter = unitChar.get(unitChar.indexOf("<") - 1);
			String value = unitChar.get(unitChar.indexOf("<") + 1);
			int index = path.indexOf(unit);
			Unit nextUnit = path.get(index + 1);
			List<String> nextUnitChar =  java.util.Arrays.asList(nextUnit.toString().split(" "));
			if (nextUnitChar.get(0).equals("nop")){
				Node node = new Node();
				String parameterValue = midResult.get(parameter).getValue();
				node.setValue(parameterValue + "<" + value);
				node.setType("constraint");
				midResult.put(parameterValue, node);
			}else if (nextUnitChar.get(0).equals("goto")){
				Node node = new Node();
				String parameterValue = midResult.get(parameter).getValue();
				node.setValue(parameterValue + ">=" + value);
				node.setType("constraint");
				midResult.put(parameterValue, node);
			}
		}
		else if (unit.toString().contains("=") && !unit.toString().contains("?") && !unit.toString().contains(":=")){
			List<String> unitChar =  java.util.Arrays.asList(unit.toString().split(" "));
			//System.out.println(unitChar);
			String parameter = unitChar.get(unitChar.indexOf("=") - 1);
			//System.out.println(parameter);
			List<String> values = unitChar.subList(unitChar.indexOf("=") + 1, unitChar.size());
			String value1 = values.get(0);
			for (int i = 1; i < values.size(); i++){
				value1 += values.get(i);
			}
			if (midResult.containsKey(value1)){
				//System.out.println(parameter);
				midResult.put(parameter, midResult.get(value1));
			}
			String value;
			if (values.contains("+")){
				//System.out.println(values);
				//System.out.println(values.get(values.indexOf("+") - 1));
				//System.out.println(result.get("temp$0").getValue());
				if (midResult.get(values.get(values.indexOf("+") - 1)).getValue().length() == 1){
					value = midResult.get(values.get(values.indexOf("+") - 1)).getValue() + "+" + values.get(values.indexOf("+") + 1);
				}else{
					if (midResult.get(values.get(values.indexOf("+") + 1)) == null){
						value = "(" + midResult.get(values.get(values.indexOf("+") - 1)).getValue() + ")" + "+" + values.get(values.indexOf("+") + 1);
					}else{
						value = "(" + midResult.get(values.get(values.indexOf("+") - 1)).getValue() + ")" + "+" + "(" + midResult.get(values.get(values.indexOf("+") + 1)).getValue() + ")";
					}
				}
				Node node = new Node();
				node.setValue(value);
				node.setType("operation");
				//System.out.println(parameter);
				//System.out.println(node);
				midResult.put(parameter, node);
			}else if(values.contains("-")){
				if (midResult.get(values.get(values.indexOf("-") - 1)).getValue().length() == 1){
					value = midResult.get(values.get(values.indexOf("-") - 1)).getValue() + "-" + values.get(values.indexOf("-") + 1);
				}else{
					//value = "(" + result.get(values.get(values.indexOf("-") - 1)).getValue() + ")" + "-" + values.get(values.indexOf("-") + 1);
					if (midResult.get(values.get(values.indexOf("-") + 1)) == null){
						value = "(" + midResult.get(values.get(values.indexOf("-") - 1)).getValue() + ")" + "-" + values.get(values.indexOf("-") + 1);
					}else{
						value = "(" + midResult.get(values.get(values.indexOf("-") - 1)).getValue() + ")" + "-" + "(" + midResult.get(values.get(values.indexOf("-") + 1)).getValue() + ")";
					}
				}
				Node node = new Node();
				node.setValue(value);
				node.setType("operation");
				midResult.put(parameter, node);
			}else if(values.contains("*")){
				if (midResult.get(values.get(values.indexOf("*") - 1)).getValue().length() == 1){
					value = midResult.get(values.get(values.indexOf("*") - 1)).getValue() + "*" + values.get(values.indexOf("*") + 1);
				}else{
					//value = "(" + result.get(values.get(values.indexOf("*") - 1)).getValue() + ")" + "*" + values.get(values.indexOf("*") + 1);
					if (midResult.get(values.get(values.indexOf("*") + 1)) == null){
						value = "(" + midResult.get(values.get(values.indexOf("*") - 1)).getValue() + ")" + "*" + values.get(values.indexOf("*") + 1);
					}else{
						value = "(" + midResult.get(values.get(values.indexOf("*") - 1)).getValue() + ")" + "*" + "(" + midResult.get(values.get(values.indexOf("*") + 1)).getValue() + ")";
					}
				}
				Node node = new Node();
				node.setValue(value);
				node.setType("operation");
				midResult.put(parameter, node);
			}else if(values.contains("/")){
				if (midResult.get(values.get(values.indexOf("/") - 1)).getValue().length() == 1){
					value = midResult.get(values.get(values.indexOf("/") - 1)).getValue() + "/" + values.get(values.indexOf("/") + 1);
				}else{
					//value = "(" + result.get(values.get(values.indexOf("/") - 1)).getValue() + ")" + "/" + values.get(values.indexOf("/") + 1);
					if (midResult.get(values.get(values.indexOf("/") + 1)) == null){
						value = "(" + midResult.get(values.get(values.indexOf("/") - 1)).getValue() + ")" + "/" + values.get(values.indexOf("/") + 1);
					}else{
						value = "(" + midResult.get(values.get(values.indexOf("/") - 1)).getValue() + ")" + "/" + "(" + midResult.get(values.get(values.indexOf("/") + 1)).getValue() + ")";
					}
				}
				Node node = new Node();
				node.setValue(value);
				node.setType("operation");
				midResult.put(parameter, node);
			}
		}
		else if (unit.toString().contains("return")){
			List<String> unitChar =  java.util.Arrays.asList(unit.toString().split(" "));
			String parameter = unitChar.get(unitChar.size() - 1);
			String theResult = null;
			try{
				String value = midResult.get(parameter).getValue();
				theResult = value;
				String pattern = "(@[a-z]*[0-9])";
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(value);
				while (m.find()){
					String parameter1 = m.group();
					if (midResult.get(parameter1) != null){
						if (midResult.get(parameter1).getType().equals("constraint")){
							theResult = theResult + '&' + midResult.get(parameter1).getValue();
						}
					}
				}
				
			}catch(Exception e){
				System.out.println("此处发生了一些错误");
			}
			result.add(theResult);
		}
	}
	
}

/**
 * 变量存值
 * @author lichongyang
 *
 */
class Node{
	private String value;
	private String type;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("value:" + value + ";type:" + type);
	}
	
}