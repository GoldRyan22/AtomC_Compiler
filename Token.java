

enum code 
{
   ID("ID"), BREAK("BREAK"), CHAR("CHAR"), DOUBLE("DOUBLE"), ELSE("ELSE"), FOR("FOR"), IF("IF"), INT("INT"), RETURN("RETURN"), STRUCT("STRUCT"), VOID("VOID"), WHILE("WHILE"), CT_INT("CT_INT"), CT_REAL("CT_REAL"), CT_CHAR("CT_CHAR"), CT_STRING("CT_STRING"), COMMA("COMMA"), SEMICOLON("SEMICOLON"), LPAR("LPAR"), RPAR("RPAR"), LBRACKET("LBRACKET"), RBRACKET("RBRACKET"), LACC("LACC"), RACC("RACC"), ADD("ADD"), SUB("SUB"), MUL("MUL"), DIV("DIV"), DOT("DOT"), AND("AND"), OR("OR"), NOT("NOT"), ASSIGN("ASSIGN"), QUAL("QUAL"), NOTEQ("NOTEQ"), LESS("LESS"), LESSEQ("LESSEQ"), GREATER("GREATER"), GREATEREQ("GREATEREQ");

   String codeName;

   code(String codeName)
   {
      this.codeName = codeName;
   }

}

public class Token 
{
   //code code;

   String code;

   Object value;

   int line;

   Token(int line, String value, String codeName)
   {
      this.line = line;
      this.code = codeName;

      if(codeName.equals("CT_INT"))
      {
         this.value = Integer.valueOf(value);
         //this.code = code.CT_INT;
      }
      else if(codeName.equals("CT_REAL"))
      {
         this.value = Double.valueOf(value);
         //this.code = code.CT_REAL;
      }
      else
      {
         this.value = value;
      }

      
      

   }
}
