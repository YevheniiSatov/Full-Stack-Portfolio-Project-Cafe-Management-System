import 'package:flutter/material.dart';
import '../models/order.dart';

class OrderList extends StatelessWidget {
  final String title;
  final List<Order> orders;
  final Function(int)? onAction;
  final String? actionLabel;

  const OrderList({
    Key? key,
    required this.title,
    required this.orders,
    this.onAction,
    this.actionLabel,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(
          '$title (${orders.length})',
          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
        Expanded(
          child: orders.isNotEmpty
              ? ListView.builder(
            itemCount: orders.length,
            itemBuilder: (context, index) {
              final order = orders[index];
              return Card(
                child: ListTile(
                  title: Text('ID Заказа: ${order.id}'),
                  subtitle: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('Статус: ${order.status}'),
                      Text('Кафе: ${order.assignedCafe}'),
                      Text('Товары:'),
                      ...order.items.map((item) => Text(
                          '${item.name} - Количество: ${item.quantity}')),
                    ],
                  ),
                  trailing: onAction != null
                      ? ElevatedButton(
                    onPressed: () => onAction!(order.id),
                    child: Text(actionLabel ?? 'Действие'),
                  )
                      : null,
                ),
              );
            },
          )
              : Center(child: Text('Нет заказов')),
        ),
      ],
    );
  }
}
