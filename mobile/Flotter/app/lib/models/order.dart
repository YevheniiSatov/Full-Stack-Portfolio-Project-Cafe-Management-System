class Order {
  final int id;
  final String status;
  final String clientName;
  final String clientPhone;
  final String assignedCafe;
  final DateTime acceptanceTime;
  final String address;
  final List<OrderItem> items;

  Order({
    required this.id,
    required this.status,
    required this.clientName,
    required this.clientPhone,
    required this.assignedCafe,
    required this.acceptanceTime,
    required this.address,
    required this.items,
  });

  factory Order.fromJson(Map<String, dynamic> json) {
    return Order(
      id: json['id'],
      status: json['status'],
      clientName: json['clientName'] ?? '',
      clientPhone: json['clientPhone'] ?? '',
      assignedCafe: json['assignedCafe'] ?? '',
      acceptanceTime: DateTime.parse(json['acceptanceTime'] ?? DateTime.now().toIso8601String()),
      address: json['address'] ?? '',
      items: (json['orderItems'] as List)
          .map((item) => OrderItem.fromJson(item))
          .toList(),
    );
  }
}

class OrderItem {
  final String name;
  final int quantity;

  OrderItem({
    required this.name,
    required this.quantity,
  });

  factory OrderItem.fromJson(Map<String, dynamic> json) {
    return OrderItem(
      name: json['menuItem']['name'],
      quantity: json['quantity'],
    );
  }
}
