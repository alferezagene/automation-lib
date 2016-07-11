package nz.co.thebteam.AutomationLibrary.Utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JDBCHelper {

    String URL;
    ResultSet rs;
    Statement stmt;
    Connection conn = null;
    String userName;
    String password;

    public JDBCHelper(String URL, String userName, String password, String query) {
        this.URL = URL;
        this.userName = userName;
        this.password = password;
        createConnection();
        sendQuery(query);
    }

    //this is mainly for testing purposes as usually we would get a results set via the conn
    public JDBCHelper(ResultSet rs) {
        this.rs = rs;
    }

    //live connection - currently only for oracle DBs
    //You must have ojdbc14.jar in your /jre/lib/ext directory to open an oracle connection successfully
    public void createConnection() {
        // create  the connection object
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //closes after each query in case of assertion errors
    public void sendQuery(String query) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> data = new HashMap<>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                data.put(rsmd.getColumnName(i), new ArrayList<String>());
            }

            while (rs.next()) {
                for (int q = 1; q <= rsmd.getColumnCount(); q++) {
                    data.get(rsmd.getColumnName(q)).add(rs.getString(q));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
        if (conn != null) {
            conn.close();
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


}
