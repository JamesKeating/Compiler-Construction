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
		  //String assigned_val = "t" + Integer.toString(location);
		  String assigned_val = location_map.get("assn");
		  if  (assigned_val == null)
			  assigned_val ="t" + Integer.toString(location);
		  
		  String assigned_type = "Int";
		  
		  if (node.jjtGetNumChildren() == 4){
			  if(node.jjtGetChild(3).jjtGetNumChildren() == 0){
				  assigned_type = "Bool";
				  assigned_val = memory.get(node.jjtGetChild(2));
			  }
		  }
		  
		  
		  if (data_type.get(child1.value) != assigned_type){
			  System.out.print("incorrect type cannot assign bool to int or visa versa\nTerminating Program");
			  	System.exit(1);
		  }
		  	
		
		  code.add("Load");
		  code.add(child1.value.toString());
		  code.add(assigned_val);
		  code.add("null");
		  
		  System.out.println(code);
		  
		  if (node.jjtGetNumChildren() == 4 && assigned_type == "Int"){
			  System.out.println("[Lable, "+"lb"+Integer.toString(lable) +", null, null]");
			  if (((SimpleNode)node.jjtGetChild(3).jjtGetChild(2)).value == "false")
			  	return data;
		  }
		  
		  memory.put(child1.value, memory.get(assigned_val));
		  location_map.remove("assn");
		  //System.out.print(memory);
		  return data;
	  }
	  
	  
	  public Object visit(ASTArith node, Object data){
		  
		  
		  data = node.childrenAccept(this, data);
		  		  
		  if (node.jjtGetNumChildren() == 1)
			  return data;
	  	
		  String opCode = "Addition";
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
		  SimpleNode child2= (SimpleNode) node.jjtGetChild(2).jjtGetChild(0);
		  SimpleNode child3= (SimpleNode) node.jjtGetChild(1);
		  //System.out.print(child1 + "---");
		  
		  if (child1.value == null)
			  child1 = (SimpleNode) child1.jjtGetChild(0).jjtGetChild(0);
		  
		  if (child2.value == null)
			  child2 = (SimpleNode) child2.jjtGetChild(0).jjtGetChild(0);
		  
		  if (child3.value.toString() == "-")
			  opCode = "Subtraction";
		  
		  if (child1.value == null)
			  child1 = (SimpleNode) child1.jjtGetChild(0);
		  
		  //System.out.print("sa"+child1+child2.value);
		  if(data_type.get(child1.value) != "Int"|| data_type.get(child2.value)!= "Int" ){
			  System.out.print("Type error cannot " + child3.toString() + " bool\nTerminating program");
			  System.exit(1);
		  }

		  if((memory.get(child1.value) == null || memory.get(child2.value)!= null) && child1.toString() != "Num"){
			  System.out.println("Cannot "+child3.toString()+" a null value. Make sure all variables are assigned non null value.\nTerminating program");
			  System.exit(1);
		  }
		  
		  code.add(opCode);
		  code.add(newLocation());
		  code.add(location_map.get(child1.value));
		  code.add(location_map.get(child2.value));
		  System.out.println(code);
		  
		  location_map.remove("assn");
		  
		  String val;
		  if (code.get(0) == "Addition")
			  val =String.valueOf(Integer.parseInt(memory.get(code.get(2))) +Integer.parseInt(memory.get(code.get(3))));   
		  else
			  val =String.valueOf(Integer.parseInt(memory.get(code.get(2))) - Integer.parseInt(memory.get(code.get(3))));
		  
		  memory.put("t" +Integer.toString(location), val);
		
		  ((SimpleNode)child1.jjtGetParent().jjtGetParent()).value = val;
		  data_type.put(val, "Int");
		  location_map.put(val, "t" +Integer.toString(location));
		  //System.out.print(location_map);
		  
  
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
		  
		  if(data_type.get(child1.value) != "Int"|| data_type.get(child2.value)!= "Int" ){
			  System.out.print("Type error cannot multiple bool\nTerminating program");
			  System.exit(1);
		  }
		  String v1,v2;
		  if (child1.toString() == "Num")
			  v1 = child1.value.toString();
		  
		  else
			  v1 = memory.get(child1.value);
		  
		  if (child2.toString() == "Num")
			  v2 = child2.value.toString();
		  
		  else
			  v2= memory.get(child2.value);
		  
		  if(v1 == null || v2 == null ){
			  System.out.println("Cannot multiply a null value. Make sure all variables are assigned non null value.\nTerminating program");
			  System.exit(1);
		  }
		  	
		  String result = Integer.toString(Integer.parseInt(v1) * Integer.parseInt(v2));
		  
		  code.add("Multiply");  
		  code.add(node.value.toString());
		  code.add(location_map.get(child2.value));
		  code.add(location_map.get(child1.value));
		  
		  System.out.println(code);
		  
		  location_map.put(node.value, node.value.toString());
		  data_type.put(node.value, "Int");
		  memory.put(node.value, result);
		  location_map.remove("assn");
		    
		  return data;
	  }
	  
	  public Object visit(ASTFactor node, Object data){
		  
		  data = node.childrenAccept(this, data);
		  		  
		  String opCode = "Load";
		  String loc = location_map.get(node);
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child = (SimpleNode) node.jjtGetChild(0);
		  
		//  System.out.print(child);
		  if(child.toString() == "Arith")
			  return data;
		  
    	  if (location_map.containsKey(child.value)){
    		  location_map.put("assn", location_map.get(child.value.toString()));
			  return data;
    	  }
    	  location_map.remove("assn");
    	  
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
		  
		  if(node.jjtGetNumChildren() == 0){
			  data = node.childrenAccept(this, data);
			  return data;
	  }
		  
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
		  SimpleNode child2= (SimpleNode) node.jjtGetChild(2);
		  SimpleNode parent = (SimpleNode) node.jjtGetParent().jjtGetChild(2);
		  
		  child1.value = memory.get(parent);
		  if (child1.value == "true")
			  child2.value = "false";
		  else
			  child2.value = "true";

		  String opCode;
		  
		  SimpleNode loc1 = (SimpleNode) parent.jjtGetParent().jjtGetChild(1).jjtGetChild(0);
		  SimpleNode loc2 = (SimpleNode) parent.jjtGetChild(1).jjtGetChild(0);

		  
		  if (loc1.value == null)
			  loc1 = (SimpleNode) loc1.jjtGetChild(0).jjtGetChild(0);
		  
		  if (loc2.value == null)
			  loc2 = (SimpleNode) loc2.jjtGetChild(0).jjtGetChild(0);
		  
		  
		  switch (parent.value.toString()){
		  case "=": opCode = "Jump_Equal";
		  break;
		  
		  case ">": opCode = "Jump_More";
		  break;
		  
		  default: opCode = "Jump_Less";
		  }
	  
		  code.add(opCode);
		  code.add(newLable());
		  code.add(location_map.get(loc1.value));
		  code.add(location_map.get(loc2.value));
		  System.out.println(code);
		  
		  Node temp = node.children[0];
		  node.children[0] = node.children[2];
		  node.children[2] = temp;
		  
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
	  
	  public Object visit(ASTJump node, Object data){
		  data = node.childrenAccept(this, data);
		  String x = memory.get("assn");
		  if (x == null)
			  x ="t" + Integer.toString(location);
		  
	  	System.out.print("[Load, " 
		 +((SimpleNode)node.jjtGetParent().jjtGetParent().jjtGetChild(0)).value.toString() 
		 + ", " + x +" null]"
		 +"\n[Jump, " + newLable() +", null, null]"
		 +"\n[Lable, " +"lb"+Integer.toString(lable - 1)  + ", null, null]\n");
		  
		  if( ((SimpleNode)node.jjtGetParent().jjtGetChild(0)).value == "true" ){
			  	memory.put(((SimpleNode)node.jjtGetParent().jjtGetParent().jjtGetChild(0)).value, memory.get(x));
			  	location_map.remove("assn");
		  }
		  return data;
	  }
}
