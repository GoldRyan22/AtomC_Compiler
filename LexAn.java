
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.*;
import java.util.ArrayList;
import java.util.Scanner;


abstract class States
{
    States newState = null;
    boolean finalState = false;

    abstract States HandleState(char currentChar);
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

        
    }
    
}

public class LexAn 
{
    ArrayList<Token> tokenList = new ArrayList<>();

    String readLine;
    int lineCount = 0;
    int lineLen;

    char currentChar;
    char prevChar;

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

            lineCount++;

            lineLen = readLine.length();

            for(int i = 0; i < lineLen; i++)
            {
                currentChar = readLine.charAt(i);

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
            
        } catch (FileNotFoundException e) 
        {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }       

        return tokenList;
    }
}
