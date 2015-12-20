/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import beans.User;
import database.DataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton
 */
public class UserController {
    
        public User getUserByLoginPassword(String login, String password) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        User user = null;
        try {
            con = DataBase.getInstance().getConnection();
            String query = "select * from User WHERE login=? and password=?;";
            
            pstm = con.prepareStatement(query);
            pstm.setString(1, login);
            pstm.setString(2, password);
            
            rs = pstm.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("Id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setLogin(rs.getString("first_name"));
                user.setPassword(rs.getString("last_name"));
                user.setAdmin(rs.getBoolean("admin"));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return user;
    }
        
 public String createUser(User user) {

      return createUser(user.getFirstname(), user.getLastname(), user.getLogin(),user.getPassword());
    
 }

    public String createUser(String fname, String lname, String login,String password) {
        Connection con = null;
        PreparedStatement pstm = null;
        
        ResultSet rs = null;
        try {
            con = DataBase.getInstance().getConnection();
            
            String query = "select * from User where login = ?";
            pstm = con.prepareStatement(query);
             pstm.setString(1, login);
            rs = pstm.executeQuery();
            
            if (rs.next()) return "user exists";
            
            query = "insert into User (first_name, last_name, login, password) values(?,?,?,?);";
            pstm = con.prepareStatement(query);

            pstm.setString(1, fname);
            pstm.setString(2, lname);
            pstm.setString(3, login);
            pstm.setString(4, password);
                        
            pstm.executeUpdate();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return "";
    }
       
   
  public String updateUser(User user) {

      return updateUser(user.getId(),user.getFirstname(), user.getLastname(), user.getLogin(),user.getPassword());
    
 }

    public String updateUser(int id,String fname, String lname, String login,String password) {
        Connection con = null;
        PreparedStatement pstm = null;
        
        ResultSet rs = null;
        try {
            con = DataBase.getInstance().getConnection();
            
            String query = "select  * from User where Id = ?";
            pstm = con.prepareStatement(query);
             pstm.setInt(1, id);
            rs = pstm.executeQuery();
            
            if (!rs.next()) return "user not exists";
            
            
             query = "update User set first_name=?, last_name=?,login=?, password=? where Id=?;";
            
            pstm = con.prepareStatement(query);

            pstm.setString(1, fname);
            pstm.setString(2, lname);
            pstm.setString(3, login); 
            pstm.setString(4, password);
            pstm.setInt(5, id);
            pstm.executeUpdate();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return "";
    }
       
          
       public User getUserByLogin(String login) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user=new User();
        
        try {
            con = DataBase.getInstance().getConnection();
             String query = "select  * from User where login = ?";
            pstm = con.prepareStatement(query);
             pstm.setString(1, login);
            rs = pstm.executeQuery();
            
            if (!rs.next()) return user;
            user.setId(rs.getInt("Id"));
            user.setFirstname(rs.getString("first_name"));
            user.setLastname(rs.getString("last_name"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setAdmin(rs.getBoolean("admin"));
            
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return user;
    } 

    public void deleteUser(int uid) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DataBase.getInstance().getConnection();
            
            String query = "DELETE FROM Task WHERE user_id =?;";
            pstm = con.prepareStatement(query);

            pstm.setInt(1, uid);

            pstm.executeUpdate();

            query = "DELETE FROM User WHERE Id =?;";
            pstm = con.prepareStatement(query);

            pstm.setInt(1, uid);

            pstm.executeUpdate();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }

    public List<User> getAllUsers() {
       
         Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<User> users = null;
        User user;
        try {
            con = DataBase.getInstance().getConnection();
            String query = "select * from User";

            pstm = con.prepareStatement(query);
          

            rs = pstm.executeQuery();
            users = new ArrayList<User>();

            //    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("Id"));
                user.setFirstname(rs.getString("first_name"));
                user.setLastname(rs.getString("last_name"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                
                users.add(user);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return users;


    }
    
    
}
