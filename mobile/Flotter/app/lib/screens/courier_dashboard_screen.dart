import 'dart:async';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../providers/order_provider.dart';
import '../widgets/countdown_timer.dart';

class CourierDashboardScreen extends StatefulWidget {
  @override
  State<CourierDashboardScreen> createState() => _CourierDashboardScreenState();
}

class _CourierDashboardScreenState extends State<CourierDashboardScreen> {
  @override
  void initState() {
    super.initState();
    final authToken =
        Provider.of<AuthProvider>(context, listen: false).user!.token;
    Provider.of<OrderProvider>(context, listen: false)
        .fetchCourierOrders(authToken);

    // Обновление заказов каждые 10 секунд
    Future.delayed(Duration.zero, () {
      Timer.periodic(Duration(seconds: 10), (_) {
        Provider.of<OrderProvider>(context, listen: false)
            .fetchCourierOrders(authToken);
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    final orderProvider = Provider.of<OrderProvider>(context);

    return Scaffold(
      appBar: AppBar(title: Text('Доступные заказы для курьера')),
      body: Column(
        children: [
          ElevatedButton(
            onPressed: () {
              final authToken =
                  Provider.of<AuthProvider>(context, listen: false)
                      .user!
                      .token;
              orderProvider.fetchCourierOrders(authToken);
            },
            child: Text('Обновить вручную'),
          ),
          Expanded(
            child: orderProvider.isLoading
                ? Center(child: CircularProgressIndicator())
                : ListView.builder(
              itemCount: orderProvider.orders.length,
              itemBuilder: (context, index) {
                final order = orderProvider.orders[index];
                return Card(
                  child: ListTile(
                    title: Text('Заказ №${order.id}'),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Имя заказчика: ${order.assignedCafe}'),
                        Text('Адрес: Не указан'), // Добавьте поле адреса
                        Text('Оставшееся время:'),
                        CountdownTimer(
                          acceptanceTime: DateTime.now(), // Замените на order.acceptanceTime
                        ),
                      ],
                    ),
                    trailing: ElevatedButton(
                      onPressed: () {},
                      child: Text('Принять заказ'),
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
