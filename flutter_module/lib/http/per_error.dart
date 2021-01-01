class PerError {
  final int code;
  final String message;

  PerError(this.code, this.message);
}

class NeedLogin implements PerError {
  @override
  int get code => 401;

  @override
  String get message => "请先登录";
}

class NeedAuth implements PerError {
  @override
  int get code => 403;

  @override
  String get message => "非法访问";
}