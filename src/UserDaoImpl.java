import java.sql.*;
import java.util.Scanner;

public class UserDaoImpl implements DAO {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    listUsers();
                    break;
                case "2":
                    findUserById();
                    break;
                case "3":
                    selectUsername();
                    break;
                case "4":
                    deleteUser();
                    break;
                case "5":
                    updateUserPassword();
                    break;
                case "6":
                    System.out.println("Kilépés");
                    return;
                default:
                    System.out.println("Érvénytelen válasz! :/");
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println("Válassz egy opciót:");
        System.out.println("1. Felhasználók listája");
        System.out.println("2. Felhasználó keresése ID alapján");
        System.out.println("3. Felhasználó keresése név alapján");
        System.out.println("4. Felhasználó törlése");
        System.out.println("5. Felhasználó jelszavának módosítása");
        System.out.println("6. Kilépés");
    }

    @Override
    public void listUsers() {
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Név: " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findUserById() {
        System.out.print("Keresett felhasználó ID-je: ");
        int searchID = scanner.nextInt();
        scanner.nextLine(); // Üres sor beolvasás

        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, searchID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Felhasználó adatok:");
                    System.out.println("ID: " + resultSet.getInt("id") + ", Név: " + resultSet.getString("name") + ", Jelszó: " + resultSet.getString("password"));
                } else {
                    System.out.println("Nem található ilyen felhasználó.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectUsername() {
        System.out.print("Keresett névrészlet: ");
        String searchName = scanner.nextLine();

        String query = "SELECT * FROM users WHERE name LIKE ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchName + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean found = false;
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") + ", Név: " + resultSet.getString("name") + ", Jelszó: " + resultSet.getString("password"));
                    found = true;
                }
                if (!found) {
                    System.out.println("Nem található ilyen név.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser() {
        System.out.println("Felhasználó ID-je akit törölni szeretnél : ");
        int userID = scanner.nextInt();
        scanner.nextLine(); // Üres sor beolvasás

        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);

            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("A felhasználót sikeresen töröltük");
            } else {
                System.out.println("Nem található ilyen felhasználó! ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserPassword() {
        System.out.print("Felhasználó ID-je akinek szeretnéd felülírni a jelszavát: ");
        int userID = scanner.nextInt();
        scanner.nextLine(); // Üres sor beolvasás

        System.out.print("Új jelszó: ");
        String newPassword = scanner.nextLine();

        String query = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userID);

            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Jelszó sikeresen megváltoztatva");
            } else {
                System.out.println("Nem található ilyen ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.run();
    }
}
