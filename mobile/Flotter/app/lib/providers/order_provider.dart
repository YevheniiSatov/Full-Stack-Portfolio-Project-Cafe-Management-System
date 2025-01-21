import 'package:flutter/material.dart';
import '../models/order.dart';
import '../services/order_service.dart';

class OrderProvider with ChangeNotifier {
  List<Order> _orders = [];
  bool _isLoading = false;
  String _error = '';

  List<Order> get orders => _orders;
  bool get isLoading => _isLoading;
  String get error => _error;

  final OrderService _orderService = OrderService();

  // Получение заказов пользователя
  Future<void> fetchUserOrders(String authToken) async {
    _isLoading = true;
    notifyListeners();

    try {
      _orders = await _orderService.fetchUserOrders(authToken);
      _error = '';
    } catch (e) {
      _error = 'Ошибка при загрузке истории заказов.';
      print('Error fetching user orders: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // Получение заказов кафе
  Future<void> fetchCafeOrders(String email, String authToken) async {
    _isLoading = true;
    notifyListeners();

    try {
      _orders = await _orderService.fetchCafeOrders(email, authToken);
      _error = '';
    } catch (e) {
      _error = 'Ошибка при загрузке заказов кафе.';
      print('Error fetching cafe orders: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // Получение заказов курьера
  Future<void> fetchCourierOrders(String authToken) async {
    _isLoading = true;
    notifyListeners();

    try {
      _orders = await _orderService.fetchCourierOrders(authToken);
      _error = '';
    } catch (e) {
      _error = 'Ошибка при загрузке заказов курьера.';
      print('Error fetching courier orders: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // Принятие заказа кафе
  Future<void> acceptOrder(int orderId, String authToken) async {
    try {
      await _orderService.acceptOrder(orderId, authToken);
      _orders.removeWhere((order) => order.id == orderId);
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  // Принятие заказа курьером
  Future<void> acceptCourierOrder(int orderId, String authToken) async {
    try {
      await _orderService.acceptCourierOrder(orderId, authToken);
      _orders.removeWhere((order) => order.id == orderId);
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  Future<void> submitOrder(Map<String, dynamic> orderData, String authToken) async {
    try {
      await _orderService.submitOrder(orderData, authToken);
      _error = '';
      notifyListeners();
    } catch (e) {
      _error = 'Ошибка при оформлении заказа: ${e.toString()}';
      notifyListeners();
      throw e;
    }
  }
}
