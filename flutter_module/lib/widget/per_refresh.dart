import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

class PerRefresh extends StatefulWidget {
  final RefreshController controller;
  final VoidCallback onRefresh;
  final VoidCallback onLoading;
  final Widget content;

  PerRefresh({this.controller, this.onRefresh, this.onLoading, this.content});

  @override
  _PerRefreshState createState() => _PerRefreshState();
}

class _PerRefreshState extends State<PerRefresh> {
  @override
  Widget build(BuildContext context) {
    return SmartRefresher(
      controller: widget.controller,
      enablePullDown: true,
      enablePullUp: true,
      header: CustomHeader(
        builder: (BuildContext context, RefreshStatus mode) {
          Widget body;
          if (mode == RefreshStatus.canRefresh) {
            body = textIndicator("松开刷新");
          } else if (mode == RefreshStatus.refreshing) {
            body = textIndicator("加载中...");
          } else if (mode == RefreshStatus.idle) {
            body = textIndicator("下拉刷新");
          }
          return Container(
            height: 55,
            child: Center(
              child: body,
            ),
          );
        },
      ),
      footer: CustomFooter(
        builder: (BuildContext context, LoadStatus mode) {
          Widget body;
          if (mode == LoadStatus.idle) {
            body = Text("上拉加载");
          } else if (mode == LoadStatus.loading) {
            body = Container(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CupertinoActivityIndicator(),
                  Padding(padding: EdgeInsets.only(left: 10)),
                  Text("加载中...")
                ],
              ),
            );
          } else if (mode == LoadStatus.failed) {
            body = Text("加载失败！点击重试！");
          } else if (mode == LoadStatus.canLoading) {
            body = Text("松手,加载更多!");
          } else {
            body = Text("没有更多数据了!");
          }
          return Container(
            height: 55.0,
            child: Center(child: body),
          );
        },
      ),
      onRefresh: widget.onRefresh,
      onLoading: widget.onLoading,
      child: widget.content,
    );
  }

  Widget textIndicator(String statusStr) {
    return Container(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          CupertinoActivityIndicator(),
          Padding(padding: EdgeInsets.only(top: 6)),
          Text(statusStr)
        ],
      ),
    );
  }
}
