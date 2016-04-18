import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyParserDumpVisitor implements MyParserVisitor 
{
	  private int location = 0;
	  private Map<SimpleNode, String> location_map = new HashMap<>();
	  
	  
	  private String newLocation(){
		  location++; 
		  return "t" + Integer.toString(location);
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
		  return data;
	  }  
	  	    
	  public Object visit(ASTAssn node, Object data){
		  data = node.childrenAccept(this, data);
		  ArrayList<String> code = new ArrayList<String>();
		  code.add("Load");
		  code.add(((SimpleNode) node.jjtGetChild(0)).value.toString());
		  code.add("t" + Integer.toString(location));
		  code.add("null");
		  System.out.println(code);
		  return data;
	  }
	  
	  public Object visit(ASTArith node, Object data){
		  data = node.childrenAccept(this, data);
		  if (node.children.length == 1)
			  return data;
		  
		  String opCode = "Addition";
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0).jjtGetChild(0);
		  SimpleNode child2= (SimpleNode) node.jjtGetChild(2).jjtGetChild(0).jjtGetChild(0);
		  SimpleNode child3= (SimpleNode) node.jjtGetChild(1);
		  
		  if (child3.value.toString() == "-")
			  opCode = "Subtraction";
		  
		  code.add(opCode);
		  code.add(newLocation());
		  code.add(location_map.get(child1));
		  code.add(location_map.get(child2));
		  System.out.println(code);
		  
		  location_map.put(child1, "t" +Integer.toString(location));	
		  return data;
	  }
	  
	  public Object visit(ASTArith_Prime node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTTerm node, Object data){
		  data = node.childrenAccept(this, data);
		  
		  ArrayList<String> code = new ArrayList<String>();
		  if (node.children.length == 1)
			  return data;
		  
		  SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
		  SimpleNode child2 = (SimpleNode) node.jjtGetChild(1).jjtGetChild(0);
		  
		  code.add("Multiply");  
		  code.add(newLocation());
		  code.add(location_map.get(child2));
		  code.add(location_map.get(child1));
		  System.out.println(code);
		  
		  location_map.put(child1, "t" +Integer.toString(location));	//??
	  
		  return data;
	  }
	  
	  public Object visit(ASTTerm_Prime node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTFactor node, Object data){

		  data = node.childrenAccept(this, data);
		  String opCode = "Load";
		  ArrayList<String> code = new ArrayList<String>();
		  SimpleNode child = (SimpleNode) node.jjtGetChild(0);
		  if (child.value.toString().matches("[-+]?\\d*\\.?\\d+") )
			  opCode = "Load_Literal";
		  
		  code.add(opCode);
		  code.add(newLocation());
		  code.add(child.value.toString());
		  code.add("null");
		  System.out.println(code);
		  
		  location_map.put(node, "t" +Integer.toString(location));
		  return data;
	  }
	  
	  public Object visit(ASTComp node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTComp_Prime node, Object data){
		  data = node.childrenAccept(this, data);
		  return data;
	  }
	  
	  public Object visit(ASTCond node, Object data){
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
}
