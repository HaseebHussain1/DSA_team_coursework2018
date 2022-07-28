package fileParser;


import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileParser
{
    private Scanner reader;

    /**
     * Create a new InputReader that reads text from the file with the given name.
     */
    public FileParser(String fileName) 
    {
        try 
        {
            reader = new Scanner(new File(fileName));
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Read a line of text from the input (a CSV file),
     * and return it as an array of String data values.
     *
     * @return  An array of Strings, where each String is one of the 
     *          fields in the input. Returns null if at the end 
     */
    public String[] getInput() 
    {
    	if (!reader.hasNext()) 
    	{
    		return null;
    	}
        String[] input = reader.nextLine().trim().split("\t");
        return input;
    }
    
    
}
