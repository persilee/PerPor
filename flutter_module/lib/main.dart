import 'package:flutter/material.dart';
import 'package:flutter_module/page/favorite_page.dart';
import 'package:flutter_module/page/recommend_page.dart';

void main() => runApp(MyApp(FavoritePage()));

@pragma('vm:entry-point')
void recommend() => runApp(MyApp(RecommendPage()));

class MyApp extends StatelessWidget {
  final Widget page;

  MyApp(this.page);
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: page,
      ),
    );
  }
}



