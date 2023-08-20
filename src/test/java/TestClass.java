import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import parts.BracketsChecker;
import parts.EquationChecker;
import parts.EquationDatabaseHandler;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.testng.AssertJUnit.*;

public class TestClass {
    public EquationDatabaseHandler handler;
    String validEquation = "-2*(1.1+4)=x/5";
    @BeforeTest
    public void setUp() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/equation_db";
        String username = "user_user";
        String password = "qwerty12345";
        handler = new EquationDatabaseHandler(jdbcUrl, username, password);

        try {
            handler.connect();
            handler.dropEquationsTable();
            handler.createEquationsDB();
            handler.createEquationsTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBracketsBalanced() {
        assertTrue(BracketsChecker.isEquationBalanced(validEquation));
        assertFalse(BracketsChecker.isEquationBalanced("(-2*(1.1+4)=x/5)"));
    }

    @Test
    public void testEquationChecker() {
        assertTrue(EquationChecker.isEquationValid(validEquation));
        assertFalse(EquationChecker.isEquationValid("-2**(1.1+4)=x/5"));
    }
    @Test
    public void testCountNumbersInEquation() {
        int count = EquationChecker.countNumbersInEquation("-2*(1.1+4)=x/5");
        assertEquals(4, count);
    }

    @Test
    public void testInsertEquationInDatabase() throws SQLException, NoSuchAlgorithmException {

        handler.insertEquation(validEquation);
        assertEquals(validEquation,EquationDatabaseHandler.selectLastInsertEquation());
    }

    @Test
    public void testUniqueInsertEquationInDatabase() throws SQLException, NoSuchAlgorithmException {
        handler.insertEquation("x=0");
        handler.insertEquation("x=1");
        handler.insertEquation("x=0");
        assertEquals("x=1",EquationDatabaseHandler.selectLastInsertEquation());
    }

    @AfterTest
    public void tearDown() {
        try {
            handler.dropEquationsTable();
            handler.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}