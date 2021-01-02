import 'package:dio/dio.dart';
import 'package:flutter_module/http/retrofit/api/base_dio.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:pretty_dio_logger/pretty_dio_logger.dart';
import 'package:retrofit/http.dart';
import 'package:retrofit/retrofit.dart';

part 'recommend_api.g.dart';

@RestApi(baseUrl: "https://api.devio.org/as")
abstract class RecommendApi {
  factory RecommendApi({String baseUrl}) {
    Dio dio = BaseDio.getInstance().getDio();
    return _RecommendApi(dio, baseUrl: baseUrl);
  }

  @GET("/goods/recommend")
  Future<GoodsModel> getRecommend(
      @Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize);
}
