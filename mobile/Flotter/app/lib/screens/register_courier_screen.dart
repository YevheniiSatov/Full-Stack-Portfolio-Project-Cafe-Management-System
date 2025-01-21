import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';

class RegisterCourierScreen extends StatefulWidget {
  @override
  State<RegisterCourierScreen> createState() => _RegisterCourierScreenState();
}

class _RegisterCourierScreenState extends State<RegisterCourierScreen> {
  final TextEditingController nameController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  String error = '';
  String success = '';

  void _register() async {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    await authProvider.registerCourier(
        nameController.text, emailController.text, passwordController.text);

    if (authProvider.error.isEmpty) {
      setState(() {
        success = 'Регистрация успешна! Пожалуйста, войдите.';
        error = '';
      });
    } else {
      setState(() {
        error = authProvider.error;
        success = '';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Регистрация курьера'),
        ),
        body: Padding(
          padding: EdgeInsets.all(16.0),
          child: Column(
            children: [
              TextField(
                controller: nameController,
                decoration: InputDecoration(labelText: 'Имя'),
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
                onPressed: _register,
                child: Text('Зарегистрироваться'),
              ),
              if (error.isNotEmpty)
                Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    error,
                    style: TextStyle(color: Colors.red),
                  ),
                ),
              if (success.isNotEmpty)
                Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    success,
                    style: TextStyle(color: Colors.green),
                  ),
                ),
            ],
          ),
        ));
  }
}
