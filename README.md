# Neleme 仿饿了么点餐页面

![原版页面](https://github.com/wudifamo/TestTinker/blob/master/gif/eleme.gif)![neleme页面](https://github.com/wudifamo/TestTinker/blob/master/gif/neleme.gif)

已实现的功能      
* 顶部嵌套滑动逻辑 
* 分类和商品级联滑动定位
* 添加购物车动画
* 购物车弹窗

嵌套滑动使用Coordinator+Behavior实现,不能直接做library使用,仅提供实现方法供大家参考
购物车动画是二阶贝塞尔,想要一模一样的可以自己调quadTo里面的路径

buildToolsVersion使用的最新的26,如果不想更新改低的话有些地方findviewById编译报红,加上转换类型就好了,26的特性就是简化掉了这个显式转换

我这边试了4.4 5.0 7.0都是可以的,细节优化什么的以后会补上的

计划实现的功能
* 点击商品后的上滑进入详情下滑退出等逻辑
* 商户详情页面
* 评价页面(就是一个recyclerview,本来想做原来的加载动画了,可是刚刚饿了么更新动画换了,现在很尴尬...)
