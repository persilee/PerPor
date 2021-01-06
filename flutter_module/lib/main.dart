import 'package:flutter/material.dart';
import 'package:flutter/physics.dart';
import 'package:flutter_module/page/favorite_page.dart';
import 'package:flutter_module/page/recommend_page.dart';
import 'package:flutter_module/view_model/favorite_view_model.dart';
import 'package:flutter_module/widget/page_state.dart';
import 'package:provider/provider.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

void main() => runApp(MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (_) => FavoriteViewModel(),
        ),
      ],
      child: MyApp(FavoritePage()),
    ));

@pragma('vm:entry-point')
void recommend() => runApp(MyApp(RecommendPage()));

class MyApp extends StatelessWidget {
  final Widget page;

  MyApp(this.page);

  @override
  Widget build(BuildContext context) {
    return RefreshConfiguration(
      headerTriggerDistance: 100.0,
      maxOverScrollExtent: 120,
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        home: Scaffold(
          body: page,
        ),
      ),
    );
  }
}
