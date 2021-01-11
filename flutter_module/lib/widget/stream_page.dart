import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';
import 'package:flutter_module/http/per_error.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/view_model/favorite_view_model.dart';
import 'package:flutter_module/widget/base_view_model.dart';
import 'package:flutter_module/widget/page_state.dart';
import 'package:provider/provider.dart';

import 'empty_page.dart';

class StreamPage<T extends PageState> extends StatefulWidget {
  final BaseViewModel model;
  final Widget content;
  final Function() onRefresh;
  final Widget Function(BuildContext context, AsyncSnapshot<T> snapshot) builder;

  StreamPage({@required this.model, this.content, this.onRefresh, this.builder});

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
            print("PerState: ${snapshot.connectionState} in hasError");
            return EmptyPage(
              title: (snapshot.error as PerError).code?.toString(),
              desc: (snapshot.error as PerError).message,
              buttonAction: (){
                if (snapshot.error is NeedLogin) {
                  PerFlutterBridge.getInstance().goToNative({"action": "goToLogin"});
                } else {
                  widget.onRefresh();
                }
              },
              buttonText: snapshot.error is NeedLogin ? "登录" : "刷新",
            );
          }
          var pageState = snapshot.data;
          if (!snapshot.hasData || pageState is BusyState) {
            print("PerState: ${snapshot.connectionState} in busy");
            return Center(
              child: CircularProgressIndicator(
                valueColor: AlwaysStoppedAnimation<Color>(Color(0xFFDD2F24)),
              ),
            );
          }

          if (pageState is DataFetchState && !pageState.hasData) {
            print("PerState: ${snapshot.connectionState} in nonData");
            return EmptyPage(
              title: "没有数据！",
            );
          }

          return Padding(padding: EdgeInsets.only(bottom: 55), child: widget.builder(context, snapshot),);
        });
  }
}
