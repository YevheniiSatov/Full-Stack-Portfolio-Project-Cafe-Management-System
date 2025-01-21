import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/user.dart';

class AuthService {
  final String baseUrl = 'http://localhost:8080/api';


  // Метод для входа пользователя
  Future<User> login(String email, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/login'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({'email': email, 'password': password}),
    );

    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      final user = User.fromJson(data['user']);
      user.token = data['token'];
      user.role = data['role'];
      return user;
    } else {
      throw Exception('Ошибка при входе');
    }
  }

  // Метод для регистрации пользователя
  Future<void> register(String name, String email, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/register'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'name': name,
        'email': email,
        'password': password,
        'role': 'USER',
      }),
    );

    if (response.statusCode != 200) {
      throw Exception('Ошибка при регистрации');
    }
  }

  // Метод для входа курьера
  Future<User> loginCourier(String email, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/login-courier'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({'email': email, 'password': password}),
    );

    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      final user = User.fromJson(data['user']);
      user.token = data['token'];
      user.role = data['role'];
      return user;
    } else {
      throw Exception('Ошибка при входе курьера');
    }
  }

  // Метод для регистрации курьера
  Future<void> registerCourier(String name, String email, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/register-courier'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'name': name,
        'email': email,
        'password': password,
        'role': 'COURIER',
      }),
    );

    if (response.statusCode != 200) {
      throw Exception('Ошибка при регистрации курьера');
    }
  }
}
