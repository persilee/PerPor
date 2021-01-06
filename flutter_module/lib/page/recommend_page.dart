import 'package:flutter/material.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/page/recommend_item.dart';
import 'package:flutter_module/view_model/recommend_view_model.dart';
import 'package:flutter_module/widget/page_state.dart';
import 'package:flutter_module/widget/stream_page.dart';
import 'package:provider/provider.dart';

class RecommendPage extends StatefulWidget {
  @override
  _RecommendPageState createState() => _RecommendPageState();
}

class _RecommendPageState extends State<RecommendPage> {

  final _model = RecommendViewModel();

  @override
  void initState() {
    _model.getData();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {

    List<Goods> goodsList;

    return Scaffold(
      body: StreamPage(
        model: _model,
        content: RecommendItem(),
        onReady: (pageState) {
          if (pageState is DataFetchState && pageState.hasData) {
            goodsList = pageState.data as List<Goods>;
            Provider.of<RecommendViewModel>(context).updateData(goodsList);
          }
        },
      ),
    );
  }
}
