package connector;

import java.sql.Connection;
import java.sql.DriverManager;

public class connector {
    public static Connection getConnection(){
        Connection conn;
        try
        {
            conn = DriverManager.getConnection("jdbc:postgresql://database1.c8hjhpmlhlok.us-east-1.rds.amazonaws.com/database1","postgres","taco1234");
            return conn;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
