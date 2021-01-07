import 'dart:async';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/http/retrofit/api/api_client.dart';
import 'package:flutter_module/http/retrofit/api/base_dio.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/widget/base_view_model.dart';
import 'package:flutter_module/widget/page_state.dart';

class RecommendViewModel with ChangeNotifier implements BaseViewModel<Goods> {
  List<Goods> dataLists;
  int pageIndex = 1;

  @override
  StreamController<PageState> controller = StreamController<PageState>();

  @override
  Stream<PageState> get stream => controller.stream;

  @override
  Future<List<Goods>> getData() async {
    controller.add(BusyState());
    var goodsModel;
    Dio dio;
    try {
      dio = await BaseDio.getInstance().getDio();
      goodsModel = await ApiClient(dio: dio).getRecommend("1", "10");
      dataLists = goodsModel.data.list;
      if(dataLists.length > 0) {
        controller.add(DataFetchState<List<Goods>>(dataLists));
      }
      if (dataLists.length <= 0) {
        controller.add(DataFetchState(null));
      }
    } catch (e) {
      print(e);
      controller.addError(BaseDio.getInstance().getDioError(e));
    }
    notifyListeners();

    return dataLists;
  }

  Future<List<Goods>> loadMore({bool isRefresh = false}) async {
    pageIndex ++;
    GoodsModel goodsModel;
    Dio dio;
    try {
      dio = await BaseDio.getInstance().getDio();
      if(isRefresh) {
        pageIndex = 1;
        goodsModel = await ApiClient(dio: dio).getRecommend(pageIndex.toString(), "10");
        if (goodsModel.data.list.length > 0) {
          dataLists.clear();
          dataLists.addAll(goodsModel.data.list);
        }
      } else {
        goodsModel = await ApiClient(dio: dio).getRecommend(pageIndex.toString(), "10");
        if (goodsModel.data.list.length > 0) {
          dataLists.addAll(goodsModel.data.list);
        } else {
          return null;
        }
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
}