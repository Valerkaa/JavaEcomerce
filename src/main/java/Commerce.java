import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

record Product(int id, String name, String description, float price, Category category) {
}

record Category(String name) {
}

class Cart {
    private final List<Product> products;

    public Cart() {
        products = new ArrayList<>();
    }
    public void addToCart(Inventory inventory, Scanner scanner) {
        inventory.displayProducts();
        System.out.print("Введіть ID товару, який ви хочете додати до кошика: ");
        try {
            int productId = Integer.parseInt(scanner.nextLine());
            Product product = inventory.getProductById(productId);
            if (product != null) {
                products.add(product);
                System.out.println("Товар доданий до кошика.");
            } else {
                System.out.println("Товар з таким ID не знайдено.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Невірний ввід. Будь ласка, введіть числове значення ID товару.");
        }
    }

    public void displayCart() {
        System.out.println("Кошик:");
        if (products.isEmpty()) {
            System.out.println("Кошик порожній.");
        } else {
            for (Product product : products) {
                System.out.println("ID: " + product.id() + ", Назва: " + product.name() + ", Ціна: " + product.price());
            }
        }
    }

    public void placeOrder(OrderHistory orderHistory) {
        if (products.isEmpty()) {
            System.out.println("Кошик порожній. Спочатку додайте товари до кошика.");
            return;
        }

        System.out.print("Введіть номер телефону: ");
        Scanner scanner = new Scanner(System.in);
        String phoneNumber = scanner.nextLine();

        System.out.print("Введіть адресу доставки: ");
        String address = scanner.nextLine();

        List<Product> orderedProducts = new ArrayList<>(products);
        Order order = new Order(orderHistory.getOrders().size() + 1, orderedProducts, "В обробці", phoneNumber, address);
        orderHistory.addOrder(order);
        products.clear();
        System.out.println("Замовлення успішно зроблено.");
    }
}


record Order(int orderId, List<Product> orderProducts, String status, String phoneNumber, String address) {
}

    class OrderHistory {
        private final List<Order> orders;

        public OrderHistory() {
            orders = new ArrayList<>();
        }

        public void addOrder(Order order) {
            orders.add(order);
        }

        public List<Order> getOrders() {
            return orders;
        }

        public void displayOrderHistory() {
            if (orders.isEmpty()) {
                System.out.println("Історія замовлень порожня.");
            } else {
                System.out.println("Історія замовлень:");
                for (Order order : orders) {
                    System.out.println("ID замовлення: " + order.orderId() + ", Статус: " + order.status());
                    System.out.println("Номер телефону: " + order.phoneNumber() + ", Адреса: " + order.address());
                    System.out.println("Товари:");
                    for (Product product : order.orderProducts()) {
                        System.out.println("  Назва: " + product.name() + ", Ціна: " + product.price() + 1);
                    }
                    System.out.println();
                }
            }
        }
    }

    class Inventory {
        private final List<Product> products;

        public Inventory() {
            products = new ArrayList<>();
        }

        public void displayProducts() {
            System.out.println("Список товарів:");
            for (Product product : products) {
                System.out.println("ID: " + product.id() + ", Назва: " + product.name() + ", Ціна: " + product.price() + ", Опис: " + product.description());
            }
        }

        public Product getProductById(int id) {
            for (Product product : products) {
                if (product.id() == id) {
                    return product;
                }
            }
            return null;
        }

        public void addProduct(Product product) {
            products.add(product);
        }

        public void searchByName(String name) {
            List<Product> foundProducts = new ArrayList<>();
            String lowerCaseName = name.toLowerCase();
            for (Product product : products) {
                String lowerCaseProductName = product.name().toLowerCase();
                if (lowerCaseProductName.contains(lowerCaseName)) {
                    foundProducts.add(product);
                }
            }
            if (foundProducts.isEmpty()) {
                System.out.println("Товарів з такою назвою не знайдено.");
            } else {
                System.out.println("Знайдені товари:");
                for (Product product : foundProducts) {
                    System.out.println("ID: " + product.id() + ", Назва: " + product.name() + ", Ціна: " + product.price());
                }
            }
        }


        public void searchByCategory(String categoryName) {
            List<Product> foundProducts = new ArrayList<>();
            String lowerCaseCategoryName = categoryName.toLowerCase();
            boolean categoryFound = false;
            for (Product product : products) {
                String productCategory = product.category().name().toLowerCase();
                if (productCategory.contains(lowerCaseCategoryName)) {
                    foundProducts.add(product);
                    categoryFound = true;
                }
            }
            if (!categoryFound) {
                System.out.println("Категорії з такою назвою не знайдено.");
                return;
            }
            if (foundProducts.isEmpty()) {
                System.out.println("Товарів з такою назвою категорії не знайдено.");
            } else {
                System.out.println("Знайдені товари:");
                for (Product product : foundProducts) {
                    System.out.println("ID: " + product.id() + ", Назва: " + product.name() + ", Ціна: " + product.price());
                }
            }
        }
    }

