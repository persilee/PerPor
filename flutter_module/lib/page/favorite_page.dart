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

  @override
  void initState() {
    _model.getData();
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {

    List<Goods> goodsList;

    return Scaffold(
      body: StreamPage<PageState>(
        model: _model,
        content: FavoriteItem(),
        onReady: (pageState) {
          if (pageState is DataFetchState && pageState.hasData) {
            goodsList = pageState.data as List<Goods>;
            Provider.of<FavoriteViewModel>(context).updateData(goodsList);
          }
        },
      ),
    );
  }
}
