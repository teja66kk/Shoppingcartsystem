import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3307/ShoppingCartDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Tejakk@99";

    private Connection connection;

    public CartService() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean productExists(int productId) {
        boolean exists = false;
        try {
            String query = "SELECT id FROM Product WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            exists = resultSet.next(); // Check if any rows are returned
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public void addItemToCart(Product product, int quantity) {
        if (productExists(product.getId())) {
            try {
                String query = "INSERT INTO CartItem (product_id, quantity) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, product.getId());
                preparedStatement.setInt(2, quantity);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Product with ID " + product.getId() + " does not exist.");
        }
    }

    public List<CartItem> getCart() {
        List<CartItem> cartItems = new ArrayList<>();
        try {
            String query = "SELECT * FROM CartItem";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");
                // Fetch product details from the database
                Product product = getProductById(productId);
                if (product != null) {
                    cartItems.add(new CartItem(product, quantity));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    private Product getProductById(int productId) {
        try {
            String query = "SELECT * FROM Product WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                return new Product(id, name, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeItemFromCart(CartItem item) {
        try {
            String query = "DELETE FROM CartItem WHERE product_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, item.getProduct().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
