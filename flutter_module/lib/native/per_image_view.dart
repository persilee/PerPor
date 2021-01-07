import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PerImageView extends StatefulWidget {
  final String url;

  PerImageView({this.url});

  @override
  _PerImageViewState createState() => _PerImageViewState();
}

class _PerImageViewState extends State<PerImageView> {

  static const StandardMessageCodec _decode = StandardMessageCodec();

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      viewType: "PerImageView",
      creationParams: {"url": widget.url},
      creationParamsCodec: _decode,
    );
  }
}
