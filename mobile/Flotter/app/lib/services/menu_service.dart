import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/menu_item.dart';

class MenuService {
  final String baseUrl = 'http://localhost:8080/api';

  // Получение меню по кафе
  Future<List<MenuItem>> fetchMenu(String cafe) async {
    final response = await http.get(Uri.parse('$baseUrl/menu?cafe=$cafe'));

    if (response.statusCode == 200) {
      List<dynamic> data = json.decode(response.body);
      return data.map((json) => MenuItem.fromJson(json)).toList();
    } else {
      throw Exception('Ошибка при загрузке меню');
    }
  }

  // Отправка заказа
  Future<void> submitOrder(Map<String, dynamic> orderData) async {
    final response = await http.post(
      Uri.parse('$baseUrl/orders'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(orderData),
    );

    if (response.statusCode != 200) {
      throw Exception('Ошибка при оформлении заказа');
    }
  }
}
