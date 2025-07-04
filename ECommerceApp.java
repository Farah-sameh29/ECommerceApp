import java.time.LocalDate; // import the date library 
import java.util.ArrayList; // import the list library
import java.util.List;
// shipping interface
interface Shipping{
    String getName();
    double getWeight();
}
// class product
class Product implements Shipping{
    double weight;
    String name;
    double price;
    int quantity;
    boolean isExpirable; //el salahya 
    LocalDate expiryDate; // el entahet salahytha
    boolean isShipping;
    
   public Product(String name, double price, int quantity, boolean isExpirable, LocalDate expiryDate, boolean isShipping, double weight) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isExpirable = isExpirable;
        this.expiryDate = expiryDate;
        this.isShipping = isShipping;
        this.weight = weight;
   } 
   // Shipping interface implementation
    public String getName() {
        return name;
    }
    public double getWeight() {
        return weight;
    }
}
// class customer
class Customer {
    String name;
    double balance;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
}
// Cart item
class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
   }
}
// class cart 
class Cart{
    List<CartItem> items = new ArrayList<>(); 
// add proudect for cart
public void add(Product product, int quantity) {
        if (quantity > product.quantity) {
            System.out.println("Requested quantity is not available for the product: " + product.name);
            return;
  } 
  items.add(new CartItem(product, quantity));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
// Class Shipping Service
class ShippingService {
    public void ship(List<Shipping> items) {
        double totalWeight = 0;
        System.out.println("** Shipment notice **");
        for (Shipping item : items) {
            System.out.println(item.getName() + "    " + (int)item.getWeight() + "g");
            totalWeight += item.getWeight();
        }
        System.out.println("Total package weight " + (totalWeight / 1000) + "kg");
    }
}
// funcation main
public class ECommerceApp {
    public static void main(String[] args) {
        // Product definition
        Product cheese = new Product("Cheese", 100, 5, true, LocalDate.of(2025, 12, 31), true, 200);
        Product biscuits = new Product("Biscuits", 150, 2, true, LocalDate.of(2025, 8, 1), true, 700);
        Product labtop = new Product("labtop", 5000, 3, false, null, true, 800);
        Product scratchCard = new Product("Scratch Card", 50, 10, false, null, false, 0);
// Defining the customer
Customer customer = new Customer("Ahmed", 1000);
//  Defining the cart
Cart cart = new Cart();
        cart.add(cheese, 2);
        cart.add(biscuits, 1);
        cart.add(scratchCard, 1);
// Checkout process        
 checkout(customer, cart);
    }
// funcation pay
public static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("The cart is empt!");
            return;
        }

        double subtotal = 0;
        double shipping = 0;
        double totalWeight = 0;
        List<Shipping> shippingItems = new ArrayList<>();
// Check the items in the cart
 for (CartItem item : cart.items) {
            Product p = item.product;
            if (item.quantity > p.quantity) {
                System.out.println("Requested quantity is not available for the product: " + p.name);
                return;
            }
            if (p.isExpirable && p.expiryDate != null && p.expiryDate.isBefore(LocalDate.now())) {
                System.out.println("This product is expired: " + p.name);
                return;
            }
            subtotal += p.price * item.quantity;
            if (p.isShipping) {
                for (int i = 0; i < item.quantity; i++) {
                    shippingItems.add(p);
                    totalWeight += p.weight;
               }
           }
}
if (!shippingItems.isEmpty()) {
            shipping = 30; 
            new ShippingService().ship(shippingItems);
        }

        double total = subtotal + shipping;
        if (customer.balance < total) {
            System.out.println("Insufficient balance");
            return;
        }

        customer.balance -= total;
// Print invoice
System.out.println("\n** Checkout receipt **");
        for (CartItem item : cart.items) {
            System.out.println(item.quantity + "x " + item.product.name + "    " + (int)(item.product.price * item.quantity));
        }
        System.out.println("----------------------");
        System.out.println("Subtotal         " + (int)subtotal);
        System.out.println("Shipping         " + (int)shipping);
        System.out.println("Amount           " + (int)total);
        System.out.println("Balance after payment: " + (int)customer.balance);
    }
}
