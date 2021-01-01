
enum HttpMethod {
  GET,
  POST
}

abstract class BaseRequest {
  String url() {
    return "api.devio.org";
  }

  HttpMethod httpMethod();

  String path();

  Map<String, String> params = Map();

  Map<String, String> add(String k, Object v) {
    params[k] = v.toString();
    return params;
  }
}