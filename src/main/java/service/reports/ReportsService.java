package service.reports;

//import com.opencsv.CSVWriter;
//import com.opencsv.CSVWriterBuilder;
//import com.opencsv.ICSVWriter;
import database.DbConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportsService {

    private Connection connection;

    public ReportsService() {
        this.connection = DbConnection.INSTANCE.getConnection();
    }

//    public void generateAlphabeticalReport(String fileName) {
//        try {
//            Statement selectStmt = this.connection.createStatement();
//            ResultSet resultSet = selectStmt.executeQuery("SELECT * FROM books ORDER BY title ASC");
//
//            Writer writer = new FileWriter("src/main/resources/" + fileName);
//            ICSVWriter csvWriter = new CSVWriterBuilder(writer)
//                    .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
//                    .withSeparator(';')
//                    .build();
//
//            csvWriter.writeAll(resultSet, false);
//
//            csvWriter.close();
//
//        } catch (SQLException e) {
//
//            System.out.println("db error: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("Error processing file.");
//        }
//    }
}
