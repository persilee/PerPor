import 'dart:async';

import 'package:flutter_module/http/per_error.dart';
import 'package:flutter_module/http/retrofit/api/api_client.dart';
import 'package:flutter_module/http/retrofit/api/base_dio.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/widget/page_state.dart';

class RecommendViewModel {
  
  final StreamController<PageState> _controller = StreamController<PageState>();

  Stream<PageState> get stream => _controller.stream;

  List<Goods> _dataLists;
  
  Future getRecommend() async {
    _controller.add(BusyState());
    var goodsModel;
    try {
      goodsModel = await ApiClient().getRecommend("1", "10");
      _dataLists = goodsModel.data.list;
      _controller.add(DataFetchState<List<Goods>>(_dataLists));
      if (_dataLists.length <= 0) {
        _controller.add(DataFetchState(null));
      }
    } catch (e) {
      _controller.addError(BaseDio.getInstance().getDioError(e));
    }
  }
  
}