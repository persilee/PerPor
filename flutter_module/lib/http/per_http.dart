import 'package:flutter_module/http/request/base_request.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class PerHttp {
  PerHttp._();

  static PerHttp _instance;

  static PerHttp getInstance() {
    if (_instance == null) {
      _instance = PerHttp._();
    }
    return _instance;
  }

  Future fire(BaseRequest request) async {
    if (request.httpMethod() == HttpMethod.GET) {
      return _doGet(request);
    }
  }

  Future _doGet(BaseRequest request) async {
    var uri;
    if (request.params.length != 0) {
      uri = Uri.https(request.url(), request.path(), request.params);
    } else {
      uri = Uri.https(request.url(), request.path());
    }
    http.Response response;
    var header = getHeaderParams();
    response = await http.get(uri.toString(), headers: header);
    Utf8Decoder utf8decoder = Utf8Decoder();
    var result;
    if (response.headers["content-type"].contains("/json")) {
      result = json.decode(utf8decoder.convert(response.bodyBytes));
    } else {
      result = utf8.decode(response.bodyBytes);
    }
    if (response.statusCode == 200) {
      if (result["code"] == 0) {
        return result["data"];
      } else {
        throw Exception(result["msg"]);
      }
    }
  }

  getHeaderParams() {
    return {"auth-token", "MTU5Mjg1MDg3NDcwNw11.26=="};
  }
}
