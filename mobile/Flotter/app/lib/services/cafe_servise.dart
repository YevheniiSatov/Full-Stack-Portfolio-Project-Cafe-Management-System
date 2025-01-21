import 'package:http/http.dart' as http;
import 'package:flutter/foundation.dart';  // Для использования debugPrint()

class CafeService {
  final String baseUrl = 'http://localhost:8080/api';

  Future<String?> getCafeByLocation(double latitude, double longitude) async {
    try {
      // Логируем параметры запроса с использованием debugPrint()
      debugPrint('Отправка запроса на сервер с координатами: широта = $latitude, долгота = $longitude');

      final response = await http.post(
        Uri.parse('$baseUrl/geo/location'),
        headers: {'Content-Type': 'application/json; charset=UTF-8'},
        body: '{"latitude": $latitude, "longitude": $longitude}',
      );

      // Логируем статус ответа и заголовки
      debugPrint('Получен ответ с кодом: ${response.statusCode}');
      debugPrint('Заголовки ответа: ${response.headers}');
      debugPrint('Тело ответа: ${response.body}');  // Логируем ответ сервера

      // Проверяем тип содержимого, чтобы убедиться, что он текстовый
      if (response.headers['content-type']?.contains('text/plain') ?? false) {
        // Обрабатываем ответ как строку
        String cafeName = response.body.trim(); // убираем лишние пробелы

        // Логируем полученное название кафе
        debugPrint('Получено название кафе: $cafeName');

        // Если кафе называется "Maj", заменяем на "MÁJ"
        if (cafeName == 'Maj') {
          debugPrint('Кафе "Maj" найдено, заменяем на "MÁJ"');
          cafeName = 'MÁJ';
        }

        return cafeName; // Возвращаем название кафе
      } else {
        debugPrint('Неподдерживаемый тип контента: ${response.headers['content-type']}');
        return null;
      }
    } catch (e) {
      // Логируем ошибку, если она возникла
      debugPrint('Произошла ошибка при выполнении запроса: $e');
      return null;
    }
  }
}
