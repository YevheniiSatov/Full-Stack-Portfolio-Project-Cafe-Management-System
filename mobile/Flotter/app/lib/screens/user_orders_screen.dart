import 'dart:async';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../providers/order_provider.dart';

class UserOrdersScreen extends StatefulWidget {
  @override
  State<UserOrdersScreen> createState() => _UserOrdersScreenState();
}

class _UserOrdersScreenState extends State<UserOrdersScreen> {
  @override
  void initState() {
    super.initState();
    final authToken =
        Provider.of<AuthProvider>(context, listen: false).user!.token;
    Provider.of<OrderProvider>(context, listen: false)
        .fetchUserOrders(authToken);

    // Обновление заказов каждые 15 секунд
    Future.delayed(Duration.zero, () {
      Timer.periodic(Duration(seconds: 15), (_) {
        Provider.of<OrderProvider>(context, listen: false)
            .fetchUserOrders(authToken);
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    final orderProvider = Provider.of<OrderProvider>(context);

    if (orderProvider.isLoading) {
      return Scaffold(
        appBar: AppBar(title: Text('История заказов')),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (orderProvider.error.isNotEmpty) {
      return Scaffold(
        appBar: AppBar(title: Text('История заказов')),
        body: Center(child: Text(orderProvider.error)),
      );
    }

    final orders = orderProvider.orders;

    return Scaffold(
      appBar: AppBar(title: Text('История заказов')),
      body: orders.isNotEmpty
          ? ListView.builder(
        itemCount: orders.length,
        itemBuilder: (context, index) {
          final order = orders[index];
          final totalPrice = order.items
              .map((item) => item.quantity * item.name.length) // Предположим, цена зависит от длины имени
              .reduce((value, element) => value + element);

          return Card(
            child: ListTile(
              title: Text('Заказ №${order.id}'),
              subtitle: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Статус: ${order.status}'),
                  Text('Товары:'),
                  ...order.items.map((item) =>
                      Text('${item.name} - Количество: ${item.quantity}')),
                  Text('Общая стоимость: $totalPrice ₽'),
                ],
              ),
            ),
          );
        },
      )
          : Center(child: Text('Заказы отсутствуют')),
    );
  }
  }

