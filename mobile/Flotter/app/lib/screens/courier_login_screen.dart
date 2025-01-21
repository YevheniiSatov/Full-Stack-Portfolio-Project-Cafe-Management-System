import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';

class CourierLoginScreen extends StatefulWidget {
  @override
  State<CourierLoginScreen> createState() => _CourierLoginScreenState();
}

class _CourierLoginScreenState extends State<CourierLoginScreen> {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  String error = '';

  void _login() async {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    await authProvider.loginCourier(
        emailController.text, passwordController.text);

    if (authProvider.isAuthenticated) {
      Navigator.pushReplacementNamed(context, '/courier-dashboard');
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
          title: Text('Вход для курьера'),
        ),
        body: Padding(
          padding: EdgeInsets.all(16.0),
          child: Column(
            children: [
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
