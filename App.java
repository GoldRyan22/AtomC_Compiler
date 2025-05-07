import java.util.*;

public class App 
{
    public static void main(String[] args)
    {
       // System.out.println("Hello, World!");

        //States state = new CT_INTState();

       // String name = state.getClass().getName();

        //int len = name.length();

        //System.out.println(name.subSequence(0, len-5));

        LexAn lexAn = new LexAn();

         

        List<Token> tokenList = lexAn.LexicalAnalysis("tests/0.c");

        for (Token token : tokenList) 
        {
            System.out.println(token.toString());
        }

        SynAn synAn = new SynAn(tokenList);

        System.out.println(synAn.parse() + " " + synAn.getCurrent() + " " + synAn.getToken(synAn.getCurrent()).toString());
    }
}
