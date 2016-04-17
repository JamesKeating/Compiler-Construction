
public class MyParserDumpVisitor implements MyParserVisitor 
{
	  private int indent = 0;
	  
	  private String indentString(){
		  StringBuffer sb = new StringBuffer();
		  for (int i = 0; i < indent; ++i ){
			  sb.append(' ');
		  }
		  return sb.toString();
	  }
	
	  public Object visit(SimpleNode node, Object data){
		  System.out.println(indentString() + node + "acceptor not implemented in subclass");
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTStart node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTBlock node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTDeclset node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTDeclset_Prime node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTDecl node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTVars node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTVars_Prime node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTStmtset node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTStmtset_Prime node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTStmt node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTAssn node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTArith node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTArith_Prime node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTTerm node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTTerm_Prime node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTFactor node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTComp node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTComp_Prime node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTCond node, Object data){
		  System.out.println(indentString() + node);
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTID node, Object data){
		  System.out.println(indentString() + node + "[" + node.value + "]" );
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
	  
	  public Object visit(ASTNum node, Object data){
		  System.out.println(indentString() + node + "[" + node.value + "]" );
		  ++indent;
		  data = node.childrenAccept(this, data);
		  --indent;
		  return data;
	  }
}
