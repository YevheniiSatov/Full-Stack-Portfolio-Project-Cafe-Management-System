import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'providers/auth_provider.dart';
import 'providers/order_provider.dart';
import 'screens/home_screen.dart';
import 'screens/login_screen.dart';
import 'screens/register_screen.dart';
import 'screens/client_order_screen.dart';
import 'screens/user_orders_screen.dart';
import 'screens/cafe_orders_screen.dart';
import 'screens/courier_login_screen.dart';
import 'screens/register_courier_screen.dart';
import 'screens/courier_dashboard_screen.dart';

void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthProvider()),
        ChangeNotifierProvider(create: (_) => OrderProvider()),
      ],
      child: MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Cafe App',
      theme: ThemeData(
        primaryColor: Color(0xFFFF6F61),
        scaffoldBackgroundColor: Color(0xFFF4F4F9),
        textTheme: TextTheme(
          headlineLarge: TextStyle(
            fontSize: 32,
            fontWeight: FontWeight.bold,
            color: Color(0xFF333333),
          ),
          bodyLarge: TextStyle(
            fontSize: 16,
            color: Color(0xFF333333),
          ),
        ),
      ),
      initialRoute: '/',
      routes: {
        '/': (context) => HomeScreen(),
        '/login': (context) => LoginScreen(),
        '/register': (context) => RegisterScreen(),
        '/client-order': (context) => ClientOrderScreen(),
        '/user-orders': (context) => UserOrdersScreen(),
        '/cafe-orders': (context) => CafeOrdersScreen(),
        '/courier-login': (context) => CourierLoginScreen(),
        '/register-courier': (context) => RegisterCourierScreen(),
        '/courier-dashboard': (context) => CourierDashboardScreen(),
      },
    );
  }
}
