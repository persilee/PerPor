import 'package:flutter/services.dart';

class PerFlutterBridge {
  
  static PerFlutterBridge _instance = PerFlutterBridge._();
  MethodChannel _channel = const MethodChannel("PerFlutterBridge");
  var _listeners = {};
  PerFlutterBridge._() {
    _channel.setMethodCallHandler((MethodCall call){
      String method = call.method;
      if(_listeners[method] != null) {
          return _listeners[method](call);
      }
      return null;
    });
  }

  static PerFlutterBridge getInstance() {
    return _instance;
  }

  register(String method, Function(MethodCall) callback) {
    _listeners[method] = callback;
  }

  unRegister(String method) {
    _listeners.remove(method);
  }

  goToNative(Map params) {
    _channel.invokeMethod("goToNative", params);
  }

  MethodChannel bridge() {
    return _channel;
  }
}