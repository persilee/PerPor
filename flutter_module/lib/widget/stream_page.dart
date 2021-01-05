import 'package:flutter/material.dart';
import 'package:flutter_module/http/per_error.dart';
import 'package:flutter_module/widget/base_view_model.dart';
import 'package:flutter_module/widget/page_state.dart';

import 'empty_page.dart';

class StreamPage extends StatefulWidget {

  final BaseViewModel model;
  final Widget content;


  StreamPage({@required this.model, this.content});

  @override
  _StreamPageState createState() => _StreamPageState();
}

class _StreamPageState extends State<StreamPage> {

  @override
  Widget build(BuildContext context) {
    return StreamBuilder(
        stream: widget.model.stream,
        builder: (buildContext, snapshot) {
          if (snapshot.hasError) {
            return EmptyPage(
              title: (snapshot.error as PerError).code?.toString(),
              desc: (snapshot.error as PerError).message,
            );
          }
          var pageState = snapshot.data;
          if (!snapshot.hasData || pageState is BusyState) {
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

          return widget.content;
        });
  }
}
