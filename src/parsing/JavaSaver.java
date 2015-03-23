package parsing;

import java.io.File;
import java.util.List;

/**
 * Created by Szymon on 15/03/19.
 */
public class JavaSaver {

    private static String convertName(String n)
    {

        if(n.contains("_"))
        {
            int temp=n.indexOf("_");
            n=n.substring(0, temp-1).concat(n.substring(temp+1, n.length()-1));
            Character.toUpperCase(n.charAt(temp));
        }

        if (n.endsWith("ees")) //e.g. employees
        {
            return n.substring(0, n.length()-2);
        }

        if (n.endsWith("es")) // e.g. heroes
        {
            return n.substring(0, n.length()-3);
        }

        if (n.endsWith("s")) // e.g. students
        {
            return n.substring(0, n.length()-2);
        }
        
        return n;
    }

    static void save(File folder, List<Table> tables)
    {

    }
}
