import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyParserDumpVisitor implements MyParserVisitor 
{
	  private int location = 0, lable = 0;
	  //store data used in generation of code
	  private Map<Object, String> location_map = new HashMap<>();	//eg of map myId = t1, otherId = t2
	  private Map<Object, String> data_type = new HashMap<>();			//eg of map myId = Int, otherId = Bool
	  private Map<Object, String> memory = new HashMap<>();			//eg of map t1 = 10, otherId = true
	  
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
		  
		  //Output of code for assignment
		  code.add("Load");
		  code.add(child1.value.toString());
		  code.add(assigned_val);
		  code.add("null");		  
		  System.out.println(code);
		  
		  //If assignment is based on condition
		  if (node.jjtGetNumChildren() == 4 && assigned_type == "Int"){
			  System.out.println("[Lable, "+"lb"+Integer.toString(lable) +", null, null]");
			  if (((SimpleNode)node.jjtGetChild(3).jjtGetChild(2)).value == "false")
			  	return data;
		  }
		  
		  memory.put(child1.value, memory.get(assigned_val));
		  location_map.remove("assn");
		  return data;
	  }
	  
	  
	  public Object visit(ASTArith node, Object data){
		  data = node.childrenAccept(this, data);
		 
		  //if arith only consists of factor or term (no addition or subtraction)
		  if (node.jjtGetNumChildren() == 1)
			  return data;
	  	
		  String opCode = "Addition";
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0); //element 1 being added
		  SimpleNode child2= (SimpleNode) node.jjtGetChild(2).jjtGetChild(0); //element 1 being added 
		  SimpleNode child3= (SimpleNode) node.jjtGetChild(1);	//plus or minus
		  
		  //if term has no value drill down to factor
		  if (child1.value == null)
			  child1 = (SimpleNode) child1.jjtGetChild(0).jjtGetChild(0);
		  
		  if (child2.value == null)
			  child2 = (SimpleNode) child2.jjtGetChild(0).jjtGetChild(0);
		  
		  //choose operation Plus by default
		  if (child3.value.toString() == "-")
			  opCode = "Subtraction";
		  
		  //if factor is (Arith) drill down one more level to value
		  if (child1.value == null)
			  child1 = (SimpleNode) child1.jjtGetChild(0);
		  
		  //check to values being added are declared as int (Num are all concidered int)
		  if(data_type.get(child1.value) != "Int"|| data_type.get(child2.value)!= "Int" ){
			  System.out.print("Type error cannot " + child3.toString() + " bool\nTerminating program");
			  System.exit(1);
		  }

		  //make sure numbers being addde have non null values, ie they have been assigned a value. They are null by default
		  if((memory.get(child1.value) == null || memory.get(child2.value)!= null) && child1.toString() != "Num"){
			  System.out.println("Cannot "+child3.toString()+" a null value. Make sure all variables are assigned non null value.\nTerminating program");
			  System.exit(1);
		  }
		  
		  //Output of code for arith
		  code.add(opCode);
		  code.add(newLocation());
		  code.add(location_map.get(child1.value));
		  code.add(location_map.get(child2.value));
		  System.out.println(code);
		  
		  location_map.remove("assn");	
		  
		  //actually adds numbers and stores result in case it is needed for a comparison
		  String val;
		  if (code.get(0) == "Addition")
			  val =String.valueOf(Integer.parseInt(memory.get(code.get(2))) +Integer.parseInt(memory.get(code.get(3))));   
		  else
			  val =String.valueOf(Integer.parseInt(memory.get(code.get(2))) - Integer.parseInt(memory.get(code.get(3))));
		  
		  memory.put("t" +Integer.toString(location), val);
		
		  
		  ((SimpleNode)child1.jjtGetParent().jjtGetParent()).value = val;
		  data_type.put(val, "Int");
		  location_map.put(val, "t" +Integer.toString(location));
		  return data;
	  }	  
	  
	  public Object visit(ASTTerm node, Object data){
		  data = node.childrenAccept(this, data);
		  
		  ArrayList<String> code = new ArrayList<String>();
		  //if term is made of only a factor (no multiplication code needed)
		  if (node.children.length == 1)
			  return data;
		  
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0).jjtGetChild(0);
		  SimpleNode child2 = (SimpleNode) node.jjtGetChild(1).jjtGetChild(0).jjtGetChild(0);
		  node.value = newLocation();
		  
		  if(data_type.get(child1.value) != "Int"|| data_type.get(child2.value)!= "Int" ){
			  System.out.print("Type error cannot multiple bool\nTerminating program");
			  System.exit(1);
		  }
		  //gets actual value of result so it can be used in comparisons / conditional
		  String v1,v2;
		  if (child1.toString() == "Num")
			  v1 = child1.value.toString();
		  else
			  v1 = memory.get(child1.value);
		  
		  if (child2.toString() == "Num")
			  v2 = child2.value.toString();
		  else
			  v2= memory.get(child2.value);
		  
		  //checks values being multiplied have been assigned a value
		  if(v1 == null || v2 == null ){
			  System.out.println("Cannot multiply a null value. Make sure all variables are assigned non null value.\nTerminating program");
			  System.exit(1);
		  }
		  String result = Integer.toString(Integer.parseInt(v1) * Integer.parseInt(v2));
		  
		  //Output of muliplication code
		  code.add("Multiply");  
		  code.add(node.value.toString());
		  code.add(location_map.get(child2.value));
		  code.add(location_map.get(child1.value));
		  System.out.println(code);
		  
		  //update hashmaps based on output
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
		  
		  if(child.toString() == "Arith")//if (arith)
			  return data;
		  
		  //if value has is already stored in temp variable ie t1 use that location no need to store it again
    	  if (location_map.containsKey(child.value)){
    		  location_map.put("assn", location_map.get(child.value.toString()));
			  return data;
    	  }
    	  location_map.remove("assn");
    	  
    	  //if value has not been stored anywhere store it in new temp var
		  if (loc  == null )
			  loc = newLocation();
		  
		  //if factor is a Num
		  if (child.value.toString().matches("[-+]?\\d*\\.?\\d+") ){
			  opCode = "Load_Literal";
			  data_type.put(child.value.toString(), "Int");
			  memory.put(loc, child.value.toString());
		  }
		  //if factor is an ID
		  else
			  memory.put(loc, memory.get(child.value));
		  
		  //Output of code for Factor
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
		  
		  //if there is no cond just a comp
		  if(node.jjtGetNumChildren() == 0){
			  data = node.childrenAccept(this, data);
			  return data;
		  }
		  
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
		  SimpleNode child2= (SimpleNode) node.jjtGetChild(2);
		  SimpleNode parent = (SimpleNode) node.jjtGetParent().jjtGetChild(2);
		  
		  //stores which Arith will actually get executed to we know which value to assign to memory
		  child1.value = memory.get(parent);
		  if (child1.value == "true")
			  child2.value = "false";
		  else
			  child2.value = "true";

		  String opCode;
		  SimpleNode loc1 = (SimpleNode) parent.jjtGetParent().jjtGetChild(1).jjtGetChild(0);
		  SimpleNode loc2 = (SimpleNode) parent.jjtGetChild(1).jjtGetChild(0);
		  //if term has no value drill down to factor
		  if (loc1.value == null)
			  loc1 = (SimpleNode) loc1.jjtGetChild(0).jjtGetChild(0);
		  
		  if (loc2.value == null)
			  loc2 = (SimpleNode) loc2.jjtGetChild(0).jjtGetChild(0);
		  
		  //decide on op code
		  switch (parent.value.toString()){
		  case "=": opCode = "Jump_Equal";
		  break;
		  
		  case ">": opCode = "Jump_More";
		  break;
		  
		  default: opCode = "Jump_Less";
		  }
	  
		  //output jump code for cond
		  code.add(opCode);
		  code.add(newLable());
		  code.add(location_map.get(loc1.value));
		  code.add(location_map.get(loc2.value));
		  System.out.println(code);
		  
		  //switch order of Arith : Arith (allows less jump/lables)
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
		  //jump and lable code after the else section of cond
	  	System.out.print("[Load, " 
		 +((SimpleNode)node.jjtGetParent().jjtGetParent().jjtGetChild(0)).value.toString() 
		 + ", " + x +" null]"
		 +"\n[Jump, " + newLable() +", null, null]"
		 +"\n[Lable, " +"lb"+Integer.toString(lable - 1)  + ", null, null]\n");
		  
	  	//if the cond was false. The else portion of cond is stored as the assigned value
		  if( ((SimpleNode)node.jjtGetParent().jjtGetChild(0)).value == "true" ){
			  	memory.put(((SimpleNode)node.jjtGetParent().jjtGetParent().jjtGetChild(0)).value, memory.get(x));
			  	location_map.remove("assn");
		  }
		  return data;
	  }
}