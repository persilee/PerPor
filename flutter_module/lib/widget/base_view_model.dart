import 'dart:async';

import 'package:flutter_module/widget/page_state.dart';

abstract class BaseViewModel<T> {

  final StreamController<PageState> controller;

  final Stream<PageState> stream;

  BaseViewModel(this.controller, this.stream);

  Future<List<T>> getData();
}