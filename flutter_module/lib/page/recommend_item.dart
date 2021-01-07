import 'package:flutter/material.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/view_model/favorite_view_model.dart';
import 'package:flutter_module/view_model/recommend_view_model.dart';
import 'package:flutter_module/widget/per_refresh.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:provider/provider.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import 'package:transparent_image/transparent_image.dart';

class RecommendItem extends StatefulWidget {

  @override
  _RecommendItemState createState() => _RecommendItemState();
}

class _RecommendItemState extends State<RecommendItem> {
  RefreshController _refreshController = RefreshController();
  ScrollController _scrollController = ScrollController();


  void _onRefresh() async {
    var model = Provider.of<RecommendViewModel>(context, listen: false);
    List<Goods> lists = await model.loadMore(isRefresh: true);
    if ( lists != null) {
      _refreshController.refreshCompleted();
      _refreshController.footerMode.value = LoadStatus.canLoading;
    } else {
      _refreshController.refreshFailed();
    }
  }

  void _onLoading() async {
    var model = Provider.of<RecommendViewModel>(context, listen: false);
    if (await model.loadMore() != null) {
      _refreshController.loadComplete();
    } else {
      _refreshController.loadNoData();
    }
  }

  @override
  Widget build(BuildContext context) {

    List<Goods> goodsList = Provider.of<RecommendViewModel>(context).list;

    print(goodsList);

    return PerRefresh(
      controller: _refreshController,
      onLoading: _onLoading,
      onRefresh: _onRefresh,
      content: StaggeredGridView.countBuilder(
        controller: _scrollController,
        crossAxisCount: 4,
        itemCount: goodsList?.length ?? 0,
        padding: EdgeInsets.only(bottom: 60),
        staggeredTileBuilder: (int index) => StaggeredTile.fit(2),
        itemBuilder: (BuildContext context, int index) =>
            _item(context, goodsList[index]),
      ),
    );
  }

  _item(BuildContext context, Goods goods) {
    return GestureDetector(
      onTap: () {
        PerFlutterBridge.getInstance().goToNative({"action": "goToDetail", "goodsId": goods.goodsId});
      },
      child: Card(
        child: PhysicalModel(
          color: Colors.transparent,
          clipBehavior: Clip.antiAlias,
          borderRadius: BorderRadius.circular(5),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              _itemImage(context, goods),
              Container(
                padding: EdgeInsets.all(4),
                child: Text(
                  goods.goodsName,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(fontSize: 14, color: Colors.black87),
                ),
              ),
              _infoText(goods)
            ],
          ),
        ),
      ),
    );
  }

  _itemImage(BuildContext context, Goods goods) {
    final size = MediaQuery.of(context).size;
    return Container(
      //设置最小初始高度，防止动态图片高度时的抖动
      constraints: BoxConstraints(minHeight: size.width / 2),
      child: FadeInImage.memoryNetwork(
        placeholder: kTransparentImage,
        image: goods.sliderImage,
        fit: BoxFit.cover,
      ),
    );
  }

  _infoText(Goods goods) {
    return Container(
      padding: EdgeInsets.fromLTRB(6, 0, 6, 10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Padding(
            padding: EdgeInsets.only(bottom: 5),
            child: Text(
              goods.tags,
              style: TextStyle(fontSize: 11, color: Colors.deepOrangeAccent),
            ),
          ),
          Row(
            children: <Widget>[
              Text(
                "￥",
                style: TextStyle(fontSize: 10, color: Colors.redAccent),
              ),
              Padding(
                  padding: EdgeInsets.only(right: 5),
                  child: Text(
                    goods.groupPrice,
                    style: TextStyle(fontSize: 18, color: Colors.redAccent),
                  )),
              Text(
                goods.completedNumText,
                style: TextStyle(fontSize: 12, color: Colors.grey),
              )
            ],
          ),
        ],
      ),
    );
  }
}
