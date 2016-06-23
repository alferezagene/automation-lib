package nz.co.thebteam.AutomationLibrary.Utilities;

import com.mockrunner.mock.jdbc.MockResultSet;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.fail;

public class JDBCHelper {

    String URL;
    ResultSet rs;
    Statement stmt;
    Connection conn;

    public JDBCHelper(String URL, String query) {
        this.URL = URL;
        createConnection();
        sendQuery(query);
    }

    //this is mainly for testing purposes as usually we would get a results set via the conn
    public JDBCHelper(ResultSet rs) {
        this.rs = rs;
    }

    public void createConnection() {
        try {
            conn = DriverManager.getConnection(URL, "", "");
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendQuery(String query) {
        try {
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

        return data;
    }


}
