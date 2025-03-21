
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.nimbus.State;


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
        if(Character.isDigit(currentChar))
        {
            newState = new DigitState();
            return newState;
        }
        else if(currentChar == ' ' || currentChar == '\n' || currentChar == '\r')
        {
            newState = new State0();
            return newState;
        }

        
        return newState;    
    }   
}

//------------------------------ BASIC STATES-END ----------------------------------

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
                    // making the codename for the final class ex CT_INTSTATE -> CT_INT   
                    String codeName = TheState.getClass().getName();
                    int nameLen = codeName.length();
                    codeName = codeName.subSequence(0, nameLen-5).toString();

                    Token newToken = new Token(lineCount, TokenValue, codeName);
                    tokenList.add(newToken);

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
