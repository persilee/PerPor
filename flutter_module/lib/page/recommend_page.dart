import 'package:flutter/material.dart';
import 'package:flutter_module/http/per_error.dart';
import 'package:flutter_module/view_model/recommend_view_model.dart';
import 'package:flutter_module/widget/empty_page.dart';
import 'package:flutter_module/widget/page_state.dart';

class RecommendPage extends StatefulWidget {
  @override
  _RecommendPageState createState() => _RecommendPageState();
}

class _RecommendPageState extends State<RecommendPage> {

  final model = RecommendViewModel();

  @override
  void initState() {
    model.getRecommend();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: StreamBuilder(
        stream: model.stream,
        builder: (buildContext, snapshot) {
          if (snapshot.hasError) {
            return EmptyPage(
              title: (snapshot.error as PerError).code.toString(),
              desc: (snapshot.error as PerError).message,
            );
          }
          var pageState = snapshot.data;
          if (!snapshot.hasData || pageState is BusyState ) {
            return Center(
              child: CircularProgressIndicator(
                valueColor: AlwaysStoppedAnimation<Color>(Color(0xFFDD2F24)),
              ),
            );
          }

          if (pageState is DataFetchState && !pageState.hasData) {
            return EmptyPage(
              title: "没有数据！",
            );
          }

          return Container(
            child: Text("推荐"),
          );
        },
      ),
    );
  }
}
