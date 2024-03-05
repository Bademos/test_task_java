import java.sql.*;

public class Strange {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        App app = new App(args);
        app.inputHandler();
    }
}

