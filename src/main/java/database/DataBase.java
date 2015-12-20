package database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import snaq.db.ConnectionPool;



public class DataBase {
    private final String pathdb = "jdbc:sqlite:src\\main\\resources\\Tasks.db";
    private static DataBase instance;
 //   private ConnectionPool pool ;

    private DataBase() {
//        try {
//        Class c = Class.forName("org.sqlite.JDBC");
//        Driver driver = (Driver) c.newInstance();
//        DriverManager.registerDriver(driver);
//        
//        pool = new ConnectionPool("local",
//                5, 10, 30, 180, pathdb, "", "");
//        
//         } catch (ClassNotFoundException ex) {
//                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (InstantiationException ex) {
//                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IllegalAccessException ex) {
//                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SQLException ex) {
//                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//            }
    }

    public static synchronized DataBase getInstance() {
        if (instance == null) {
            
                instance = new DataBase();
           
        }
        return instance;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException{
        
        
     //   return pool.getConnection(2000);
        
        Class.forName("org.sqlite.JDBC"); 
        return DriverManager.getConnection(pathdb);
    }

}
