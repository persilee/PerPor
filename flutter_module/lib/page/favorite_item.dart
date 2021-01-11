import 'package:flutter/material.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/view_model/favorite_view_model.dart';
import 'package:flutter_module/widget/per_refresh.dart';
import 'package:provider/provider.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

class FavoriteItem extends StatefulWidget {

  @override
  _FavoriteItemState createState() => _FavoriteItemState();
}

class _FavoriteItemState extends State<FavoriteItem> {
  RefreshController _refreshController = RefreshController();
  ScrollController _scrollController = ScrollController();


  void _onRefresh() async {
    var model = Provider.of<FavoriteViewModel>(context, listen: false);
    List<Goods> lists = await model.loadMore(isRefresh: true);
    if ( lists != null) {
      _refreshController.refreshCompleted();
      _refreshController.footerMode.value = LoadStatus.canLoading;
    } else {
      _refreshController.refreshFailed();
    }
  }

  void _onLoading() async {
    var model = Provider.of<FavoriteViewModel>(context, listen: false);
    if (await model.loadMore() != null) {
      _refreshController.loadComplete();
    } else {
      _refreshController.loadNoData();
    }
  }

  @override
  Widget build(BuildContext context) {

    List<Goods> goodsList = Provider.of<FavoriteViewModel>(context).list;

    return PerRefresh(
      controller: _refreshController,
      onLoading: _onLoading,
      onRefresh: _onRefresh,
      content: ListView.builder(
        controller: _scrollController,
        itemCount: goodsList?.length ?? 0,
        padding: EdgeInsets.only(bottom: 60),
        itemBuilder: (BuildContext context, int index) =>
            _item(goodsList[index]),
      ),
    );
  }

  _item(Goods goods) {
    return GestureDetector(
      onTap: () {
        PerFlutterBridge.getInstance().goToNative({"action": "goToDetail", "goodsId": goods.goodsId});
      },
      child: Card(
        child: IntrinsicHeight(
          child: Padding(
            padding: EdgeInsets.all(10),
            child: Row(
              children: <Widget>[_itemImage(goods), _itemText(goods)],
            ),
          ),
        ),
      ),
    );
  }

  _itemImage(Goods goods) {
    return Container(
      child: PhysicalModel(
        color: Colors.transparent,
        clipBehavior: Clip.antiAlias,
        borderRadius: BorderRadius.circular(2),
        child: Image.network(goods.sliderImage),
      ),
      height: 128,
      width: 128,
      margin: EdgeInsets.only(right: 10),
    );
  }

  _itemText(Goods goods) {
    //让侧容器填满剩余的横向空间
    return Expanded(
        child: Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text(
          goods.goodsName ?? "",
          maxLines: 2,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(fontSize: 14, color: Colors.black87),
        ),
        _infoText(goods),
      ],
    ));
  }

  _infoText(Goods goods) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Row(
          children: <Widget>[
            Text(
              "￥",
              style: TextStyle(fontSize: 10, color: Colors.redAccent),
            ),
            Padding(
                padding: EdgeInsets.only(right: 5),
                child: Text(
                  goods.groupPrice ?? "",
                  style: TextStyle(fontSize: 18, color: Colors.redAccent),
                )),
            Text(
              goods.completedNumText ?? "",
              style: TextStyle(fontSize: 12, color: Colors.grey),
            )
          ],
        ),
        GestureDetector(
          child: Container(
            padding: EdgeInsets.only(left: 20),
            child: Icon(
              Icons.more_horiz,
              size: 20,
              color: Colors.black26,
            ),
          ),
          onTap: () {
            print("click more");
          },
        )
      ],
    );
  }
}
