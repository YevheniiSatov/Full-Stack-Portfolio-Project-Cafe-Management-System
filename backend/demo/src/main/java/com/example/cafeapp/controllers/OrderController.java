    package com.example.cafeapp.controllers;

    import com.example.cafeapp.entities.Order;
    import com.example.cafeapp.services.OrderService;
    import com.example.cafeapp.utils.JwtUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/orders")
    public class OrderController {

        @Autowired
        private OrderService orderService;
        @Autowired
        private JwtUtils jwtUtils;

        @PostMapping
        public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
            try {
                System.out.println("Order received: " + orderRequest);
                orderService.processOrder(orderRequest);  // Обработка заказа
                return ResponseEntity.ok("Order created successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Error processing order: " + e.getMessage());
            }
        }
        @GetMapping("/user")
        public ResponseEntity<List<Order>> getUserOrders(@RequestHeader("Authorization") String token) {
            // Убираем префикс 'Bearer ' перед токеном
            String jwtToken = token.replace("Bearer ", "");

            // Извлекаем email из токена
            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

            // Получаем заказы пользователя по email
            List<Order> orders = orderService.getOrdersByUserEmail(email);

            return ResponseEntity.ok(orders);
        }



        @GetMapping
        public ResponseEntity<List<Order>> getAllOrders() {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        }
    }
