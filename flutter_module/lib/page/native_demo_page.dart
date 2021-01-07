import 'package:flutter/material.dart';
import 'package:flutter_module/native/per_image_view.dart';

class NativeDemoPage extends StatefulWidget {
  @override
  _NativeDemoPageState createState() => _NativeDemoPageState();
}

class _NativeDemoPageState extends State<NativeDemoPage> {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text("native view for image", style: TextStyle(fontSize: 20),),
        SizedBox(
          height: 300,
          child: PerImageView(
            url: "https://cdn.lishaoy.net/flutterFlare/flutter-flare-cover.png",
          ),
        )
      ],
    );
  }
}
