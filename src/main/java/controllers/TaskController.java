/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Task;
import database.DataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Anton
 */
public class TaskController {

    private ArrayList<Task> tasks;

    public ArrayList<Task> GetTasksByUser(int userId, boolean isAdmin) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        Task task = null;
        try {
            con = DataBase.getInstance().getConnection();
            String query;
            if (isAdmin) {
                query = "select Task.*,User.login as author from Task join User " +
                "on User.Id = Task.user_id ;";
                pstm = con.prepareStatement(query);            
           
                } else {
                query = "select Task.*,User.login as author from Task join User " +
                "on User.Id = Task.user_id and Task.user_id=?;";
                 pstm = con.prepareStatement(query);            
                pstm.setInt(1, userId);
                
                }
            
            rs = pstm.executeQuery();
            tasks = new ArrayList<Task>();

            //    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            while (rs.next()) {
                task = new Task();
                task.setId(rs.getInt("Id"));
                task.setName(rs.getString("name"));
                task.setUserId(rs.getInt("user_id"));
                task.setDone(rs.getBoolean("done"));
                task.setStart_date(rs.getDate("start_date"));
                task.setFinish_date(rs.getDate("finish_date"));
                task.setDescription(rs.getString("description"));
                task.setPriority(rs.getInt("priority"));
                task.setAuthor(rs.getString("author"));
                tasks.add(task);
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
        return tasks;
    }

    public void createTask(Task task) {

        int id = createTask(task.getName(), task.isDone(), task.getUserId(),
         task.getStart_date(), task.getFinish_date(),task.getDescription(), task.getPriority());
        task.setId(id);
    }

    public int createTask(String name, Boolean complet, int userId,
            Date start_date, Date finish_date, String description, int priority) {
        Connection con = null;
        PreparedStatement pstm = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            con = DataBase.getInstance().getConnection();
            String query = "insert into Task (name, done, user_id, start_date, finish_date, description, priority) values(?,?,?,?,?,?,?);";
            pstm = con.prepareStatement(query);

            pstm.setString(1, name);
            pstm.setBoolean(2, complet);
            pstm.setInt(3, userId);
            pstm.setDate(4, new java.sql.Date(start_date.getTime()));
            pstm.setDate(5, new java.sql.Date(finish_date.getTime()));

            pstm.setString(6, description);
            pstm.setInt(7, priority);
            
            
            pstm.executeUpdate();
            stm = con.createStatement();
            // We must now recover the Id of the inserted row
            rs = stm.executeQuery("select  last_insert_rowid() as Id");
            rs.next();
            return rs.getInt("Id");

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

        return 0;
    }

    public void deleteTask(int taskId) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DataBase.getInstance().getConnection();

            String query = "DELETE FROM Task WHERE Id =?;";
            pstm = con.prepareStatement(query);

            pstm.setInt(1, taskId);

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

    public void updateTask(int taskId, String name, boolean complet, 
            Date start_date, Date finish_date, String description, int priority) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = DataBase.getInstance().getConnection();
           
            String query = "update Task set name=?, done=?, start_date=?, finish_date=?, description=?, priority=? where Id=?;";
            pstm = con.prepareStatement(query);

            pstm.setString(1, name);
            pstm.setBoolean(2, complet);

            pstm.setDate(3, new java.sql.Date(start_date.getTime()));
            pstm.setDate(4, new java.sql.Date(finish_date.getTime()));
            pstm.setString(5, description);
            pstm.setInt(6, priority);
            pstm.setInt(7, taskId);
            
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

    public void updateTask(Task upTask) {
        updateTask(upTask.getId(), upTask.getName(), upTask.isDone(), 
        upTask.getStart_date(), upTask.getFinish_date(),upTask.getDescription(), upTask.getPriority());
    }

}
