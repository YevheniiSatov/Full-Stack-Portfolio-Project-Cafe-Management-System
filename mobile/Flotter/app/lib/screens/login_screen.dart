import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';

class LoginScreen extends StatefulWidget {
  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  String error = '';
  String _selectedRole = 'USER'; // Значение по умолчанию

  void _login() async {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);

    if (_selectedRole == 'USER') {
      await authProvider.login(emailController.text, passwordController.text);
    } else if (_selectedRole == 'COURIER') {
      await authProvider.loginCourier(emailController.text, passwordController.text);
    }

    if (authProvider.isAuthenticated) {
      final role = authProvider.user!.role;
      if (role == 'CAFE') {
        Navigator.pushReplacementNamed(context, '/cafe-orders');
      } else if (role == 'COURIER') {
        Navigator.pushReplacementNamed(context, '/courier-dashboard');
      } else {
        Navigator.pushReplacementNamed(context, '/client-order');
      }
    } else {
      setState(() {
        error = authProvider.error;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Вход'),
        ),
        body: Padding(
          padding: EdgeInsets.all(16.0),
          child: Column(
            children: [
              DropdownButtonFormField<String>(
                value: _selectedRole,
                onChanged: (value) {
                  setState(() {
                    _selectedRole = value!;
                  });
                },
                items: [
                  DropdownMenuItem(value: 'USER', child: Text('Пользователь')),
                  DropdownMenuItem(value: 'COURIER', child: Text('Курьер')),
                  // Добавьте опцию для кафе, если необходимо
                ],
                decoration: InputDecoration(labelText: 'Выберите роль'),
              ),
              TextField(
                controller: emailController,
                decoration: InputDecoration(labelText: 'Email'),
              ),
              TextField(
                controller: passwordController,
                decoration: InputDecoration(labelText: 'Пароль'),
                obscureText: true,
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: _login,
                child: Text('Войти'),
              ),
              if (error.isNotEmpty)
                Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    error,
                    style: TextStyle(color: Colors.red),
                  ),
                ),
            ],
          ),
        ));
  }
}
