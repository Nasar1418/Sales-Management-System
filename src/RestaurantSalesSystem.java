

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;

public class RestaurantSalesSystem {
    private JFrame frame;
    private JPanel leftPanel;
    private static JPanel rightPanel;
    private static List<Product> productList = new ArrayList<>();
    private static List<Product> cart = new ArrayList<>(); // Create a cart to store selected products
    static String user = "nasar";
    static double pass = 123;

    private JPanel sellPanel;
    private static JTextArea txtbill;

    public RestaurantSalesSystem() {

        String userid = JOptionPane.showInputDialog(null, "Enter userid");

        String passInput = JOptionPane.showInputDialog(null, "Enter password");
        double passInpu = Double.parseDouble(passInput);
        while (pass != passInpu) {

            passInput = JOptionPane.showInputDialog(null, "Incorrect Password: \nEnter Correct password");
            passInpu = Double.parseDouble(passInput);
        }

        frame = new JFrame("Restaurant Sales System");
        frame.setLayout(new BorderLayout());

        // Create a left panel and set its preferred width to 20% of the screen width
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(7, 1));

        JButton homeButton = new JButton("Home");
        JButton sellButton = new JButton("Sell");
        JButton salespersonButton = new JButton("Cart");
        JButton profitButton = new JButton("Payment");
        JButton profiButton = new JButton("Profit");

        leftPanel.add(homeButton);
        leftPanel.add(sellButton);
        leftPanel.add(salespersonButton);
        leftPanel.add(profitButton);
        leftPanel.add(profiButton);

        rightPanel = new JPanel();

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        initializeMenu(); // Initialize menu data

        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               showHomePage();

            }
        });

        sellButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showSellPage();
            }

        });

        salespersonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // calculateCartTotalPrice();
                showSalespersonPage();
            }
        });

        profitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update rightPanel content for "Profit"

                showreceipt();
            }
        });

        profiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update rightPanel content for "Profit"
                // calculateCartTotalPrice();
                showprofipage();
            }
        });

        // Add the "Add Product" button to the left panel
        JButton addButton = new JButton("Add Product");
        leftPanel.add(addButton);

        // Add an action listener to the "Add Product" button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewProductToMenu();
            }
        });

        // Add a "Delete Product" button to the left panel
        JButton deleteButton = new JButton("Delete Product");
        leftPanel.add(deleteButton);

// Add an action listener to the "Delete Product" button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProductFromDatabase();
            }
        });


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }


    private void initializeMenu() {
        // Create product objects with unique IDs, actual prices, and selling prices
        Product product1 = new Product(1, "Chicken Dum Biryani", 130, 150);
        Product product2 = new Product(2, "Boneless Chicken Biryani", 160, 200);
        Product product3 = new Product(3, "Fried Chicken Biryani", 140, 160);
        Product product4 = new Product(4, "Thums Up", 40, 50);

        // Populate the product list
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
    }



    // Method to add a new product to the local menu list
    private void addNewProductToMenu() {

        String productIdInput = JOptionPane.showInputDialog(null, "Enter Product ID:");
        int productId = Integer.parseInt(productIdInput);

        String productName = JOptionPane.showInputDialog(null, "Enter Product Name:");
        String actualPriceInput = JOptionPane.showInputDialog(null, "Enter Actual Price:");
        double actualPrice = Double.parseDouble(actualPriceInput);

        String sellingPriceInput = JOptionPane.showInputDialog(null, "Enter Selling Price:");
        double sellingPrice = Double.parseDouble(sellingPriceInput);

        // Check if the product ID is unique in your local list
        if (isUniqueProductId(productId)) {
            // Create a new Product instance and add it to your local list
            Product newProduct = new Product(productId, productName, actualPrice, sellingPrice);
            productList.add(newProduct);

            // Inform the user that the product has been added to the local list
            JOptionPane.showMessageDialog(null, "Product added successfully to the local list!");

            // Now, insert this product into the database by calling a method to do so
            insertProductIntoDatabase(newProduct);
        } else {
            // Inform the user that the product ID is not unique in the local list
            JOptionPane.showMessageDialog(null, "Product ID already exists in the local list. Please choose a unique ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to check if the product ID is unique in the local list
    private boolean isUniqueProductId(int productId) {
        for (Product product : productList) {
            if (product.getId() == productId) {
                return false; // Product ID already exists in the local list
            }
        }
        return true; // Product ID is unique in the local list
    }

    // Method to insert a new product into the database
    private void insertProductIntoDatabase(Product product) {
        // Define your database connection URL, username, and password
//        String url = "jdbc:mysql://your-database-host:3306/your-database-name";
//        String username = "your-username";
//        String password = "your-password";

        try {
            String driver="com.mysql.cj.jdbc.Driver";
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/javaprojects";
            String username = "root";
            String password = "12345";
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            String insertQuery = "INSERT INTO products (product_id, product_name, actual_price, selling_price) VALUES (?, ?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setDouble(3, product.getActualPrice());
            preparedStatement.setDouble(4, product.getSellingPrice());

            // Execute the INSERT statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("New product added to the database.");
            } else {
                System.out.println("Failed to add the product to the database.");
            }

            // Close the PreparedStatement and database connection
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to delete a product from the database
    private void deleteProductFromDatabase() {
        String productIdInput = JOptionPane.showInputDialog(null, "Enter Product ID to delete:");
        int productId = Integer.parseInt(productIdInput);

        // Check if the product exists in the database
        if (isProductExistsInDatabase(productId)) {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this product?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String driver = "com.mysql.cj.jdbc.Driver";
                    // Database connection parameters
                    String url = "jdbc:mysql://localhost:3306/javaprojects";
                    String username = "root";
                    String password = "12345";
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url, username, password);
                    String deleteQuery = "DELETE FROM products WHERE product_id = ?";

                    // Create a PreparedStatement
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setInt(1, productId);

                    // Execute the DELETE statement
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Product with ID " + productId + " deleted successfully.");
                        // Refresh the product data after deletion
                        fetchProductDataFromDatabase();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete the product.");
                    }

                    // Close the PreparedStatement and database connection
                    preparedStatement.close();
                    connection.close();
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Product with ID " + productId + " does not exist in the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to check if a product with a specific ID exists in the database
    private boolean isProductExistsInDatabase(int productId) {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/javaprojects";
            String username = "root";
            String password = "12345";
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            String selectQuery = "SELECT * FROM products WHERE product_id = ?";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, productId);

            // Execute the SELECT statement
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a product exists with the specified ID
            boolean productExists = resultSet.next();

            // Close the PreparedStatement and database connection
            preparedStatement.close();
            connection.close();

            return productExists;
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    private void showHomePage() {
        fetchProductDataFromDatabase();
        // Create a panel for the home page content
        JPanel homePagePanel = new JPanel();
        homePagePanel.setLayout(new GridLayout(0, 3));

        // Add headings
        JLabel productIdLabel = new JLabel("Product ID");
        JLabel productLabel = new JLabel("Products");
        JLabel priceLabel = new JLabel("Selling Price (Rupees)");

        // Set headings style
        productIdLabel.setHorizontalAlignment(JLabel.CENTER);
        productLabel.setHorizontalAlignment(JLabel.CENTER);
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        productIdLabel.setForeground(Color.RED);
        productLabel.setForeground(Color.RED);
        priceLabel.setForeground(Color.RED);
        Font boldFont = new Font(productLabel.getFont().getName(), Font.BOLD, productLabel.getFont().getSize());
        productIdLabel.setFont(boldFont);
        productLabel.setFont(boldFont);
        priceLabel.setFont(boldFont);

        homePagePanel.add(productIdLabel);
        homePagePanel.add(productLabel);
        homePagePanel.add(priceLabel);

        // Add product list, IDs, and selling prices
        for (Product product : productList) {
            JLabel productId = new JLabel(Integer.toString(product.getId()));
            JLabel productName = new JLabel(product.getName());
            JLabel price = new JLabel(Double.toString(product.getSellingPrice()));

            // Set text alignment and font
            productId.setHorizontalAlignment(JLabel.CENTER);
            productName.setHorizontalAlignment(JLabel.CENTER);
            price.setHorizontalAlignment(JLabel.CENTER);

            homePagePanel.add(productId);
            homePagePanel.add(productName);
            homePagePanel.add(price);
        }

        // Center the content in the right panel
        homePagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePagePanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Clear and set the right panel with the home page content
        rightPanel.removeAll();
        rightPanel.add(homePagePanel);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    // ...

    private void showSellPage() {
        // Create a panel for the sell page content
        sellPanel = new JPanel();
        sellPanel.setLayout(new GridLayout(0, 4)); // Reduced the number of columns

        // Add headings
        JLabel productIdLabel = new JLabel("Product ID");
        JLabel productLabel = new JLabel("Products");
        JLabel priceLabel = new JLabel("Selling Price (Rupees)");
        JLabel addButton = new JLabel("Add"); // Added "Add" column header

        // Set headings style
        productIdLabel.setHorizontalAlignment(JLabel.CENTER);
        productLabel.setHorizontalAlignment(JLabel.CENTER);
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        addButton.setHorizontalAlignment(JLabel.CENTER);

        productIdLabel.setForeground(Color.RED);
        productLabel.setForeground(Color.RED);
        priceLabel.setForeground(Color.RED);
        addButton.setForeground(Color.RED);

        Font boldFont = new Font(productLabel.getFont().getName(), Font.BOLD, productLabel.getFont().getSize());
        productIdLabel.setFont(boldFont);
        productLabel.setFont(boldFont);
        priceLabel.setFont(boldFont);
        addButton.setFont(boldFont);

        sellPanel.add(productIdLabel);
        sellPanel.add(productLabel);
        sellPanel.add(priceLabel);
        sellPanel.add(addButton); // Add the "Add" column header

        // Add product list, IDs, selling prices, and "Add" buttons
        for (Product product : productList) {
            JLabel productId = new JLabel(Integer.toString(product.getId()));
            JLabel productName = new JLabel(product.getName());
            JLabel price = new JLabel(Double.toString(product.getSellingPrice()));
            JButton addButto = new JButton("Add"); // Added "Add" buttons

            // Set text alignment
            productId.setHorizontalAlignment(JLabel.CENTER);
            productName.setHorizontalAlignment(JLabel.CENTER);
            price.setHorizontalAlignment(JLabel.CENTER);

            // Add action listener to "Add" buttons
            addButto.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int productId = product.getId();
                    // Add the selected product to the cart
                    Product selectedProduct = getProductById(productId);
                    cart.add(selectedProduct);

                }
            });

            sellPanel.add(productId);
            sellPanel.add(productName);
            sellPanel.add(price);
            sellPanel.add(addButto); // Add the "Add" buttons

        }

        // Create the "OK" button to print the bill
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateCartTotalPrice();
                sellPanel.remove(okButton);
            }
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // calculateCartTotalPrice();
                makeCartEmpty();
                sellPanel.add(okButton);
            }
        });

        // Add the "OK" button to the panel
        sellPanel.add(okButton);
        sellPanel.add(refreshButton);

        // Center the content in the right panel
        sellPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Clear and set the right panel with the sell page content
        rightPanel.removeAll();
        rightPanel.add(sellPanel);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public static void makeCartEmpty() {
        cart.removeAll(cart);
        totalCartPrice = 0;
        totalprofi = 0;
    }

    private void showSalespersonPage() {
        // Create a panel for the salesperson page content
        JPanel salespersonPanel = new JPanel();
        salespersonPanel.setLayout(new GridLayout(0, 3));

        // Add headings for the cart
        JLabel productIdLabel = new JLabel("Product ID");
        JLabel productLabel = new JLabel("Products");
        JLabel priceLabel = new JLabel("Price");

        // Set headings style
        productIdLabel.setHorizontalAlignment(JLabel.CENTER);
        productLabel.setHorizontalAlignment(JLabel.CENTER);
        priceLabel.setHorizontalAlignment(JLabel.CENTER);

        productIdLabel.setForeground(Color.RED);
        productLabel.setForeground(Color.RED);
        priceLabel.setForeground(Color.RED);

        Font boldFont = new Font(productLabel.getFont().getName(), Font.BOLD, productLabel.getFont().getSize());
        productIdLabel.setFont(boldFont);
        productLabel.setFont(boldFont);
        priceLabel.setFont(boldFont);

        salespersonPanel.add(productIdLabel);
        salespersonPanel.add(productLabel);
        salespersonPanel.add(priceLabel);

        // Display the cart
        for (Product product : cart) {
            JLabel productId = new JLabel(Integer.toString(product.getId()));
            JLabel productName = new JLabel(product.getName());
            JLabel price = new JLabel(Integer.toString((int) product.getSellingPrice()));
            productId.setHorizontalAlignment(JLabel.CENTER);
            productName.setHorizontalAlignment(JLabel.CENTER);
            price.setHorizontalAlignment(JLabel.CENTER);

            salespersonPanel.add(productId);
            salespersonPanel.add(productName);
            salespersonPanel.add(price);
        }

        salespersonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        salespersonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Clear and set the right panel with the salesperson page content
        rightPanel.removeAll();
        rightPanel.add(salespersonPanel);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    static double totalprofi = 0;

    public static void calculateTotalProfit() {
        // Calculate the total cart price
        for (Product product : cart) {
            totalprofi += (product.getSellingPrice() - product.getActualPrice());
        }

    }

    private void showprofipage() {

        String userid = JOptionPane.showInputDialog(null, "Enter userid");

        String passInput = JOptionPane.showInputDialog(null, "Enter password");
        double passInpu = Double.parseDouble(passInput);
        while (pass != passInpu) {

            passInput = JOptionPane.showInputDialog(null, "Incorrect Password: \nEnter Correct password");
            passInpu = Double.parseDouble(passInput);
        }

        String payInput = JOptionPane.showInputDialog(null,
                "Total profit amount is : " + totalprofi + "\nPress any key to continue");
        // Create a panel for the home page content
        JPanel profitPagePanel = new JPanel();
        profitPagePanel.setLayout(new GridLayout(0, 4));

        // Add headings
        JLabel productIdLabel = new JLabel("Product ID");
        JLabel productLabel = new JLabel("Products");
        JLabel priceLabel = new JLabel("Selling Price (Rupees)");
        JLabel actualpriceLabel = new JLabel("Actual Price (Rupees)");

        // Set headings style
        productIdLabel.setHorizontalAlignment(JLabel.CENTER);
        productLabel.setHorizontalAlignment(JLabel.CENTER);
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        actualpriceLabel.setHorizontalAlignment(JLabel.CENTER);
        productIdLabel.setForeground(Color.RED);
        productLabel.setForeground(Color.RED);
        priceLabel.setForeground(Color.RED);
        actualpriceLabel.setForeground(Color.RED);
        Font boldFont = new Font(productLabel.getFont().getName(), Font.BOLD, productLabel.getFont().getSize());
        productIdLabel.setFont(boldFont);
        productLabel.setFont(boldFont);
        priceLabel.setFont(boldFont);
        actualpriceLabel.setFont(boldFont);

        profitPagePanel.add(productIdLabel);
        profitPagePanel.add(productLabel);
        profitPagePanel.add(priceLabel);
        profitPagePanel.add(actualpriceLabel);

        // Add product list, IDs, and selling prices
        for (Product product : cart) {
            JLabel productId = new JLabel(Integer.toString(product.getId()));
            JLabel productName = new JLabel(product.getName());
            JLabel price = new JLabel(Double.toString(product.getSellingPrice()));
            JLabel actualprice = new JLabel(Double.toString(product.getActualPrice()));

            // Set text alignment and font
            productId.setHorizontalAlignment(JLabel.CENTER);
            productName.setHorizontalAlignment(JLabel.CENTER);
            price.setHorizontalAlignment(JLabel.CENTER);
            actualprice.setHorizontalAlignment(JLabel.CENTER);

            profitPagePanel.add(productId);
            profitPagePanel.add(productName);
            profitPagePanel.add(price);
            profitPagePanel.add(actualprice);
        }

        // Center the content in the right panel
        profitPagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profitPagePanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Clear and set the right panel with the home page content
        rightPanel.removeAll();
        rightPanel.add(profitPagePanel);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

//    public void showreceipt() {
//        // Prompt the user to enter payment amount
//        String payInput = JOptionPane.showInputDialog(null,
//                "Enter payment amount (Rupees):\n" + "Total amount is : " + totalCartPrice);
//
//        try {
//            double pay = Double.parseDouble(payInput);
//            double balance = pay - totalCartPrice;
//
//            // Create a bill text area
//            txtbill = new JTextArea();
//            txtbill.setEditable(false);
//            txtbill.setMargin(new Insets(10, 10, 10, 10));
//
//            // Add the bill content to the text area
//            txtbill.append("******************************************************\n");
//            txtbill.append("           POSBILL                                     \n");
//            txtbill.append("*******************************************************\n");
//            txtbill.append("Product              " + "\t" + "Price" + "\t" + "Amount" + "\n");
//
//            for (Product product : cart) {
//                txtbill.append(
//                        product.getId() + "\t" + product.getName() + "\t\t" + product.getSellingPrice() + "\t" + "\n");
//            }
//
//            txtbill.append("\n");
//            txtbill.append("\t" + "\t" + "Total: " + totalCartPrice + " Rupees\n");
//            txtbill.append("\t" + "\t" + "Pay: " + pay + " Rupees\n");
//            txtbill.append("\t" + "\t" + "Balance: " + balance + " Rupees\n"); // Display the calculated balance
//            txtbill.append("\n");
//            txtbill.append("*******************************************************\n");
//            txtbill.append("           THANK YOU, COME AGAIN             \n");
//            calculateTotalProfit();
//            writertofile();
//
//            // Center the content in the right panel
//            txtbill.setAlignmentX(Component.CENTER_ALIGNMENT);
//            txtbill.setAlignmentY(Component.CENTER_ALIGNMENT);
//
//            // Clear and set the right panel with the bill content
//            rightPanel.removeAll();
//            rightPanel.add(txtbill);
//            rightPanel.revalidate();
//            rightPanel.repaint();
//        } catch (NumberFormatException e) {
//            // Handle invalid input
//            JOptionPane.showMessageDialog(null, "Invalid payment amount. Please enter a valid amount.");
//        }
//    }
public void showreceipt() {
    // Prompt the user to enter payment amount
    String payInput = JOptionPane.showInputDialog(null,
            "Enter payment amount (Rupees):\n" + "Total amount is : " + totalCartPrice);

    try {
        double pay = Double.parseDouble(payInput);
        double balance = pay - totalCartPrice;

        // Create a bill text area
        txtbill = new JTextArea();
        txtbill.setEditable(false);
        txtbill.setMargin(new Insets(10, 10, 10, 10));

        // Add the bill content to the text area
        txtbill.append("******************************************************\n");
        txtbill.append("           POSBILL                                     \n");
        txtbill.append("*******************************************************\n");
        txtbill.append("Product              " + "\t" + "Price" + "\t" + "Amount" + "\n");

        for (Product product : cart) {
            txtbill.append(
                    product.getId() + "\t" + product.getName() + "\t\t" + product.getSellingPrice() + "\t" + "\n");
        }

        txtbill.append("\n");
        txtbill.append("\t" + "\t" + "Total: " + totalCartPrice + " Rupees\n");
        txtbill.append("\t" + "\t" + "Pay: " + pay + " Rupees\n");
        txtbill.append("\t" + "\t" + "Balance: " + balance + " Rupees\n"); // Display the calculated balance
        txtbill.append("\n");
        txtbill.append("*******************************************************\n");
        txtbill.append("           THANK YOU, COME AGAIN             \n");
        calculateTotalProfit();

        // Insert the transaction into the database
        insertTransactionIntoDatabase(totalCartPrice, totalprofi);

        // Center the content in the right panel
        txtbill.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtbill.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Clear and set the right panel with the bill content
        rightPanel.removeAll();
        rightPanel.add(txtbill);
        rightPanel.revalidate();
        rightPanel.repaint();
    } catch (NumberFormatException e) {
        // Handle invalid input
        JOptionPane.showMessageDialog(null, "Invalid payment amount. Please enter a valid amount.");
    }
}

    // Method to insert a new transaction into the database
    private void insertTransactionIntoDatabase(double totalSellingAmount, double totalProfitAmount) {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/javaprojects";
            String username = "root";
            String password = "12345";
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            String insertQuery = "INSERT INTO transactions (date, TotalSellingAmount, TotalProfitAmount) VALUES (?, ?, ?)";

            // Get the current date and time
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            // Create a PreparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, formattedDate); // Date
            preparedStatement.setDouble(2, totalSellingAmount); // TotalSellingAmount
            preparedStatement.setDouble(3, totalProfitAmount); // TotalProfitAmount

            // Execute the INSERT statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction added to the database.");
            } else {
                System.out.println("Failed to add the transaction to the database.");
            }

            // Close the PreparedStatement and database connection
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    static double totalCartPrice = 0;

    private static void calculateCartTotalPrice() {

        // Calculate the total cart price
        for (Product product : cart) {
            totalCartPrice += product.getSellingPrice();
        }
    }

    static String fileName = "profit.txt";

    static void writertofile() {
        Date currentDate = new Date();

        // Define a date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Format the current date as a string
        String formattedDate = dateFormat.format(currentDate);

        // Print the formatted date and time
        System.out.println("Current Date and Time: " + formattedDate);

        String content = "Hello, this is a text file!\nWriting to a file in Java is easy.";
        try {
            // Create a FileWriter object
            // FileWriter fileWriter = new FileWriter(fileName);
            FileWriter fileWriter = new FileWriter(fileName, true);

            // Write the content to the file
            fileWriter.write(formattedDate + " . Total Selling amount is :" + totalCartPrice + "\n");
            fileWriter.write(formattedDate + " . Total Profit is :" + totalprofi + "\n");

            // Close the FileWriter
            fileWriter.close();

            System.out.println("Data has been written to the file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private Product getProductById(int productId) {
        for (Product product : productList) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }
    // Create a method to fetch product data from the database
    private static void fetchProductDataFromDatabase() {
        productList.clear(); // Clear the existing product list
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/javaprojects";
            String username = "root";
            String password = "12345";
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            String selectQuery = "SELECT * FROM products";

            // Create a Statement
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Iterate through the result set and create Product instances
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                double actualPrice = resultSet.getDouble("actual_price");
                double sellingPrice = resultSet.getDouble("selling_price");

                Product product = new Product(productId, productName, actualPrice, sellingPrice);
                productList.add(product);
            }

            // Close the Statement and database connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        fetchProductDataFromDatabase();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RestaurantSalesSystem();
            }
        });
    }

    // Product class to store product information
    static class Product {
        private int id;
        private String name;
        private double actualPrice;
        private double sellingPrice;

        public Product(int id, String name, double actualPrice, double sellingPrice) {
            this.id = id;
            this.name = name;
            this.actualPrice = actualPrice;
            this.sellingPrice = sellingPrice;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public double getActualPrice() {
            return actualPrice;
        }
    }
}
