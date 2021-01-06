import 'dart:async';
import 'package:flutter_module/http/retrofit/api/api_client.dart';
import 'package:flutter_module/http/retrofit/api/base_dio.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/widget/base_view_model.dart';
import 'package:flutter_module/widget/page_state.dart';

class RecommendViewModel implements BaseViewModel<Goods> {
  List<Goods> dataLists;

  @override
  StreamController<PageState> controller = StreamController<PageState>();

  @override
  Stream<PageState> get stream => controller.stream;

  @override
  Future<List<Goods>> getData() async {
    controller.add(BusyState());
    var goodsModel;
    try {
      goodsModel = await ApiClient().getRecommend("1", "10");
      dataLists = goodsModel.data.list;
      controller.add(DataFetchState<List<Goods>>(dataLists));
      if (dataLists.length <= 0) {
        controller.add(DataFetchState(null));
      }
    } catch (e) {
      controller.addError(BaseDio.getInstance().getDioError(e));
    }

    return dataLists;
  }
}