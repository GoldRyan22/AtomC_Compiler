import java.util.List;

public class SynAn 
{
    private List<Token> tokens;
    private int current;

    Token getToken(int index)
    {
        return tokens.get(index);
    }

    public int getCurrent()
    {
        return current;
    }
    
    public SynAn(List<Token> tokens) 
    {
        this.tokens = tokens;
        this.current = 0;
    }
    
    private Token peek() 
    {
        if(current < tokens.size())
        {
            return tokens.get(current);
        }
        return null;

        //return current < tokens.size() ? tokens.get(current) : null;
    }
    
    private Token advance() 
    {
        if(current < tokens.size())
        {
            return tokens.get(current++);
        }
        return null;
        //return current < tokens.size() ? tokens.get(current++) : null;
    }
    
    private boolean match(String expected) 
    {
        if (peek() != null && peek().code.equals(expected)) 
        {
            if(expected.equals("END")) return true;
            advance();
            return true;
        }
        return false;
    }
    
    public boolean parse() 
    {
        while (peek() != null && !match("END")) 
        {
            if (!unit()) return false;
        }
        return match("END");
    }
    
    private boolean unit() 
    {
        return declStruct() || declFunc() || declVar();
    }
    
    private boolean declStruct() 
    {
        if (!match("STRUCT")) return false;
        if (!match("ID"))
        {
            System.err.println("expected ID");
            return false;
        }
        if (!match("LACC"))
        {
            System.err.println("expected LACC");
            return false;
        } 
        while (declVar());
        if (!match("RACC"))
        {
            System.err.println("expected RACC");
            return false;
        }
        if(match("SEMICOLON")) return true;
        System.err.println("expected SEMICOLON");
        return false;
    }
    
    private boolean declVar() 
    {
        if (!typeBase()) return false;
        if (!match("ID"))
        {
            System.err.println("expected ID");
            return false;
        } 
        arrayDecl();
        while (match("COMMA")) 
        {
            if (!match("ID"))
            {
                System.err.println("expected ID");
                return false;
            }
            arrayDecl();
        }
        if(match("SEMICOLON")) return true;
        System.err.println("expected SEMICOLON");
        return false;
    }
    
    private boolean typeBase() 
    {
        boolean result = match("INT") || match("DOUBLE") || match("CHAR") || (match("STRUCT") && match("ID"));
        if(result) return result;
        //System.err.println("expected TYPEBASE declaration");
        return result;
    }
    
    private void arrayDecl() 
    {
        if (match("LBRACKET")) 
        {
            expr();
            match("RBRACKET");
        }
    }
    
    private boolean declFunc() 
    {
        if (!(typeBase() || match("VOID"))) return false;
        match("MUL");
        if (!match("ID"))
        {
            System.err.println("expected ID");
            return false;
        }
        if (!match("LPAR")) 
        {
            System.err.println("expected LPAR");
            return false;
        }
        if (funcArg()) 
        {
            while (match("COMMA")) 
            {
                if (!funcArg()) return false;
            }
        }
        if (!match("RPAR")) 
        {
            System.err.println("expected RPAR");
            return false;
        }
        return stmCompound();
    }
    
    private boolean funcArg() 
    {
        if (!typeBase()) return false;
        if (!match("ID")) return false;
        arrayDecl();
        return true;
    }
    
    private boolean stmCompound() 
    {
        if (!match("LACC")) return false;
        while (declVar() || stm());
        return match("RACC");
    }
    
    private boolean stm() 
    {
        if (stmCompound()) return true;
        if (match("IF")) 
        {
            if (!match("LPAR")) return false;
            if (!expr()) return false;
            if (!match("RPAR")) return false;
            if (!stm()) return false;
            if (match("ELSE")) 
            {
                return stm();
            }
            return true;
        }
        if (match("WHILE")) 
        {
            if (!match("LPAR")) return false;
            if (!expr()) return false;
            if (!match("RPAR")) return false;
            return stm();
        }
        if (match("FOR")) 
        {
            if (!match("LPAR"))
            {
                System.err.println("expected LPAR");
                return false;
            } 
            expr();

            if(!match("SEMICOLON"))
            {
                System.err.println("expected SEMICOLON 1");
                return false;
            }

            expr(); 
            if(!match("SEMICOLON"))
            {
                System.err.println("expected SEMICOLON 2");
                return false;
            }

            expr();
            if (!match("RPAR"))
            {
                System.err.println("expected RPAR");
                return false;
            }
            return stm();
        }
        if (match("BREAK")) return match("SEMICOLON");
        if (match("RETURN")) {
            expr();
            return match("SEMICOLON");
        }
        expr();
        return match("SEMICOLON");
    }
    
    private boolean expr() 
    {
        return exprAssign();
    }

    private boolean exprAssign() 
    {
        if(exprUnary())
        {
            if(match("ASSIGN")) return exprAssign();        
        }
        return exprOr();
    }

    private boolean exprOr() 
    {
        if (!exprAnd()) return exprOr1();
        return exprOr1();
    }

    private boolean exprOr1() 
    {
        if (match("OR")) 
        {
            if (!exprAnd()) return false;
            return exprOr1();
        }
        return true;
    }

    private boolean exprAnd() 
    {
        if (!exprEq()) return exprAnd1();
        return exprAnd1();
    }

    private boolean exprAnd1() 
    {
        if (match("AND")) 
        {
            if (!exprEq()) return false;
            return exprAnd1();
        }
        return true;
    }

    private boolean exprEq() 
    {
        if (!exprRel()) return exprEq1();
        return exprEq1();
    }

    private boolean exprEq1() 
    {
        if (match("EQUAL") || match("NOTEQ")) 
        {
            if (!exprRel()) return false;
            return exprEq1();
        }
        return true;
    }

    private boolean exprRel() 
    {
        if (!exprAdd()) return exprRel1();
        return exprRel1();
    }

    private boolean exprRel1() 
    {
        if (match("LESS") || match("LESSEQ") || match("GREATER") || match("GREATEREQ")) {
            if (!exprAdd()) return false;
            return exprRel1();
        }
        return true;
    }

    private boolean exprAdd() 
    {
        if (!exprMul()) return exprAdd1();
        return exprAdd1();
    }

    private boolean exprAdd1() 
    {
        if (match("ADD") || match("SUB")) 
        {
            if (!exprMul()) return false;
            return exprAdd1();
        }
        return true;
    }

    private boolean exprMul() 
    {
        if (!exprCast()) return exprMul1();
        return exprMul1();
    }

    private boolean exprMul1() 
    {
        if (match("MUL") || match("DIV")) 
        {
            if (!exprCast()) return false;
            return exprMul1();
        }
        return true;
    }

    private boolean exprCast() 
    {
        if (match("LPAR")) 
        {
            if (typeName()) 
            {
                if (!match("RPAR")) return false;
                return exprCast();
            }
            current--;
        }
        return exprUnary();
    }

    private boolean typeName() 
    {
        if (!typeBase()) return false;
        arrayDecl();
        return true;
    }
    
    private boolean exprUnary() 
    {
        if (match("SUB") || match("NOT")) return exprUnary();
        return exprPostfix();
    }
    
    private boolean exprPostfix() 
    {
        if (!exprPrimary()) return false;
        while (match("LBRACKET")) 
        {
            if (!expr()) return false;
            if (!match("RBRACKET")) return false;
        }
        return true;
    }
    
    private boolean exprPrimary() 
    {
        if (match("ID")) 
        {
            if (match("LPAR")) 
            {
                if (expr()) 
                {
                    while (match("COMMA")) 
                    {
                        if (!expr()) return false;
                    }
                }
                return match("RPAR");
            }
            return true;
        }
        return match("CT_INT") || match("CT_REAL") || match("CT_CHAR") || match("CT_STRING") || (match("LPAR") && expr() && match("RPAR"));
    }
}
