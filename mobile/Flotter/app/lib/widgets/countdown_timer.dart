import 'dart:async';
import 'package:flutter/material.dart';

class CountdownTimer extends StatefulWidget {
  final DateTime acceptanceTime;

  CountdownTimer({required this.acceptanceTime});

  @override
  _CountdownTimerState createState() => _CountdownTimerState();
}

class _CountdownTimerState extends State<CountdownTimer> {
  String timeLeft = '';

  @override
  void initState() {
    super.initState();
    calculateTimeLeft();
    Timer.periodic(Duration(seconds: 1), (timer) {
      calculateTimeLeft();
    });
  }

  void calculateTimeLeft() {
    final now = DateTime.now();
    final deadline = widget.acceptanceTime.add(Duration(minutes: 3));
    final difference = deadline.difference(now);

    if (difference.isNegative) {
      setState(() {
        timeLeft = 'Время заказа истекло';
      });
    } else {
      final minutes = difference.inMinutes;
      final seconds = difference.inSeconds % 60;
      setState(() {
        timeLeft = '$minutes:${seconds.toString().padLeft(2, '0')}';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Text(timeLeft);
  }
}
