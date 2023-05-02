package database;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Getter
public enum DbConnection {
 INSTANCE;
    private Connection connection;
    private SessionFactory sessionFactory;




    DbConnection() {

        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/resources/db.properties"));

            String url = properties.getProperty("url");
            String password = properties.getProperty("password");
            String username = properties.getProperty("username");


            this.connection = DriverManager.getConnection(url, username, password);

            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            this.sessionFactory = configuration.buildSessionFactory();

        } catch (IOException | SQLException e) {
            //throw new RuntimeException(e);
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }


    }

