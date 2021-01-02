import 'package:dio/dio.dart';
import 'package:pretty_dio_logger/pretty_dio_logger.dart';

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

  getHeaders() {
    return {
      "auth-token": "MTU5Mjg1MDg3NDcwNw11.26==",
      "boarding-pass": "882541C4BC8F480A83BD16978C487D9B"
    };
  }
}