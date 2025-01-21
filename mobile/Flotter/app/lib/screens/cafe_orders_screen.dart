import 'dart:async';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/order_provider.dart';
import '../providers/auth_provider.dart';
import '../widgets/order_list.dart';

class CafeOrdersScreen extends StatefulWidget {
  @override
  _CafeOrdersScreenState createState() => _CafeOrdersScreenState();
}

class _CafeOrdersScreenState extends State<CafeOrdersScreen> {
  String authToken = '';
  String userEmail = '';
  Timer? _timer;

  @override
  void initState() {
    super.initState();
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    authToken = authProvider.user?.token ?? '';
    userEmail = authProvider.user?.email ?? '';

    final orderProvider = Provider.of<OrderProvider>(context, listen: false);
    orderProvider.fetchCafeOrders(userEmail, authToken);

    _timer = Timer.periodic(Duration(seconds: 15), (_) {
      orderProvider.fetchCafeOrders(userEmail, authToken);
    });
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  void _acceptOrder(int orderId) {
    final orderProvider = Provider.of<OrderProvider>(context, listen: false);
    orderProvider.acceptOrder(orderId, authToken);
  }

  @override
  Widget build(BuildContext context) {
    final orderProvider = Provider.of<OrderProvider>(context);

    if (orderProvider.isLoading) {
      return Scaffold(
        appBar: AppBar(
          title: Text('Заказы для кафе'),
        ),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (orderProvider.error.isNotEmpty) {
      return Scaffold(
        appBar: AppBar(
          title: Text('Заказы для кафе'),
        ),
        body: Center(child: Text(orderProvider.error)),
      );
    }

    final pendingOrders = orderProvider.orders
        .where((order) => order.status == 'Pending')
        .toList();
    final acceptedOrders = orderProvider.orders
        .where((order) => order.status == 'Accepted')
        .toList();

    return Scaffold(
      appBar: AppBar(
        title: Text('Заказы для кафе'),
      ),
      body: Column(
        children: [
          ElevatedButton(
            onPressed: () =>
                orderProvider.fetchCafeOrders(userEmail, authToken),
            child: Text('Обновить список заказов'),
          ),
          Expanded(
            child: Row(
              children: [
                Expanded(
                  child: OrderList(
                    title: 'Incoming (Pending)',
                    orders: pendingOrders,
                    onAction: _acceptOrder,
                    actionLabel: 'Принять',
                  ),
                ),
                Expanded(
                  child: OrderList(
                    title: 'Outgoing (Accepted)',
                    orders: acceptedOrders,
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
