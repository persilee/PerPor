import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_module/http/retrofit/api/api_client.dart';
import 'package:flutter_module/http/retrofit/api/base_dio.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/widget/base_view_model.dart';
import 'package:flutter_module/widget/page_state.dart';

class FavoriteViewModel with ChangeNotifier implements BaseViewModel<Goods> {
  List<Goods> dataLists;

  @override
  // ignore: close_sinks
  StreamController<PageState> controller = StreamController<PageState>();

  @override
  Future<List<Goods>> getData() async {
    controller.add(BusyState());
    GoodsModel goodsModel;
    try {
      goodsModel = await ApiClient().getFavorite("1", "10");
      dataLists = goodsModel.data.list;
      if (dataLists.length > 0) {
        controller.add(DataFetchState<List<Goods>>(dataLists));
      }
      if (dataLists.length <= 0) {
        controller.add(DataFetchState(null));
      }
    } catch (e) {
      controller.addError(BaseDio.getInstance().getDioError(e));
    }
    notifyListeners();

    return dataLists;
  }

  updateData(dynamic data) {
    dataLists = data as List<Goods>;
  }

  get total => dataLists.length;

  List<Goods> get list => dataLists;

  @override
  Stream<PageState> get stream => controller.stream;
}
