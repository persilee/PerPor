import 'package:flutter/material.dart';

import 'iconfont.dart';

class EmptyPage extends StatelessWidget {
  final IconData icon;
  final String title;
  final String desc;
  final String buttonText;
  final VoidCallback buttonAction;
  final VoidCallback helpAction;

  EmptyPage(
      {this.icon,
      this.title,
      this.desc,
      this.buttonText,
      this.buttonAction,
      this.helpAction});

  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;

    return Scaffold(
      body: Container(
        alignment: Alignment.center,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: size.width / 2.6,
              child:
                  Icon(
                    this.icon ?? IconFont.if_empty,
                    size: size.width / 3.6,
                    color: Colors.black54,
                  ),
            ),
            Padding(
              padding: EdgeInsets.only(
                top: 18,
                bottom: 12,
              ),
              child: Text(
                this.title ?? '小朋友,你是否有很多问号？',
                style: TextStyle(fontSize: 18, color: Colors.black),
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  this.desc ?? '虽然什么也没有,要不刷新看看',
                  style: TextStyle(fontSize: 18, color: Colors.grey),
                ),
                GestureDetector(
                  onTap: () => this.helpAction(),
                  child: Icon(
                    IconFont.if_detail,
                    size: 18,
                    color: Colors.grey,
                  ),
                ),
              ],
            ),
            Padding(
              padding: const EdgeInsets.all(12.0),
              child: MaterialButton(
                onPressed: () => this.buttonAction(),
                child: Text(
                  this.buttonText ?? '刷新',
                  style: TextStyle(fontSize: 16, color: Colors.white),
                ),
                color: Color(0xFFDD2F24),
                shape: RoundedRectangleBorder(
                    side: BorderSide.none,
                    borderRadius: BorderRadius.circular(6)),
              ),
            )
          ],
        ),
      ),
    );
  }
}
