import 'dart:async';

import 'package:flutter_module/widget/page_state.dart';

abstract class BaseViewModel {

  final StreamController<PageState> controller;

  final Stream<PageState> stream;

  BaseViewModel(this.controller, this.stream);

  getData();
}