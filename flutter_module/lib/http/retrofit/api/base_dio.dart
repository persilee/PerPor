import 'package:dio/dio.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';
import 'package:flutter_module/http/retrofit/api/header_interceptor.dart';
import 'package:pretty_dio_logger/pretty_dio_logger.dart';

import '../../per_error.dart';

class BaseDio {
  BaseDio._();

  static BaseDio _instance;

  static BaseDio getInstance() {
    if (_instance == null) {
      _instance = BaseDio._();
    }

    return _instance;
  }

  Dio getDio() {
    Dio dio = Dio();
    dio.options = BaseOptions(receiveTimeout: 5000, connectTimeout: 5000);
    dio.interceptors.add(HeaderInterceptor());
    dio.interceptors.add(PrettyDioLogger(
      requestHeader: true,
      requestBody: true,
      responseBody: true,
      responseHeader: false,
      compact: false,
    ));
    return dio;
  }

  PerError getDioError(Object obj) {
    switch (obj.runtimeType) {
      case DioError:
        if ((obj as DioError).type == DioErrorType.RESPONSE) {
          final response = (obj as DioError).response;
          if (response.statusCode == 401) {
            return NeedLogin();
          } else if (response.statusCode == 403) {
            return NeedAuth();
          } else {
            return OtherError(
                statusCode: response.statusCode,
                statusMessage: response.statusMessage);
          }
        }
    }

    return OtherError();
  }
}
