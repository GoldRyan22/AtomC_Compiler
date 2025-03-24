import java.util.*;




public class App 
{
    public static void main(String[] args) throws Exception 
    {
       // System.out.println("Hello, World!");

        //States state = new CT_INTState();

       // String name = state.getClass().getName();

        //int len = name.length();

        //System.out.println(name.subSequence(0, len-5));

        LexAn lexAn = new LexAn();

         

        ArrayList<Token> tokenList = lexAn.LexicalAnalysis("tests/0.c");

        for (Token token : tokenList) 
        {
            System.out.println(token.toString());
        }
    }
}
