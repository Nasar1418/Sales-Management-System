//import java.net.ConnectException;
//import  java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//public class Main {
//    public static void main(String[] args) {
//        System.out.println("hello world");
//        try{
//            String driver="com.mysql.cj.jdbc.Driver";
//            // Database connection parameters
//            String url = "jdbc:mysql://localhost:3306/javaprojects";
//            String username = "root";
//            String password = "12345";
//            Class.forName(driver);
////            // Establish a database connection
////            Connection connection = DriverManager.getConnection(url, username, password);
////
////            // Create a statement
////            Statement statement = connection.createStatement();
////
////            // Execute an SQL query to select all products
////            String query = "SELECT * FROM products";
////            ResultSet resultSet = statement.executeQuery(query);
////
////            // Display the product data
////            System.out.println("Product List:");
////            System.out.println("ID\tProduct Name\tSelling Price\tActual Price");
////
////            while (resultSet.next()) {
////                int id = resultSet.getInt("product_id");
////                String productName = resultSet.getString("product_name");
////                double sellingPrice = resultSet.getDouble("selling_price");
////                double actualPrice = resultSet.getDouble("actual_price");
////
////                System.out.println(id + "\t" + productName + "\t" + sellingPrice + "\t" + actualPrice);
//            Connection connection = DriverManager.getConnection(url, username, password);
//            String insertQuery = "INSERT INTO products (product_id, product_name, actual_price, selling_price) VALUES (?, ?, ?, ?)";
//
//            // Create a PreparedStatement
//            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//            preparedStatement.setInt(1, product.getId());
//            preparedStatement.setString(2, product.getName());
//            preparedStatement.setDouble(3, product.getActualPrice());
//            preparedStatement.setDouble(4, product.getSellingPrice());
//
//            // Execute the INSERT statement
//            int rowsAffected = preparedStatement.executeUpdate();
//
//            if (rowsAffected > 0) {
//                System.out.println("New product added to the database.");
//            } else {
//                System.out.println("Failed to add the product to the database.");
//            }
//
//            // Close the PreparedStatement and database connection
//            preparedStatement.close();
//            connection.close();
//            }
//
//            // Close the resources
//            resultSet.close();
//            statement.close();
//            connection.close();
//        }catch (Exception e){
//            System.out.println(e);
//        }
//
//    }
//}
