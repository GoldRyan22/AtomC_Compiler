
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.*;
import java.util.ArrayList;
import java.util.Scanner;


//------------------------------ BASIC STATES ----------------------------------

abstract class States
{
    States newState = null;
    boolean finalState = false;

    abstract States HandleState(char currentChar);
}

class FinalState extends States
{
    public FinalState()
    {
        this.finalState = true;
    }

    @Override
    States HandleState(char currentChar) 
    {
        newState = new State0();
        return newState;
    }
}

class ErrorState extends States
{
    @Override
    public States HandleState(char currentChar) {
        throw new UnsupportedOperationException("Not supported yet.");
    }  
}

class State0 extends States
{
    @Override
    public States HandleState(char currentChar) 
    {
        if(currentChar == '\'')
        {
            newState = new CharSlashORCharState();
        }
        else if(currentChar == '0')
        {
            newState = new OctalOrHexState();
        }
        else if(Character.isDigit(currentChar))
        {
            newState = new DigitState();    
        }
        else if(currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || Character.isWhitespace(currentChar))
        {
            newState = new State0();
            return newState;
        }
        else if(currentChar == '+')
        {
            newState = new ADDState();  
        }
        else if(currentChar == '-')
        {
            newState = new SUBState();
        }
        else if(currentChar == '*')
        {
            newState = new MULState();
        }
        else if(currentChar =='/' )
        {
            newState = new SlashState();
        }
        else if(currentChar == '.')
        {
            newState = new DOTState();
        }
        else if(currentChar == '&')
        {
            newState = new PartialAndState();
        }
        else if(currentChar == '|')
        {
            newState = new PartialOrState();
        }
        else if(currentChar == '!')
        {
            newState = new NotORNotEqualState();
        }
        else if(currentChar == '=')
        {
            newState = new AssignOREqualState();
        }
        else if(currentChar == '<')
        {
            newState = new LessORLessEqState();
        }
        else if(currentChar == '>')
        {
            newState = new GreaterORGreaterEqState();
        }
        else if(currentChar == ',')
        {
            newState = new COMMAState();
        }
        else if(currentChar == ';')
        {
            newState = new SEMICOLONState();
        }
        else if(currentChar == '(')
        {
            newState = new LPARState();
        }
        else if(currentChar == ')')
        {
            newState = new RPARState();
        }
        else if(currentChar == '[')
        {
            newState = new LBRACKETState();
        }
        else if(currentChar == ']')
        {
            newState = new RBRACKETState();
        }
        else if(currentChar == '{')
        {
            newState = new LACCState();
        }
        else if(currentChar == '}')
        {
            newState = new RACCState();
        }
        else if(Character.isLetter(currentChar))
        {
            newState = new IDKEY_BurnState();
        }


        
        return newState;    
    }   
}

//------------------------------ BASIC STATES-END ----------------------------------

//------------------------------ IDENTIFIERS AND KEYWORDS --------------------------

class IDKEY_BurnState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_')
        {
            newState = new IDKEY_BurnState();
            return newState;
        }
        else
        {
            newState = new IDKEYSTATE();
        }

        return newState;
    }
    
}

class IDKEYSTATE extends FinalState{};

//------------------------------ IDENTIFIERS AND KEYWORDS-END ----------------------

//------------------------------ CONSTANTS -----------------------------------------

class DigitState extends States
{

    @Override
    public States HandleState(char currentChar) 
    {

        if(Character.isDigit(currentChar))
        {
            newState = new DigitState();
            return newState;
        }

        if(currentChar == '.')
        {
            newState = new DigitDotState();
            return newState;
        }

        return newState = new CT_INTState();
    }
    
}

class OctalOrHexState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == 'x')
        {
            newState = new HexDigitState();
            
        }
        else if(Character.isDigit(currentChar) && currentChar!='8' && currentChar!='9')
        {
            newState = new OctalDigitState();
        }

        return newState;
        
    }
    
}

class OctalDigitState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(Character.isDigit(currentChar) && currentChar!='8' && currentChar!='9')
        {
            newState = new OctalDigitState();
        }
        else
        {
            newState = new CT_INTState();
        }

        return newState;
    }   
}

class HexDigitState extends States
{
    @Override
    States HandleState(char currentChar) 
    {
        if(Character.isDigit(currentChar) || (currentChar>='a' && currentChar<='f') || (currentChar>='A' && currentChar<='F'))
        {
            newState = new HexDigitState();
        }
        else
        {
            newState = new CT_INTState();
        }

        return newState;
    }
}

class CT_INTState extends States
{
    public CT_INTState() 
    {
        this.finalState = true;
    }

    @Override
    States HandleState(char currentChar) 
    {
        newState = new State0();
        return newState; 
    }  
}

class DigitDotState extends States
{

    @Override
    public States HandleState(char currentChar) 
    {
        if(Character.isDigit(currentChar))
        {
            newState = new DigitRealState();
            return newState;
        }

        return newState;
    }
    
}

class DigitRealState extends States
{

    @Override
    public States HandleState(char currentChar) 
    {
        if(Character.isDigit(currentChar))
        {
            newState = new DigitRealState();
            return newState;
        }
        else
        {
            newState = new CT_REALState();
            return newState;
        }   
    }
    
}

class CT_REALState extends States
{
    public CT_REALState()
    {
        this.finalState = true;
    }

    @Override
    States HandleState(char currentChar) 
    {
        newState = new State0();
        return newState;
    }
}

class CharSlashORCharState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '\\')
        {
            newState = new SpecialCharState();
        }
        else
        {
            newState = new CloseChar();
        }

        return newState;
    }  
}

class SpecialCharState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == 'a' || currentChar == 'b' || currentChar == 'f' || currentChar == 'n' ||
           currentChar == 'r' || currentChar == 't' || currentChar == 'v' || currentChar == '0')
        {
            newState = new CloseChar();
        }

        return newState;
    }
    
}

class CloseChar extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '\'')
        {
            newState = new CT_CHARState();
        }

        return newState;
    }
    
}

class CT_CHARState extends FinalState{}

class StringBurner extends States
{

    @Override
    States HandleState(char currentChar) 
    {
       if(currentChar !='\"' )
       {
            newState = new StringBurner();
       }
       else
       {
            newState = new CT_STRINGState();
       }

       return newState;
    }
}

class CT_STRINGState extends FinalState{};


//------------------------------------------ CONSTANTS-END --------------------------

//------------------------------------------ OPERATORS ------------------------------

class ADDState extends FinalState{}

class SUBState extends FinalState{}

class MULState extends FinalState{}

class DIVState extends FinalState{}

class DOTState extends FinalState{}

class PartialAndState extends States
{
    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '&')
        {
            newState = new ANDState();
            return newState;
        }
        return newState;
    }
}

class ANDState extends FinalState{}

class PartialOrState extends States
{
    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '|')
        {
            newState = new ORState();
            return newState;
        }
        return newState;
    }
}

class ORState extends FinalState{}

class NOTState extends FinalState{}

class AssignOREqualState extends States
{
    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '=')
        {
            newState = new EQUALState();
            return newState;
        }
        else
        {
            newState = new ASSIGNState();
            return newState;
        }

        //return newState;
    }
}

class ASSIGNState extends FinalState{}

class EQUALState extends FinalState{}

class NotORNotEqualState extends States
{
    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '=')
        {
            newState = new NOTEQState();
            

        }
        else
        {
            newState = new NOTState();
        }

        return newState;
        
    }
}

class NOTEQState extends FinalState{}

class LESSState extends FinalState{}

class LESSEQ extends FinalState{}

class LessORLessEqState extends States
{
    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '=')
        {
            newState = new LESSEQ();
            return newState;
        }
        else
        {
            newState = new LESSState();
            return newState;
        }
        
    }
}

class GREATERState extends FinalState{}

class GREATEREQState extends FinalState{}

class GreaterORGreaterEqState extends States
{
    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '=')
        {
            newState = new GREATEREQState();
            return newState;
        }
        else
        {
            newState = new GREATERState();
            return newState;
        }
        
    }
}

//------------------------------------------ OPERATORS-END ----------------------------

//------------------------------------------ DELIMITERS -------------------------------

class COMMAState extends FinalState{}

class SEMICOLONState extends FinalState{}

class LPARState extends FinalState{}

class RPARState extends FinalState{}

class LBRACKETState extends FinalState{}

class RBRACKETState extends FinalState{}

class LACCState extends FinalState{}

class RACCState extends FinalState{}

class SlashState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '/')
        {
            newState = new LineComBurnState();
        }
        else if(currentChar == '*')
        {
            newState = new CommBurnState();
        }
        else
        {
            newState = new DIVState();
        }

        return newState;
    }
    
} 

class LineComBurnState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar !='\n' && currentChar !='\r' && currentChar !='\0')
        {
            newState = new LineComBurnState();
        }
        else
        {
            newState = new LINECOMMENTState();
        }

        return newState;
    }
    
}

class LINECOMMENTState extends FinalState{}

class CommBurnState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
        if(currentChar == '*')
        {
            newState = new CommStarState();
        }
        else
        {
            newState = new CommBurnState();
        }

        return newState;   
    }
    
}

class CommStarState extends States
{

    @Override
    States HandleState(char currentChar) 
    {
       if(currentChar == '/')
       {
            newState = new COMMENTState();
       }
       else
       {
            newState = new CommBurnState();
       }

       return newState;
    }
}

class COMMENTState extends FinalState{}

//------------------------------------------ DELIMITERS-END ---------------------------


public class LexAn 
{
    ArrayList<Token> tokenList = new ArrayList<>();

    String readLine;
    int lineCount = 0;
    int lineLen;

    char currentChar;
    //char prevChar;

    String TokenValue = "";

    States TheState = new State0();

    ArrayList<Token> LexicalAnalysis(String fileName)
    {
        try 
        {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine())
        {
            readLine = scanner.nextLine();

            readLine += '\n';

            lineCount++;

            lineLen = readLine.length();

            for(int i = 0; i < lineLen; i++)
            {
                currentChar = readLine.charAt(i);

                if(TheState instanceof State0) TokenValue = "";

                TheState = TheState.HandleState(currentChar);

                if(TheState.finalState)
                {
                    if(TokenValue.isEmpty())
                    {
                        TokenValue +=currentChar;
                        i++;
                    }

                    

                    // making the codename for the final class ex CT_INTSTATE -> CT_INT   
                    String codeName = TheState.getClass().getName();
                    int nameLen = codeName.length();
                    codeName = codeName.subSequence(0, nameLen-5).toString();

                    if(!codeName.equals("COMMENT") && !codeName.equals("LINECOMMENT"))
                    {
                        Token newToken = new Token(lineCount, TokenValue, codeName);
                        tokenList.add(newToken);
                    }

                    TheState = TheState.HandleState(currentChar);
                    TokenValue = "";
                    i--;
                }
                else
                {
                    TokenValue += currentChar;
                }
            }
        }
        
        scanner.close();
            
        } catch (FileNotFoundException e) 
        {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }

        return tokenList;
    }
}
