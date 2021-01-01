import 'package:flutter_module/http/request/base_request.dart';

class PerHttp {
  PerHttp._();

  static PerHttp _instance;

  static PerHttp getInstance() {
    if (_instance == null) {
      _instance = PerHttp._();
    }
    return _instance;
  }

  Future  fire(BaseRequest request) async {
    if(request.httpMethod() == HttpMethod.GET) {
      return _doGet(request);
    }
  }

  Future _doGet(BaseRequest request) {

  }
}