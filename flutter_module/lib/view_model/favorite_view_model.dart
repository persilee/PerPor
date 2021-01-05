import 'dart:async';

import 'package:flutter_module/http/retrofit/api/api_client.dart';
import 'package:flutter_module/http/retrofit/api/base_dio.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/widget/base_view_model.dart';
import 'package:flutter_module/widget/page_state.dart';

class FavoriteViewModel implements BaseViewModel {
  List<Goods> dataLists;

  @override
  // ignore: close_sinks
  StreamController<PageState> controller = StreamController<PageState>();

  @override
  Future getData() async {
    controller.add(BusyState());
    var goodsModel;
    try {
      goodsModel = await ApiClient().getFavorite("1", "10");
      dataLists = goodsModel.data.list;
      if (dataLists.length > 0) {
        return controller.add(DataFetchState<List<Goods>>(dataLists));
      }
      if (dataLists.length <= 0) {
        return controller.add(DataFetchState(null));
      }
    } catch (e) {
      return controller.addError(BaseDio.getInstance().getDioError(e));
    }
  }

  @override
  Stream<PageState> get stream => controller.stream;
}
