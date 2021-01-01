import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';

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
          FlatButton(onPressed: (){
            PerFlutterBridge.getInstance().goToNative({
              "action": "goToDetail",
              "goodsId": "1580373975523"
            });
          }, child: Text('toDetail'))
        ],
      ),
    );
  }
}
