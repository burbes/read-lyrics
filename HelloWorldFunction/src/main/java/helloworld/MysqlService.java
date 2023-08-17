package helloworld;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlService {

    public void saveLyrics(String title, String content) {
        String insertSQL = "INSERT INTO Lyrics (title, artist, content) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, "sabotage");
            preparedStatement.setString(3, content);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving lyrics to database", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
