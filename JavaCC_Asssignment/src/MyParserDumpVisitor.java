import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyParserDumpVisitor implements MyParserVisitor 
{
	  private int location = 0, lable = 0;
	  private Map<Object, String> location_map = new HashMap<>();
	  private Map<Object, String> data_type = new HashMap<>();
	  private Map<Object, String> memory = new HashMap<>();
	  
	  private String newLocation(){
		  location++; 
		  return "t" + Integer.toString(location);
	  }
	  
	  private String newLable(){
		  lable++; 
		  return "lb" + Integer.toString(lable);
	  }
	  
	  public Object visit(SimpleNode node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTStart node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTDecl node, Object data){
		  data = node.childrenAccept(this, data);
		  
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0), child2;
		  
		  for(int i = 1; i < node.jjtGetNumChildren(); i++){
			  child2 =  (SimpleNode) node.jjtGetChild(i);
			  data_type.put(child2.value, child1.toString());
		  }
		  
		  
		  return data;
	  }  
	  	    
	  public Object visit(ASTAssn node, Object data){
		  data = node.childrenAccept(this, data);
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
		  String assigned_val = "t" + Integer.toString(location);
		  String assigned_type = "Int";
		  
		  if (node.jjtGetNumChildren() == 3){
			  assigned_type = "Bool";
			  assigned_val = memory.get(node.jjtGetChild(2));
		  }
		  
		  if (data_type.get(child1.value) != assigned_type)
			  System.out.print("incorrect type");
		
		  
		  code.add("Load");
		  code.add(child1.value.toString());
		  code.add(assigned_val);
		  code.add("null");
		  
		  System.out.println(code);
		  memory.put(child1.value, memory.get("t" + Integer.toString(location)));
		  return data;
	  }
	  
	  
	  public Object visit(ASTArith node, Object data){
		  
		  if (node.value == "false")
			  return data;
		  
		  data = node.childrenAccept(this, data);
		  if (node.children.length == 1)
			  return data;
		  
		  
		  
		  String opCode = "Addition";
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
		  SimpleNode child2= (SimpleNode) node.jjtGetChild(2).jjtGetChild(0);
		  SimpleNode child3= (SimpleNode) node.jjtGetChild(1);
		  
		  
		  if (child1.value == null)
			  child1 = (SimpleNode) child1.jjtGetChild(0).jjtGetChild(0);
		  
		  if (child2.value == null)
			  child2 = (SimpleNode) child2.jjtGetChild(0).jjtGetChild(0);
		  
		  if (child3.value.toString() == "-")
			  opCode = "Subtraction";
		  
		  if(data_type.get(child1.value) != "Int"|| data_type.get(child2.value)!= "Int" ){
			  System.out.print("Type error cannot " + child3.toString() + " bool\nTerminating program");
			  System.exit(1);
		  }
		  
		  code.add(opCode);
		  code.add(newLocation());
		  code.add(location_map.get(child1.value));
		  code.add(location_map.get(child2.value));
		  System.out.println(code);
		  
		  location_map.put(child1.value, "t" +Integer.toString(location));
		  return data;
	  }	  
	  
	  public Object visit(ASTTerm node, Object data){
		  data = node.childrenAccept(this, data);
		  
		  ArrayList<String> code = new ArrayList<String>();
		  if (node.children.length == 1)
			  return data;
		  
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0).jjtGetChild(0);
		  SimpleNode child2 = (SimpleNode) node.jjtGetChild(1).jjtGetChild(0).jjtGetChild(0);
		  node.value = newLocation();
		  
		  System.out.println(child1.value);
		  System.out.println(child2.value);
		  if(data_type.get(child1.value) != "Int"|| data_type.get(child2.value)!= "Int" ){
			  System.out.print("Type error cannot multiple bool\nTerminating program");
			  System.exit(1);
		  }
		  
		  String result = Integer.toString(Integer.parseInt(memory.get(child2.value)) * Integer.parseInt(memory.get(child1.value)));
		  
		  code.add("Multiply");  
		  code.add(node.value.toString());
		  code.add(location_map.get(child2.value));
		  code.add(location_map.get(child1.value));
		  
		  System.out.println(code);
		  
		  location_map.put(node.value, node.value.toString());
		  data_type.put(node.value, "Int");
		  memory.put(node.value, result);
		  
		  return data;
	  }
	  
	  public Object visit(ASTFactor node, Object data){
		  
		  data = node.childrenAccept(this, data);
		  		  
		  String opCode = "Load";
		  String loc = location_map.get(node);
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child = (SimpleNode) node.jjtGetChild(0);
		  
		  if (location_map.containsKey(child.value))
			  return data;
		  
		  if (loc  == null )
			  loc = newLocation();
		  
		  if (child.value.toString().matches("[-+]?\\d*\\.?\\d+") ){
			  opCode = "Load_Literal";
			  data_type.put(child.value.toString(), "Int");
			  memory.put(loc, child.value.toString());
		  }
		  else
			  memory.put(loc, memory.get(child.value));
		  
		  code.add(opCode);
		  code.add(loc);
		  code.add(child.value.toString());
		  code.add("null");
		  System.out.println(code);
		  
		  location_map.put(child.value, loc);
		  
		  return data;
	  }
	  
	  public Object visit(ASTComp node, Object data){
		  
		  data = node.childrenAccept(this, data);
		  SimpleNode parent = (SimpleNode) node.jjtGetParent().jjtGetChild(1).jjtGetChild(0);
		  SimpleNode child1= (SimpleNode) node.jjtGetChild(1).jjtGetChild(0);
		  SimpleNode child2 = (SimpleNode) node.jjtGetChild(0);
		  
		  if (parent.value == null)
			  parent = (SimpleNode) parent.jjtGetChild(0).jjtGetChild(0);
		  
		  if (child1.value == null)
			  child1 = (SimpleNode) child1.jjtGetChild(0).jjtGetChild(0);
		  
		  	
		  int value1, value2;
		  
		  if (parent.toString() == "Num")
			  value1 = Integer.parseInt(parent.value.toString());
		  
		  else
			  value1 = Integer.parseInt(memory.get(parent.value));
		  
		  if (child1.toString() == "Num")
			  value2 = Integer.parseInt(child1.value.toString());
		  
		  else
			  value2 = Integer.parseInt(memory.get(child1.value));
		

		  if(data_type.get(child1.value) != "Int"|| data_type.get(parent.value)!= "Int" ){
			  System.out.print("Type error cannot multiple bool\nTerminating program");
			  System.exit(1);
		  }
		  ;
		   
		  switch (child2.value.toString()){
		  
		  case "=": memory.put(node, String.valueOf(value1 == value2));
		  break;
		  
		  case ">": memory.put(node, String.valueOf(value1 > value2));
		  break;
		  
		  default: memory.put(node, String.valueOf(value1 < value2));
		  }
		  
		  node.value = child2.value.toString();

		  return data;
	  }
	  
	  public Object visit(ASTCond node, Object data){
		  
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
		  SimpleNode child2= (SimpleNode) node.jjtGetChild(1);
		  SimpleNode parent = (SimpleNode) node.jjtGetParent().jjtGetChild(2);
		  
		  child1.value = memory.get(parent);
		  if (child1.value == "true")
			  child2.value = "false";
		  else
			  child2.value = "true";
		  
		  String opCode;
		  switch (parent.value.toString()){
		  case "=": opCode = "Jump_Equal";
		  break;
		  
		  case ">": opCode = "Jump_More";
		  break;
		  
		  default: opCode = "Jump_Less";
		  }
	  
		  code.add(opCode);
		  code.add(newLable());
		  code.add("loc1");
		  code.add("loc2");
		  System.out.println(code);
		  System.out.println("[Jump, " + newLable() +", null, null]");
		  
		  data = node.childrenAccept(this, data);  
		  return data;
	  }
	  
	  public Object visit(ASTID node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTNum node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTPlus node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTMinus node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTInt node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTBool node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTGreater node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTEqual node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTLess node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
}
