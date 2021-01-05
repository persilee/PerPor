import 'package:dio/dio.dart';
import 'package:flutter_module/http/retrofit/api/base_dio.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:retrofit/http.dart';
import 'package:retrofit/retrofit.dart';

part 'api_client.g.dart';

@RestApi(baseUrl: "https://api.devio.org/as")
abstract class ApiClient {
  factory ApiClient({String baseUrl}) {
    Dio dio = BaseDio.getInstance().getDio();
    return _ApiClient(dio, baseUrl: baseUrl);
  }

  @GET("/goods/recommend")
  Future<GoodsModel> getRecommend(
      @Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize);


}
