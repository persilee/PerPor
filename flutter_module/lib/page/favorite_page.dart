import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';
import 'package:flutter_module/http/per_error.dart';
import 'package:flutter_module/http/retrofit/api/api_client.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/page/favorite_item.dart';
import 'package:flutter_module/view_model/favorite_view_model.dart';
import 'package:flutter_module/widget/empty_page.dart';
import 'package:flutter_module/widget/iconfont.dart';
import 'package:flutter_module/widget/page_state.dart';
import 'package:flutter_module/widget/stream_page.dart';
import 'package:provider/provider.dart';

class FavoritePage extends StatefulWidget {
  @override
  _FavoritePageState createState() => _FavoritePageState();
}

class _FavoritePageState extends State<FavoritePage> {
  final _model = FavoriteViewModel();
  var header;

  @override
  void initState() {
    _model.getData();
    super.initState();
    PerFlutterBridge.getInstance().register("onRefresh", (MethodCall call) {
      _model.getData();
    });
  }

  @override
  void dispose() {
    super.dispose();
    PerFlutterBridge.getInstance().unRegister("onRefresh");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: StreamPage<PageState>(
        model: _model,
        builder: (context, snapshot) {
          print("PerState: ${snapshot.connectionState} in streamPage");
          PageState pageState = snapshot.data;
          if (pageState is DataFetchState && pageState.hasData) {
            Provider.of<FavoriteViewModel>(context).updateData(pageState.data);
          }
          return FavoriteItem();
        },
        onRefresh: () =>
            Provider.of<FavoriteViewModel>(context, listen: false).getData(),
      ),
    );
  }
}
