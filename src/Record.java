import java.lang.reflect.Array;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Record {
    private String name;
    private String birthsday;
    private String sex;

    public Record(ArrayList<String> record) {
        try {
            name = record.get(0);
            birthsday = record.get(1);
            sex = record.get(2);
        } catch (Throwable e) {
            System.out.println("I`am so sorry, but record is impossible");
        }
    }

    public Record(String name, String birthsday, String sex) {
        this.name = name;
        this.birthsday = birthsday;
        this.sex = sex;
    }

    public void push(Statement stmt) {
        try {
            stmt.execute(String.format("INSERT INTO employees VALUES ('%s', '%s', '%s')", name, birthsday, sex));
        } catch (Throwable e) {
            System.out.println("Incorrect response ");
        }
    }

    public static void pushListOfRecords(ArrayList<ArrayList<String>> list, Statement stmt) throws SQLException {
        StringBuilder request = new StringBuilder("INSERT INTO employees VAlUES");

        for(ArrayList<String>record :list) {
            String recordString = String.format("('%s', '%s', '%s')", record.get(0), record.get(1), record.get(2));
            request.append(recordString).append(',');
        }
        stmt.execute(request.substring(0, request.length()-1));
    }

    public static Integer getAge(String bi) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate date = LocalDate.parse(bi, formatter);
        return Period.between(date, LocalDate.now()).getYears();
    }
}