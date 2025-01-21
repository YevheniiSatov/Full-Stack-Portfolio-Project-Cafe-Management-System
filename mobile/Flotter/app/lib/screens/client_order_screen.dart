import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import '../models/menu_item.dart';
import '../services/menu_service.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../providers/order_provider.dart';

class ClientOrderScreen extends StatefulWidget {
  @override
  _ClientOrderScreenState createState() => _ClientOrderScreenState();
}

class _ClientOrderScreenState extends State<ClientOrderScreen> {
  List<MenuItem> _menuItems = [];
  Map<int, int> _selectedItems = {};
  String _cafe = '';
  String _address = '';
  String _clientName = '';
  String _clientPhone = '';
  String _message = '';
  bool _loadingCafe = false;
  bool _isSubmitting = false;

  @override
  void initState() {
    super.initState();
    _determinePosition();
  }

  Future<void> _determinePosition() async {
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      setState(() {
        _message = 'Службы геолокации отключены.';
      });
      return;
    }

    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        setState(() {
          _message = 'Разрешение на доступ к геолокации отклонено.';
        });
        return;
      }
    }

    if (permission == LocationPermission.deniedForever) {
      setState(() {
        _message = 'Разрешение на доступ к геолокации отклонено навсегда.';
      });
      return;
    }

    Position position = await Geolocator.getCurrentPosition();
    _getCafeByLocation(position.latitude, position.longitude);
  }

  Future<void> _getCafeByLocation(double lat, double lon) async {
    setState(() {
      _loadingCafe = true;
    });
    try {
      final response = await http.post(
        Uri.parse('http://localhost:8080/api/geo/location'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode({'latitude': lat, 'longitude': lon}),
      );

      if (response.statusCode == 200) {
        setState(() {
          _cafe = response.body.trim(); // Используем ответ напрямую
          _loadingCafe = false;
        });
        _fetchMenuItems();
      } else {
        setState(() {
          _message = 'Ошибка при определении кафе.';
          _loadingCafe = false;
        });
      }
    } catch (e) {
      setState(() {
        _message = 'Ошибка при запросе кафе.';
        _loadingCafe = false;
      });
    }
  }



  Future<void> _fetchMenuItems() async {
    try {
      List<MenuItem> items = await MenuService().fetchMenu(_cafe);
      setState(() {
        _menuItems = items;
      });
    } catch (e) {
      setState(() {
        _message = 'Ошибка при загрузке меню: ${e.toString()}';
      });
    }
  }

  void _addItemToOrder(MenuItem item) {
    setState(() {
      _selectedItems.update(item.id, (quantity) => quantity + 1,
          ifAbsent: () => 1);
    });
  }

  void _submitOrder() async {
    if (_clientName.isEmpty ||
        _clientPhone.isEmpty ||
        _address.isEmpty ||
        _selectedItems.isEmpty) {
      setState(() {
        _message = 'Пожалуйста, заполните все поля и выберите товары.';
      });
      return;
    }

    setState(() {
      _isSubmitting = true;
      _message = '';
    });

    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    final authToken = authProvider.user?.token ?? '';
    final clientEmail = authProvider.user?.email ?? '';

    final orderData = {
      'items': _selectedItems.entries
          .map((e) => {'id': e.key, 'quantity': e.value})
          .toList(),
      'address': _address,
      'cafe': _cafe,
      'clientName': _clientName,
      'clientPhone': _clientPhone,
      'clientEmail': clientEmail,
    };

    try {
      await Provider.of<OrderProvider>(context, listen: false)
          .submitOrder(orderData, authToken);
      setState(() {
        _message = 'Заказ успешно оформлен!';
        _selectedItems.clear();
      });
    } catch (e) {
      setState(() {
        _message = 'Ошибка при оформлении заказа: ${e.toString()}';
      });
    } finally {
      setState(() {
        _isSubmitting = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Оформление заказа'),
        ),
        body: SingleChildScrollView(
          child: Padding(
            padding: EdgeInsets.all(16.0),
            child: Column(
              children: [
                _loadingCafe
                    ? CircularProgressIndicator()
                    : Text('Ваше кафе: $_cafe'),
                TextField(
                  decoration: InputDecoration(labelText: 'Ваше имя'),
                  onChanged: (value) => _clientName = value,
                ),
                TextField(
                  decoration: InputDecoration(labelText: 'Ваш телефон'),
                  onChanged: (value) => _clientPhone = value,
                ),
                TextField(
                  decoration: InputDecoration(labelText: 'Ваш адрес'),
                  onChanged: (value) => _address = value,
                ),
                SizedBox(height: 20),
                Text('Меню', style: TextStyle(fontSize: 18)),
                _menuItems.isNotEmpty
                    ? ListView.builder(
                  shrinkWrap: true,
                  physics: NeverScrollableScrollPhysics(),
                  itemCount: _menuItems.length,
                  itemBuilder: (context, index) {
                    final item = _menuItems[index];
                    return ListTile(
                      title: Text('${item.name} - ${item.price} ₽'),
                      trailing: IconButton(
                        icon: Icon(Icons.add),
                        onPressed: () => _addItemToOrder(item),
                      ),
                    );
                  },
                )
                    : _loadingCafe
                    ? CircularProgressIndicator()
                    : Text('Меню недоступно'),
                SizedBox(height: 20),
                Text('Корзина', style: TextStyle(fontSize: 18)),
                _selectedItems.isNotEmpty
                    ? Column(
                  children: _selectedItems.entries.map((entry) {
                    final item = _menuItems
                        .firstWhere((i) => i.id == entry.key);
                    return ListTile(
                      title: Text('${item.name} x${entry.value}'),
                    );
                  }).toList(),
                )
                    : Text('Корзина пуста'),
                SizedBox(height: 20),
                ElevatedButton(
                  onPressed: _isSubmitting ? null : _submitOrder,
                  child: _isSubmitting
                      ? CircularProgressIndicator(
                    valueColor:
                    AlwaysStoppedAnimation<Color>(Colors.white),
                  )
                      : Text('Отправить заказ'),
                ),
                if (_message.isNotEmpty)
                  Padding(
                    padding: EdgeInsets.all(8.0),
                    child: Text(
                      _message,
                      style: TextStyle(
                          color: _message.contains('успешно')
                              ? Colors.green
                              : Colors.red),
                    ),
                  ),
              ],
            ),
          ),
        ));
  }
}
