import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class App {
    Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cats", "postgres", "postgres");
    Statement stmt = con.createStatement();
    String[] args;

    public App(String[] args) throws SQLException {
        this.args = args;
    }

    public static String generateString(Integer n) {
        int leftLimit = 97; // буква 'a'
        int rightLimit = 122; // буква 'z'
        String res = "";
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            String generatedString = random.ints(leftLimit, rightLimit + 1).limit(n).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            res += generatedString.substring(0, 1).toUpperCase() + generatedString.substring(1) + " ";
        }
        return res;
    }

    public static ArrayList<String> generateRecord() {
        ArrayList<String> record = new ArrayList<>();
        Random random = new Random();
        int maxDate = 2010;
        int minDate = 1930;
        int year = random.nextInt(maxDate + 1 - minDate) + minDate;
        int month = year % 12 + 1;
        int day = year % 29 + 1;
        int lengthString = 5 + year % 4;
        String date = String.format("%d-%d-%d", year, month, day);
        String sex = year % 2 == 1 ? "Male" : "Female";
        record.add(generateString(lengthString));
        record.add(date);
        record.add(sex);
        return record;
    }

    public static String generateRecords() {
        Integer n = 1000000;
        StringBuilder request = new StringBuilder("INSERT INTO employees VAlUES");

        for (int i = 0; i < n; ++i) {
            ArrayList<String> record = generateRecord();
            String recordString = String.format("('%s', '%s', '%s')", record.get(0), record.get(1), record.get(2));
            request.append(recordString).append(',');
            System.out.println(i);
        }

        System.out.println(request);
        System.out.println(request.substring(0, request.length() - 2));
        return request.substring(0, request.length() - 1);
    }

    public void inputHandler() throws SQLException {
        switch (args[0]) {
            case "1":
                stmt.execute("CREATE TABLE IF NOT EXISTS  employees(" + "name varchar(45) NOT NULL UNIQUE," + "birthsday DATE NOT NULL," + "sex varchar(10) NOT NULL);");
                break;
            case "2":
                try {
                    Record record = new Record(args[1], args[2], args[3]);
                    record.push(stmt);
                } catch (Throwable e) {
                    System.out.println("Incorrect response ");
                }
                break;
            case "3": {
                ResultSet rs = stmt.executeQuery("SELECT *, DATE_PART('YEAR', Age(birthsday)) FROM employees " + "ORDER by name ASC;");
                while (rs.next()) {
                    System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
                }
                break;
            }
            case "4":
                stmt.execute(generateRecords());
                break;
            case "5": {
                Long start = System.currentTimeMillis();
                ResultSet rs = stmt.executeQuery("SELECT * FROM employees " + //, DATE_PART('YEAR', Age(birthsday))
                        "WHERE sex='Male' AND name LIKE 'F%'" + "ORDER by name ASC;");
                Long end = System.currentTimeMillis();

                while (rs.next()) {
                    System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + Record.getAge(rs.getString(2)));
                }
                System.out.printf("Timing: %d ms", end - start);
                break;
            }
            case "0":
                stmt.execute("DROP TABLE employees");
                break;
        }
    }
}