import 'package:flutter/material.dart';
import 'package:flutter_module/http/per_error.dart';
import 'package:flutter_module/view_model/recommend_view_model.dart';
import 'package:flutter_module/widget/empty_page.dart';
import 'package:flutter_module/widget/page_state.dart';
import 'package:flutter_module/widget/stream_page.dart';

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
    return Scaffold(
      body: StreamPage(
        model: _model,
        content: Center(
          child: Text("recommend"),
        ),
      ),
    );
  }
}
