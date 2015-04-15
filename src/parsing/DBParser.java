package parsing;

import javafx.scene.control.Tab;
import parsing.Column;
import parsing.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Szymon on 15/03/19.
 */
public class DBParser {

    /**
     * @param line
     * @return substring between first and last "`" in the line (exclusive)
     */
    private String getName(String line) {
        return line.substring(line.indexOf("`") + 1, line.lastIndexOf("`"));
    }

    /**
     * @param line
     * @return substring between:
     * -point that is 2 chars after last "`" in the line
     * -first " " AFTER this point
     */
    private String getType(String line) {
        return line.substring(line.lastIndexOf("`") + 2, line.indexOf(" ", line.lastIndexOf("`") + 2));
    }

    public List<Table> parse(File file) throws FileNotFoundException {
        final Scanner sc = new Scanner(file);
        ArrayList<Table> tables = new ArrayList<Table>();                 //create returned table
        while (sc.hasNextLine()) {                                      //gets through whole file line by line
            String lineFromFile = sc.nextLine();                        //jump to next line
            if (lineFromFile.contains("CREATE TABLE")) {                //find CREATE TABLE statement
                Table table = new Table(getName(lineFromFile));         //get name from line
                tables.add(table);                                      //add current table to final list
                lineFromFile = sc.nextLine();                           //move inside CREATE TABLE statement
                while (lineFromFile.charAt(2) == '`') {                 //continues until you find line not starting with name, i. e. key declaration
                    Column column = new Column(getName(lineFromFile), getType(lineFromFile));   //get necessary info
                    table.columns.add(column);                          //add to results
                    lineFromFile = sc.nextLine();                       //jump to next line
                }
            }

        }
        return tables;
    }
}
