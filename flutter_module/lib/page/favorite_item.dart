import 'package:flutter/material.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/view_model/favorite_view_model.dart';
import 'package:flutter_module/widget/per_refresh.dart';
import 'package:provider/provider.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

class FavoriteItem extends StatefulWidget {
  final List<Goods> dataList;
  final pageState;

  FavoriteItem({this.dataList, this.pageState});

  @override
  _FavoriteItemState createState() => _FavoriteItemState();
}

class _FavoriteItemState extends State<FavoriteItem> {
  List<String> items = ["1", "2", "3", "4", "5", "6", "7", "8"];

  RefreshController _refreshController = RefreshController();

  void _onRefresh() async {
    await Future.delayed(Duration(milliseconds: 1000));
    _refreshController.refreshCompleted();
  }

  void _onLoading() async {
    await Future.delayed(Duration(milliseconds: 1000));
    items.add((items.length + 1).toString());
    if (mounted) setState(() {});
    _refreshController.loadComplete();
  }

  @override
  Widget build(BuildContext context) {

    List<Goods> goodsList = Provider.of<FavoriteViewModel>(context).list;

    return PerRefresh(
      controller: _refreshController,
      onLoading: _onLoading,
      onRefresh: _onRefresh,
      content: Center(
        child: Text(goodsList.first.goodsName),
      ),
    );
  }
}
