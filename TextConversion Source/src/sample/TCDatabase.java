package sample;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by alperen on 18.06.2016.
 */
public class TCDatabase {
    String framework = "embedded";
    String protocol = "jdbc:derby:";
    String dbName = "TextConversionSelectedDatabase"; // the name of the database
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String tableName = "TCTable";

    public boolean firstInstall(){

        Connection conn = null;
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";

        try{
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(protocol + dbName+";create=true");
            conn.close();
            return createTable();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkDb(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(protocol + dbName+";create=false");

            ArrayList tableList=getAllUserDefinedTables();

            if(tableList.size()==0) createTable();

            conn.close();

            return true;
        }
        catch (SQLException e) {
            String msg = e.getMessage();
            e.printStackTrace();
            return firstInstall() && msg.contains("Database") && msg.contains(dbName) && msg.contains("not found") ;
        }
    }

    public ArrayList<String> getAllUserDefinedTables(){
        ArrayList<String> tableList=null;
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(protocol + dbName+";create=false");
            ResultSet results = conn.createStatement().executeQuery("SELECT TABLENAME FROM SYS.SYSTABLES WHERE TABLETYPE='T'");
            tableList=new ArrayList<>();
            if(results!=null){
                while (results.next()){
                    tableList.add(results.getString(1));
                }
            }
            return tableList;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean createTable(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(protocol + dbName+";create=false");

            conn.createStatement().execute("create table "+tableName+" (id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), elementId varchar(300))");

            conn.close();

            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    public void addEntries(ArrayList<String> controlElementIds){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(protocol + dbName+";create=false");
            PreparedStatement psInsert=null;
            for(String s :controlElementIds){
                psInsert= conn.prepareStatement("insert into "+tableName+" (elementId) values (?)");

                psInsert.setString(1, s);
                psInsert.executeUpdate();
            }
            conn.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllIds(){
        ArrayList<String> list=null;
        Connection conn = null;
        ResultSet rs=null;
        try{
            conn = DriverManager.getConnection(protocol + dbName+";create=false");

            rs=conn.createStatement().executeQuery("select * from "+tableName);

            list=new ArrayList<>();

            if(rs!=null){
                while (rs.next()){
                    String value=rs.getString("elementId");
                    list.add(value);
                }
            }
            conn.close();
            return list;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteAllEntries(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(protocol + dbName+";create=false");

            conn.createStatement().executeUpdate("DELETE from "+tableName);

            conn.close();

            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
