class User {
  final String email;
  String role;
  String token;
  final String name;

  User({
    required this.email,
    required this.name,
    this.role = 'USER',
    this.token = '',
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      email: json['email'],
      name: json['name'],
      role: json['role'] ?? 'USER',
      token: json['token'] ?? '',
    );
  }
}
