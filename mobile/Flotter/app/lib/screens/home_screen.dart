import 'package:flutter/material.dart';

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    void navigateTo(String route) {
      Navigator.pushNamed(context, route);
    }

    return Scaffold(
      body: Stack(
        children: [
          Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  'Welcome to the Cafe App',
                  style: Theme.of(context).textTheme.headlineLarge,
                ),
                SizedBox(height: 20),
                ElevatedButton(
                  onPressed: () => navigateTo('/login'),
                  child: Text('Login'),
                ),
                ElevatedButton(
                  onPressed: () => navigateTo('/register'),
                  child: Text('Register'),
                ),
              ],
            ),
          ),
          Positioned(
            bottom: 20,
            right: 20,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(
                  'Вы уже курьер или хотите им стать?',
                  style: TextStyle(color: Colors.grey),
                ),
                SizedBox(height: 10),
                OutlinedButton(
                  onPressed: () => navigateTo('/courier-login'),
                  child: Text('Login'),
                  style: OutlinedButton.styleFrom(
                    foregroundColor: Colors.blue,
                    side: BorderSide(color: Colors.blue),
                  ),
                ),
                OutlinedButton(
                  onPressed: () => navigateTo('/register-courier'),
                  child: Text('Register'),
                  style: OutlinedButton.styleFrom(
                    foregroundColor: Colors.blue,
                    side: BorderSide(color: Colors.blue),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
