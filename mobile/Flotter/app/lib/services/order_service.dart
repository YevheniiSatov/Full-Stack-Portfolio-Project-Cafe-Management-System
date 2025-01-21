import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/order.dart';

class OrderService {
  final String baseUrl = 'http://localhost:8080/api';

  Future<List<Order>> fetchUserOrders(String authToken) async {
    final response = await http.get(
      Uri.parse('$baseUrl/orders/user'),
      headers: {
        'Authorization': 'Bearer $authToken',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      List<dynamic> data = json.decode(response.body);
      return data.map((json) => Order.fromJson(json)).toList();
    } else {
      throw Exception('Ошибка при загрузке истории заказов');
    }
  }

  Future<List<Order>> fetchCafeOrders(String email, String authToken) async {
    final response = await http.get(
      Uri.parse('$baseUrl/cafe/orders?email=$email'),
      headers: {
        'Authorization': 'Bearer $authToken',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      List<dynamic> data = json.decode(response.body);
      // Парсим данные в соответствии с моделью OrderResponse
      return data.map((json) => Order.fromJson(json)).toList();
    } else {
      throw Exception('Ошибка при загрузке заказов кафе');
    }
  }

  Future<List<Order>> fetchCourierOrders(String authToken) async {
    final response = await http.get(
      Uri.parse('$baseUrl/courier/orders'),
      headers: {
        'Authorization': 'Bearer $authToken',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      List<dynamic> data = json.decode(response.body);
      return data.map((json) => Order.fromJson(json)).toList();
    } else {
      throw Exception('Ошибка при загрузке заказов курьера');
    }
  }

  Future<void> acceptOrder(int orderId, String authToken) async {
    final response = await http.post(
      Uri.parse('$baseUrl/cafe/orders/$orderId/accept'),
      headers: {
        'Authorization': 'Bearer $authToken',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode != 200) {
      throw Exception('Ошибка при принятии заказа');
    }
  }

  Future<void> acceptCourierOrder(int orderId, String authToken) async {
    final response = await http.post(
      Uri.parse('$baseUrl/courier/orders/$orderId/accept'),
      headers: {
        'Authorization': 'Bearer $authToken',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode != 200) {
      throw Exception('Ошибка при принятии заказа курьером');
    }
  }

  Future<void> submitOrder(Map<String, dynamic> orderData, String authToken) async {
    final response = await http.post(
      Uri.parse('$baseUrl/orders'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $authToken',
      },
      body: json.encode(orderData),
    );

    if (response.statusCode != 200) {
      throw Exception('Ошибка при оформлении заказа');
    }
  }
}
