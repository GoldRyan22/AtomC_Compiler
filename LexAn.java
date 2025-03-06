
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LexAn 
{
    ArrayList<Token> tokenList = new ArrayList<>();

    String readLine;
    int lineCount = 0;

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
        }    
            
        } catch (FileNotFoundException e) 
        {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
        


        return tokenList;
    }
}
