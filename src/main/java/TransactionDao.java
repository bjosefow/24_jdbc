import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDao {
    private final Connection connection;

    public TransactionDao() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/homebudget", "root", "admin");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(Transation transation) {
        final String sql = "INSERT INTO transaction (type, description, amount, date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, transation.getType());
            statement.setString(2, transation.getDescription());
            statement.setDouble(3, transation.getAmount());
            statement.setString(4, transation.getDate());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                transation.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean update(Transation transation) {
        final String sql = "UPDATE transaction SET type = ?, description = ?, amount = ?, date = ? where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, transation.getType());
            statement.setString(2, transation.getDescription());
            statement.setDouble(3, transation.getAmount());
            statement.setString(4, transation.getDate());
            statement.setInt(5, transation.getId());
            int updatedRows = statement.executeUpdate();
            return updatedRows != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        final String sql = "delete from transaction where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int deletedRows = statement.executeUpdate();
            return deletedRows != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Transation> getTransations(String type) {
        List<Transation> transations = new ArrayList<>();
        final String sql = "Select * from transaction where type = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer resId = resultSet.getInt("id");
                String resType = resultSet.getString("type");
                String resDesc = resultSet.getString("description");
                double resAmount = resultSet.getDouble("amount");
                String resDate = resultSet.getString("date");
                transations.add(new Transation(resId, resType, resDesc, resAmount, resDate));
            }
            return transations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transations;
    }

}
