package parts;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static parts.BracketsChecker.isEquationBalanced;
import static parts.EquationChecker.countNumbersInEquation;
import static parts.EquationChecker.isEquationValid;

public class Main {
    public static void main(String[] args)
    {
        UserInput inputReader = new UserInput();
        String inputEquation;
        String inputRoot;
        String jdbcUrl = "jdbc:mysql://localhost:3306/equation_db";
        String username = "user_user";
        String password = "qwerty12345";

        EquationDatabaseHandler handler = new EquationDatabaseHandler(jdbcUrl, username, password);

        try {
            handler.connect();
            handler.createEquationsDB();
            //Uncomment if you want clear this table
            //handler.dropEquationsTable();
            handler.createEquationsTable();

            while (true) {
                inputEquation = inputReader.readInputEquation();
                if (inputEquation.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the program.");
                    break;
                }
                System.out.println("Brackets balanced on both sides of equation?: " + isEquationBalanced(inputEquation));
                System.out.println("Equation is written correctly?: " + isEquationValid(inputEquation));

                if(isEquationValid(inputEquation))
                {
                    System.out.println("Number of numbers in the equation: " + countNumbersInEquation(inputEquation));
                    inputRoot = inputReader.readInputRoot();
                    if ((RootFinder.findRoot(inputEquation, Double.parseDouble(inputRoot))) == 0) {
                        System.out.println("x = " + inputRoot + " root for this equation");
                        handler.addRootInDatabase(inputEquation, Double.parseDouble(inputRoot));
                        System.out.println("Equations in the database:");
                        handler.displayEquations();
                    }
                    else
                    {
                        handler.insertEquation(inputEquation);
                        System.out.println("x = " + inputRoot + " not root for this equation.");
                        handler.displayEquations();
                    }
                }

                System.out.println("--------------------------------------------");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            try {

                handler.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        inputReader.close();
    }
}
