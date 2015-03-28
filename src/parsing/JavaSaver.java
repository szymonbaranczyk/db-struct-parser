package parsing;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Szymon on 15/03/19.
 */
public class JavaSaver {

    static private HashSet<String> set = new HashSet(5); //storage for all imports
    static private File choosenFolder = null;
    private static String toUpperCase(String s)
    {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    private static String convertName(String n) {

        while (n.contains("_")) // while because we want to delete all of them
        {
            int temp = n.indexOf("_"); // searching for '_' connecting two words
            char ch = Character.toUpperCase(n.charAt(temp + 1)); //new word should begin with upper case
            n = n.substring(0, temp) + (ch) + (n.substring(temp + 2, n.length()));
        }

        if (n.endsWith("ees")) //e.g. employees
            return n.substring(0, n.length() - 1);

        if (n.endsWith("ies"))// e.g. babies
            return (n.substring(0, n.length() - 3) + "y");

        if (n.endsWith("es")) // e.g. heroes
            return n.substring(0, n.length() - 2);


        if (n.endsWith("s")) // e.g. students
            return n.substring(0, n.length() - 1);
        return n;
    }

    private static String convertType(String t) {
        if (t.contains("bit(1)") || t.contains("bool"))
            return "boolean";
        if (t.contains("bigint"))
            return "long";
        if (t.contains("int"))
            return "int";
        if (t.contains("float"))
            return "float";
        if (t.contains("double"))
            return "double";
        if (t.contains("char") || t.contains("text"))
            return "String";
        if (t.contains("decimal")) {
            set.add("import java.math.BigDecimal;");//added to set and printed later
            return "BigDecimal";
        }
        if (t.contains("date")) {
            set.add("import java.sql.Date;");
            return "Date";
        }
        if (t.contains("timestamp")) {
            set.add("import java.sql.Timestamp;");
            return "Timestamp";
        }
        if (t.contains("time")) {
            set.add("import java.sql.Time;");
            return "Time";
        }
        if (t.contains("blob") || t.contains("binary") || t.contains("bit"))
            return "byte []";
        if (t.contains("enum")) {
            return t;
        }
        return null;
    }

    /**
     * @param table
     * @return String containing text which we will save to file
     */
    private static String createText(Table table) {    //folder is passed so that file can be created in case of enum
        String className = convertName(table.name);
        className = className.substring(0, 1).toUpperCase() + className.substring(1);

        StringBuilder s = new StringBuilder();

        s.append("public class " + className + " {\r\n\r\n");
        for (Column elem : table.columns) {
            if (!convertType(elem.type).startsWith("enum")) {
                s.append(convertType(elem.type));
                s.append(" ");
                s.append(convertName(elem.name));
                s.append(";\r\n");
            } else {
                String type = toUpperCase(convertName(elem.name));
                s.append(type);
                s.append(" ");
                s.append(convertName(elem.name));
                s.append(";\r\n");
                createEnumFile(elem.type, type);
            }
        }
        s.append("\r\n}");
        return s.toString();
    }


    public static void save(File folder, List<Table> tables) {
        BufferedWriter writer = null;
        choosenFolder = folder;
        for (Table elem : tables) {
            try {
                File file = new File(folder, toUpperCase(convertName(elem.name)) + ".java");
                writer = new BufferedWriter(new FileWriter(file));

                String temp = createText(elem);//only remembered, so we can first print all imports

                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    writer.write(iterator.next().toString() + "\r\n");
                }
                writer.write("\r\n");
                writer.write(temp);

                set.clear();
            } catch (IOException e) {
            } finally {
                try {
                    if (writer != null)
                        writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createEnumFile(String enumstring, String name) {
        File file = new File(choosenFolder, name + ".java");
        StringBuilder s = new StringBuilder();
        s.append("public enum ");
        s.append(name);
        s.append(" {\r\n");
        String[] parts = enumstring.split("'");
        for (int i = 1; i < parts.length - 1; i++) {
            if (i % 2 == 1) {
                s.append(parts[i]);         //prints every second part (enum constant) and skips commas
                if (i < parts.length - 2)        //so that ", " won't be printed after last constant
                    s.append(", ");
            }
        }
        s.append("\r\n}");
        try {
            BufferedWriter bf=new BufferedWriter(new FileWriter(file));
            bf.write(s.toString());
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
