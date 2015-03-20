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


    private static String getName(String line)
    {
        return line.substring(line.indexOf("`")+1,line.lastIndexOf("`"));
    }
    private static String getType(String line)
    {
        return line.substring(line.lastIndexOf("`")+2,line.indexOf(" ",line.lastIndexOf("`")+2));
    }
    public static List<Table> parse(File file) throws FileNotFoundException {
        final Scanner sc=new Scanner(file);
        ArrayList<Table> tables=new ArrayList<Table>();
        while (sc.hasNextLine()) {
            String lineFromFile = sc.nextLine();
            if (lineFromFile.contains("CREATE TABLE")) {
                Table table = new Table(getName(lineFromFile));
                tables.add(table);
                lineFromFile = sc.nextLine();
                while (lineFromFile.charAt(2) == '`') {
                    Column column = new Column(getName(lineFromFile), getType(lineFromFile));
                    table.columns.add(column);
                    lineFromFile = sc.nextLine();
                }
            }

        }
        return tables;
    }
}
