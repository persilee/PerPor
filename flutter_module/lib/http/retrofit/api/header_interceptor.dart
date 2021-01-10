import 'package:dio/dio.dart';
import 'package:flutter_module/core/per_flutter_bridge.dart';

class HeaderInterceptor extends Interceptor {

  @override
  Future onRequest(RequestOptions options) async {

    Map<String, String> headers = await PerFlutterBridge.getInstance().getHeaderParams();
    options.headers.addAll(headers);

    return options;
  }

}