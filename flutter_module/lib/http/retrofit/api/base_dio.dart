import 'package:dio/dio.dart';
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
    dio.options = BaseOptions(
        receiveTimeout: 5000, connectTimeout: 5000, headers: getHeaders());
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

  getHeaders() {
    return {
      "auth-token": "MTU5Mjg1MDg3NDcwNw11.26==",
      "boarding-pass": "882541C4BC8F480A83BD16978C487D9B"
    };
  }
}
