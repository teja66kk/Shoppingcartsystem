import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create a cart service
        CartService cartService = new CartService();

        // Add products to cart
        Product product1 = new Product(1, "Product 1", 10.0);
        Product product2 = new Product(2, "Product 2", 20.0);
        cartService.addItemToCart(product1, 2);
        cartService.addItemToCart(product2, 1);
        // Display cart items
        System.out.println("Cart items:");
        List<CartItem> cartItems = cartService.getCart();
        for (CartItem item : cartItems) {
            System.out.println(item.getProduct().getName() + " - Quantity: " + item.getQuantity());
        }
        // Remove a product from cart (assuming there's at least one item in the cart)
        if (!cartItems.isEmpty()) {
            cartService.removeItemFromCart(cartItems.get(0));
        }
        // Display cart items after removal
        System.out.println("\nCart items after removal:");
        for (CartItem item : cartService.getCart()) {
            System.out.println(item.getProduct().getName() + " - Quantity: " + item.getQuantity());
        }
        // Close the database connection
        cartService.closeConnection();
    }
}
