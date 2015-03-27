package test;

import parsing.Column;
import parsing.DBParser;
import parsing.JavaSaver;
import parsing.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Szymon on 15/03/20.
 */
public class DBParserTest
{
    public static void main(String[] args) {
        File file = new File("./samples/sample_schema.sql");
        File file2 = new File("./samples");
        System.out.println(file.getAbsolutePath());
        try {
            List<Table> tables = DBParser.parse(file);
            JavaSaver.save(file2, tables);
           /* for (Table t : tables) {
                System.out.println(t.name);
                for (Column c : t.columns) {
                    System.out.print("   ");
                    System.out.print(c.name);
                    System.out.print(" - ");
                    System.out.println(c.type);
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
