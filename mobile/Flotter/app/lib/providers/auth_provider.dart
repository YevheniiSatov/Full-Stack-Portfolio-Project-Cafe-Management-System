import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../models/user.dart';
import '../services/auth_service.dart';

class AuthProvider with ChangeNotifier {
  User? _user;
  bool _isAuthenticated = false;
  String _error = '';

  final _storage = FlutterSecureStorage();

  User? get user => _user;
  bool get isAuthenticated => _isAuthenticated;
  String get error => _error;

  final AuthService _authService = AuthService();

  Future<void> login(String email, String password) async {
    try {
      _user = await _authService.login(email, password);
      _isAuthenticated = true;
      _error = '';
      await _storage.write(key: 'token', value: _user!.token);
      await _storage.write(key: 'role', value: _user!.role);
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      _isAuthenticated = false;
      notifyListeners();
    }
  }

  Future<void> register(String name, String email, String password) async {
    try {
      await _authService.register(name, email, password);
      _error = '';
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  Future<void> loginCourier(String email, String password) async {
    try {
      _user = await _authService.loginCourier(email, password);
      _isAuthenticated = true;
      _error = '';
      await _storage.write(key: 'token', value: _user!.token);
      await _storage.write(key: 'role', value: _user!.role);
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      _isAuthenticated = false;
      notifyListeners();
    }
  }

  Future<void> registerCourier(String name, String email, String password) async {
    try {
      await _authService.registerCourier(name, email, password);
      _error = '';
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  Future<void> logout() async {
    _user = null;
    _isAuthenticated = false;
    await _storage.deleteAll();
    notifyListeners();
  }
}
