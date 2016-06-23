package AutomationLibTests;

import com.mockrunner.mock.jdbc.MockResultSet;
import nz.co.thebteam.AutomationLibrary.Utilities.JDBCHelper;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class JDBCHelperTests {


    @Test
    public void jdbctest() {
        //for testing purposes we make a little mocked resultSet here
        MockResultSet rs = new MockResultSet("myMock");
        rs.addColumn("columnA", new Integer[]{1, 2, 3, 4, 5, 6});
        rs.addColumn("columnB", new String[]{"Column B Value", "ewibble", "wobble", "jellybeans", "diamond rings", ""});
        rs.addColumn("columnC", new String[]{"2", "", "alsdjasd", "asdads", "", ""});

        JDBCHelper j = new JDBCHelper(rs);
        HashMap<String, List<String>> data = j.getData();

        assertTrue(data.get("columnA").get(0).equals("1"));
        assertTrue(data.get("columnA").get(5).equals("6"));
        assertTrue(data.get("columnB").get(0).equals("Column B Value"));
        assertTrue(data.get("columnB").get(3).equals("jellybeans"));
        assertTrue(data.get("columnC").get(2).equals("alsdjasd"));
        assertTrue(data.get("columnC").get(1).equals(""));

    }
}
