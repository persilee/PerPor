import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';
import 'package:flutter_module/http/per_http.dart';
import 'package:flutter_module/http/request/recommend_request.dart';
import 'package:flutter_module/http/retrofit/api/recommend_api.dart';

class FavoritePage extends StatefulWidget {
  @override
  _FavoritePageState createState() => _FavoritePageState();
}

class _FavoritePageState extends State<FavoritePage> {
  @override
  void initState() {
    super.initState();
    PerFlutterBridge.getInstance().register("onRefresh", (MethodCall call) {
      print("🍓🍈🍒🍑");
      return Future.value('flutter');
    });
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    PerFlutterBridge.getInstance().unRegister("onRefresh");
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Text("收藏"),
          FlatButton(
              onPressed: () async {
                // var request = RecommendRequest();
                // request.add("pageIndex", "1");
                // request.add("pageSize", "10");
                // try {
                //   var result = await PerHttp.getInstance().fire(request);
                //   print(result);
                // } catch (e) {
                //   print(e);
                // }
                RecommendApi()
                    .getRecommend("1", "10")
                    .then((value) => print(value.data.list[0].toJson()))
                    .catchError((e) => print(e.toString()));

                // print(data.data.list[0].toJson());
              },
              child: Text('toDetail'))
        ],
      ),
    );
  }
}
