import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';
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
    PerFlutterBridge.getInstance().register("onRefresh", (MethodCall call) {
      _model.getData();
    });
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
    PerFlutterBridge.getInstance().unRegister("onRefresh");
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      body: StreamPage(
        model: _model,
        content: RecommendItem(),
        builder: (context, snapshot) {
          PageState pageState = snapshot.data;
          if (pageState is DataFetchState && pageState.hasData) {
            Provider.of<RecommendViewModel>(context).updateData(pageState.data);
          }
          return RecommendItem();
        },
      ),
    );
  }
}
