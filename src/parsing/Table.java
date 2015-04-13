package parsing;

import parsing.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Szymon on 15/03/19.
 */
public class Table {
    public String name;
    public List<Column> columns;


    public Table(String name) {
        this.name = name;
        columns = new ArrayList<Column>();
    }
}
