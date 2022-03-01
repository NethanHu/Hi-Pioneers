# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.20)
# Database: jfinal_admin
# Generation Time: 2021-03-09 14:41:53 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickName` varchar(50) NOT NULL,
  `userName` varchar(150) NOT NULL,
  `password` varchar(150) NOT NULL,
  `salt` varchar(150) NOT NULL,
  `state` int(11) NOT NULL,
  `avatar` varchar(128) NOT NULL DEFAULT '' COMMENT '头像',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;

INSERT INTO `account` (`id`, `nickName`, `userName`, `password`, `salt`, `state`, `avatar`, `created`, `updated`)
VALUES
	(1,'美女','jfinal','7a5fdcb5c4bc6629126ccbe0434397269b6181fbc391c5cf031111c369c6eee3','aswtsUIumsmQUt7OvSgVgPGxg69Fh2vD',1,'1.png','2020-12-03 09:58:11','2020-12-03 09:58:11'),
	(2,'帅哥','james','b914c9b0dbcd208461fb9ed0d8677f92e83937866ac9af4943b1a463460ff674','YuKSF16HBcLGjjJTiqZalQp8polbfSgL',1,'2.png','2021-01-31 12:03:28','2021-01-31 12:03:28');

/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table account_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `account_role`;

CREATE TABLE `account_role` (
  `accountId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  PRIMARY KEY (`accountId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `account_role` WRITE;
/*!40000 ALTER TABLE `account_role` DISABLE KEYS */;

INSERT INTO `account_role` (`accountId`, `roleId`)
VALUES
	(1,1),
	(2,4);

/*!40000 ALTER TABLE `account_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table article
# ------------------------------------------------------------

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountId` int(11) NOT NULL,
  `title` varchar(150) NOT NULL,
  `content` text NOT NULL,
  `pic` varchar(128) NOT NULL DEFAULT '' COMMENT '文章配图',
  `state` int(11) NOT NULL COMMENT '0为草稿，1为发布',
  `seoKeywords` int(11) DEFAULT NULL,
  `seoDescription` int(11) DEFAULT NULL,
  `viewCount` int(11) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;

INSERT INTO `article` (`id`, `accountId`, `title`, `content`, `pic`, `state`, `seoKeywords`, `seoDescription`, `viewCount`, `created`, `updated`)
VALUES
	(1,1,'jfinal blog','<h2>一、概述</h2><p>&nbsp; &nbsp;再过三个月 jfinal 将走过 10 年的迭代时期，经过如此长时间的打磨 jfinal 现今已十分完善、稳定。</p><p>&nbsp; &nbsp;jfinal 起步时就定下的开发效率高、学习成本低的核心目标，一直不忘初心，坚持至今。</p><p>&nbsp; &nbsp;jfinal 在开发效率方向越来越逼近极限，如果还要进一步提升开发效率，唯一的道路就是入场前端。</p><p>&nbsp; &nbsp;jfinal 社区经常有同学反馈，用上 jfinal 以后，90% 以上的时间都在折腾前端，强烈希望 jfinal 官方能出手前端，推出一个 jfinal 风格的前端框架。</p><p>&nbsp; &nbsp;虽然我个人对前端没有兴趣，但为了进一步提升广大 jfinal 用户的开发效率，决定这次在前端先小试牛刀。</p><p>&nbsp; &nbsp;本次为大家带来是&nbsp;jfinal 极简风格的前端交互工具箱：jfinal-kit.js。jfinal-kit.js 主要特色如下：</p><ol class=\" list-paddingleft-2\"><li><p>学习成本：不引入&nbsp;vue react angular 这类前端技术栈，核心用法 10 分钟掌握，极大降低学习成本</p></li><li><p>开发效率：尽可能避免编写 js 代码就能实现前端功能，极大提升开发效率</p></li><li><p>用户体验：交互全程 ajax，交互过程 UI 尽可能及时反馈</p></li><li><p>前后分离：只在必要之处使用前后分离，其它地方使用模板引擎，结合前后分离与模板引擎优势<br></p></li></ol><p>&nbsp; &nbsp; 仍是熟悉的味道：学习成本低、开发效率高</p><h2>二、五种交互抽象</h2><p>&nbsp;&nbsp; jfinal-kit.js 将前端交互由轻到重抽象为：msg、switch、confirm、open、fill 五种模式(未来还将增加tab抽象)，以下分别介绍。</p><h3>1、msg 交互</h3><p>&nbsp; &nbsp; msg 用于最轻量级的交互，当用户点击页面中某个组件（如按钮）时立即向后端发起 ajax 请求，然后将后端响应输出到页面。要用该功能，第一步通过 jfinal-kit.js 中的 kit.bindMsg(...) 绑定需要 msg 交互的页面元素：</p><pre>kit.bindMsg(\'#content-box\',&nbsp;\'button[msg],a[msg]\',&nbsp;\'正在处理,&nbsp;请稍候&nbsp;.....\');</pre><p>&nbsp; &nbsp;以上一行代码就可以为带有 msg 属性的标签&nbsp;button 与标签 a 添加 msg 交互功能。</p><p>&nbsp; &nbsp; 注意，bindMsg 方法中的前两个参数在底层实际上就是用的 jquery 的事件绑定方法 on，尽可能用上开发者已有的技术只累，降低学习成本。第三个参数是在交互过程中的提示信息，用于提升用户体验，该参数可以省略。</p><p>&nbsp; &nbsp;第二步在 html 中使用第一步中绑定所带来的功能：</p><pre>&lt;button&nbsp;msg&nbsp;url=\"/func/clearCache\"&gt;\n&nbsp;&nbsp;&nbsp;清除缓存\n&lt;/button&gt;</pre><p>&nbsp; &nbsp;上面的 button 标签中的 url 指向了后端的 action。由于第一步中第二个参数的选择器同时也绑定了 a 标签，所以 button 改为 a 也可以。</p><p>&nbsp; &nbsp; 第三步，在后端添加第二步 url 指向的 action 即可：</p><pre>public&nbsp;void&nbsp;clearCache()&nbsp;{\n&nbsp;&nbsp;&nbsp;cacheService.clearCache();\n&nbsp;&nbsp;&nbsp;renderJson(Ret.ok(\"msg\",&nbsp;\"缓存清除完成\"));\n}</pre><p>&nbsp; &nbsp; 整个过程的代码量极少，前端交互功能的实现也像后端一样快了，开发效率得到极大提升。</p><h3>2、switch 交互</h3><p>&nbsp; &nbsp;switch 交互是指类似于手机设置中心开关控件功能，点击 switch 可在两种状态间来回切换，使用方法：</p><pre>kit.bindSwitch(\'#content-box\',&nbsp;\'div.custom-switch&nbsp;input[url]\');</pre><p>&nbsp; &nbsp;与 msg 交互类似，同样也是一行代码。参数用法也一样：将 switch 交互功能绑定到带有 url 的 input 控件上（div.custom-switch是jquery选择器的一部分）。功能绑定后，就可以在 html 中使用了：</p><pre>&lt;div&nbsp;class=\"custom-control&nbsp;custom-switch\"&gt;\n&nbsp;&nbsp;&nbsp;&lt;input&nbsp;#(x.state&nbsp;==&nbsp;1&nbsp;?&nbsp;\'checked\':\'\')&nbsp;url=\'/blog/publish?id=#(x.id)\'&nbsp;type=\"checkbox\"&gt;\n&nbsp;&nbsp;&nbsp;&lt;label&nbsp;class=\"custom-control-label\"&nbsp;for=\"id-#(x.id)\"&gt;&lt;/label&gt;\n&lt;/div&gt;</pre><p>&nbsp; &nbsp;上面代码中的 div、lable 仅仅为 bootstrap 4 的 switch 组件所要求的内容，不必关注，重点关注 input 标签，其 url 指向了后端 action，在后端添加即可：</p><pre>public&nbsp;void&nbsp;publish()&nbsp;{\n&nbsp;&nbsp;&nbsp;Ret&nbsp;ret&nbsp;=&nbsp;srv.publish(getInt(\"id\"),&nbsp;getBoolean(\"checked\"));\n&nbsp;&nbsp;&nbsp;renderJson(ret);\n}</pre><p>&nbsp; &nbsp;switch 交互与 msg 在本质上是完全相同的。</p><h3>3、confirm 交互</h3><p>&nbsp; &nbsp; confirm 交互与 msg 交互基本一样，只不过在与后端交互之前会弹出对话框进行确认，使用方法：</p><pre>kit.bindConfirm(\'#content-box\',&nbsp;\'a[confirm],button[confirm]\');</pre><p>&nbsp; &nbsp;与 msg、switch 本质上一样，将 confirm 交互绑定到具有 confirm 属性的 a 标签与 button 标签上。在 html 中使用：</p><pre>&lt;button&nbsp;confirm=\"确定重启项目&nbsp;？\"&nbsp;url=\"/admin/func/restart\"&gt;\n&nbsp;&nbsp;&nbsp;重启项目\n&lt;/button&gt;</pre><p>&nbsp; &nbsp;最后是添加后端 action：</p><pre class=\"brush:java;toolbar:false\">public&nbsp;void&nbsp;restart()&nbsp;{\n&nbsp;&nbsp;&nbsp;renderJson(srv.restart());\n}</pre><p>&nbsp; &nbsp;以上 msg、switch、confirm 三种交互方式，使用模式完全一样：<span style=\"font-family: 微软雅黑; font-size: 18px;\">绑定、添加 html（url指向后端action）、添加 action。</span></p><h3><span style=\"font-family: 微软雅黑; font-size: 18px;\">4、open 交互<span style=\"font-size: 18px; font-family: arial, helvetica, sans-serif;\"></span></span></h3><p>&nbsp; &nbsp;&nbsp;<span style=\"font-family: 微软雅黑; font-size: 18px;\">open 交互方式与前面三种交互方式基本相同，不同之处在于前三种交互方式参与的元素就在当前页面，而 open 交互方式的参与元素是一个独立的 html 文件，第一步仍然是绑定：</span></p><pre class=\"brush:java;toolbar:false\">kit.bindOpen(\'#content-box\',&nbsp;\'a[open],button[open]\',&nbsp;\'正在加载,&nbsp;请稍候&nbsp;.....\');</pre><p><span style=\"font-family: 微软雅黑; font-size: 18px;\"></span>&nbsp; &nbsp; 以上代码的含义与 msg 类似，将 open 交互功能绑定到带有 open 属性的 a 标签与 button 标签之上。</p><p>&nbsp; &nbsp; 第二步仍然是在 html 中使用：</p><pre class=\"brush:html;toolbar:false;\">&lt;button&nbsp;open&nbsp;url=\"/account/add\"&gt;\n&nbsp;&nbsp;&nbsp;创建\n&lt;/button&gt;</pre><p>&nbsp; &nbsp; 第三步仍然是创建 url 指向的 action：</p><pre class=\"brush:java;toolbar:false\">public&nbsp;void&nbsp;add()&nbsp;{\n&nbsp;&nbsp;&nbsp;render(\"add.html\");\n}</pre><p>&nbsp; &nbsp; 第三步与 msg、switch、confirm 交互不同之处在于，这里是返回一个独立的页面，而非返回 json 数据。注意，如果页面并没有动态内容，无需模板引擎渲染的话，无需创建该 action，而是让 url 直接指向它就可以了：</p><pre class=\"brush:html;toolbar:false\">&lt;button&nbsp;open&nbsp;url=\"/这里是一个静态页面文件.html\"&gt;\n&nbsp;&nbsp;&nbsp;创建\n&lt;/button&gt;</pre><p>&nbsp; &nbsp; 第四步是创建页面 \"add.html\" 单独用于交互，页面的主要内容如下：</p><pre class=\"brush:html;toolbar:false\">&lt;!--&nbsp;弹出层主体&nbsp;--&gt;\n&lt;div&nbsp;class=\"open-box\"&gt;\n&nbsp;&nbsp;&lt;form&nbsp;id=\"open-form\"&nbsp;action=\"/account/save\"&gt;	\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;div&nbsp;class=\"row\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;label&nbsp;class=\"col-2&nbsp;col-form-label\"&gt;昵称&lt;/label&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;input&nbsp;name=\"nickName\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;/div&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;div&nbsp;class=\"row\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;label&nbsp;class=\"col-2&nbsp;col-form-label\"&gt;账号&lt;/label&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;input&nbsp;name=\"userName\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;/div&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;div&nbsp;class=\"row\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;label&nbsp;class=\"col-2&nbsp;col-form-label\"&gt;密码&lt;/label&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;input&nbsp;name=\"password\"&nbsp;type=\"password\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;/div&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;div&nbsp;class=\"row\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;button&nbsp;onclick=\"submitAccount();\"&gt;提交&lt;/button&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;/div&gt;\n&nbsp;&nbsp;&nbsp;&lt;/form&gt;\n&lt;/div&gt;\n\n&lt;!--&nbsp;弹出层样式&nbsp;--&gt;\n&lt;style&gt;\n&nbsp;&nbsp;.open-box&nbsp;{padding:&nbsp;20px&nbsp;30px&nbsp;0&nbsp;35px;}\n&lt;/style&gt;\n\n&lt;!--&nbsp;弹出层&nbsp;js&nbsp;脚本&nbsp;--&gt;\n&lt;script&gt;\n&nbsp;&nbsp;function&nbsp;submitAccount()&nbsp;{\n&nbsp;&nbsp;&nbsp;&nbsp;$form&nbsp;=&nbsp;$(\'#open-form\');\n&nbsp;&nbsp;&nbsp;&nbsp;kit.post($form.attr(\'action\'),&nbsp;$form.serialize(),&nbsp;function(ret)&nbsp;{\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;kit.msg(ret);\n&nbsp;&nbsp;&nbsp;&nbsp;});\n&nbsp;&nbsp;}\n&lt;/script&gt;</pre><p>&nbsp; &nbsp; 以上 add.html 页面是用于交互的 html 内容，该内容将会显示在一个弹出的对话框之中。该文件的内容分为 html、css、js 三个部分，从而可以实现功能的模块化。</p><p>&nbsp; &nbsp; 第五步，针对 add.html 中 form 表单的 action=\"/account/save\" 创建相应的 action：</p><pre class=\"brush:java;toolbar:false\">public&nbsp;void&nbsp;save()&nbsp;{\n&nbsp;&nbsp;Ret&nbsp;ret&nbsp;=&nbsp;srv.save(getBean(Account.class));\n&nbsp;&nbsp;renderJson(ret);\n}</pre><p>&nbsp; &nbsp;action 代码十分简单，与 msg 交互模式代码风格一样。</p><p>&nbsp; &nbsp; open 交互需要一个独立的页面作为载体，而 msg、switch、confirm 没有这个载体。</p><h3>5、fill 交互</h3><p>&nbsp; &nbsp;fill 交互与前面四种交互很不一样，它是向当前页面的指定容器填充 html 内容，从而在当前页面中进行交互。</p><p>&nbsp; &nbsp;第一步仍然是绑定：</p><pre class=\"brush:java;toolbar:false\">kit.bindFill(\'#content-box\',&nbsp;\'a[fill],button[fill],ul.pagination&nbsp;a[href]\',&nbsp;\'#content-box\');</pre><p>&nbsp; &nbsp;前两个参数与前面四种交互模式完全一样，最后一个参数 \'#content-box\' 表示从后端被加载的 html 内容 fill 到的容器。<br></p><p>&nbsp; &nbsp;第二步与前面四种交互模式的用法完全一样，不再详述。</p><p>&nbsp; &nbsp;第三步与 open 模式的<span style=\"font-family: 微软雅黑; font-size: 18px;\">第四步创建页面 \"add.html\" 单独用于交互完全一样，也不再详述。</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp;fill 与 open 在本质上是一样的，只不过前者是将交互用的 html 文件内容直接 fill 到当前页面，后者是用弹出层来承载 html 文件内容，仅此而已。</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp;所以，学会了 open，相当于就学会了 fill。</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp;最后，fill 交互是实现前后分离模式的基础，后续章节将深入介绍。</span></p><h2><span style=\"font-family: 微软雅黑; font-size: 18px;\"></span></h2><h2 style=\"white-space: normal;\"><span style=\"font-family: 微软雅黑; font-size: 18px;\"><span style=\"font-size: 24px;\">三、前后端半分离方案</span></span></h2><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp;最近几年前后分离技术很热，前后分离有很多优点，但对于全栈开发者和中小企业也有一定的缺点。</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp;首先，前后分离不利于 SEO，不利于搜索引擎收录。搜索引擎仍是巨大的流量入口，如果辛辛苦苦创建的内容没有被搜索引擎收录，将是巨大的损失。</span></p><p>&nbsp; &nbsp;其次，前后分离通常要引入其整个技术栈，会带来一定的学习成本。jfinal 社区多数开发者主要面向后端开发，如果再引入前后分离技术栈，很多同学并没有多少时间与动力。有兴趣原因也有专注度原因，前端也是一片汪洋大海。</p><p>&nbsp; &nbsp;再次，前后分离通常要设置前端与后端两种工作岗位，对于小企业有成本压力。维护前后分离项目的成本也有所增加。</p><p>&nbsp; &nbsp;最后，前后分离减轻了后端工作负担，加重了前端工作负担，但对于 jfinal 社区的全栈开发者来说，相当程度上是工作负担的转移，总体上并没有消除多少工作量。对于后端包打天下，未设置前端职位的中小企业带来的是成本提升与效率降低。</p><p>&nbsp; &nbsp;jfinal-kit.js 希望能得到前后分离的优点，并同时能消除它的缺点。<br></p><p>&nbsp; &nbsp;jfinal-kit.js 的采用前后端 \"半分离\" 方案：只在必要的地方前后分离拿走前后分离的好处，其它地方使用模板引擎扔掉前后分离的坏处。并且不必引入&nbsp;<span style=\"font-family: 微软雅黑; font-size: 18px;\">vue、react 等前端技术栈，消除学习成本。</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp;jfinal-kit.js 的前后半分离具体是下面这样的，需要前后分离的 \"xxx.html\" 页面内容如下：</span></p><pre class=\"brush:java;toolbar:false\">&lt;!DOCTYPE&nbsp;html&gt;\n&lt;html&nbsp;lang=\"zh-CN\"&gt;\n&nbsp;&nbsp;&lt;head&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;内容省略...\n&nbsp;&nbsp;&lt;/head&gt;\n\n&nbsp;&nbsp;&lt;body&nbsp;class=\"home-template\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;内容省略...\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;!--&nbsp;下面&nbsp;div&nbsp;内的内容通过&nbsp;ajax&nbsp;获取，实现前后分离&nbsp;--&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;div&nbsp;id=\'article\'&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/div&gt;\n&nbsp;&nbsp;&nbsp;\n&nbsp;&nbsp;&lt;/body&gt;\n&nbsp;&nbsp;\n&nbsp;&nbsp;&lt;script&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$(function()&nbsp;{\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;kit.fill(\'/article/123\',&nbsp;null,&nbsp;\'#article\');\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;});&nbsp;&nbsp;&nbsp;\n&nbsp;&nbsp;&lt;/script&gt;\n&lt;/html&gt;</pre><p><span style=\"font-family: 微软雅黑; font-size: 18px;\"></span>&nbsp; &nbsp;以上 \"xxx.html\" 文件只有静态内容，动态内容通过 kit.fill(...) 异步加载，其第一个参数指向的 action 后端代码如下：</p><pre class=\"brush:java;toolbar:false\">public&nbsp;void&nbsp;article()&nbsp;{\n&nbsp;&nbsp;&nbsp;set(\"article\",&nbsp;srv.getById(getPara()));\n&nbsp;&nbsp;&nbsp;render(\"_article.html\");\n}</pre><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp;以上代码中的 \"_article.html\" 是与传统前的分离方案不同的地方，传统前后分离返回的是 json 数据，而这里返回的是 html 片段，其代码结构如下：</span></p><pre class=\"brush:html;toolbar:false\">&lt;div&nbsp;class=\"article-box\"&gt;\n&nbsp;&nbsp;&lt;div&nbsp;class=\"title\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;span&gt;#(article.title)&lt;/span&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;span&gt;#date(article.createTime)&lt;/span&gt;\n&nbsp;&nbsp;&lt;/div&gt;\n&nbsp;&nbsp;\n&nbsp;&nbsp;&lt;div&nbsp;class=\"content\"&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;#(article.content)\n&nbsp;&nbsp;&lt;/div&gt;\n&lt;/div&gt;</pre><p>&nbsp; &nbsp; 上面的 html 片段内容以模板方式展现，可读性、可维护性比传统前后分离要好。并且 html 片将由模板引擎渲染，客户端没有计算压力。</p><p>&nbsp; &nbsp; 以上仅仅示范了静态页面的一处动态加载方式，也可以使用任意多处动态加载，并且动态部分的粒度可以极细。例如假定静态部分如下：</p><pre class=\"brush:html;toolbar:false\">其它地方与前面的&nbsp;xxx.html&nbsp;一样，省去....\n\n&lt;table&nbsp;class=\"table&nbsp;table-hover\"&gt;			\n&nbsp;&nbsp;&lt;thead&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;tr&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;th&gt;ID&lt;/th&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;th&gt;昵称&lt;/th&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;th&gt;登录名&lt;/th&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;th&gt;创建&lt;/th&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;/tr&gt;\n&nbsp;&nbsp;&lt;/thead&gt;			\n&nbsp;&nbsp;&lt;tbody&nbsp;id=\'account-table\'&gt;\n&nbsp;&nbsp;&lt;/tbody&gt;\n&lt;/table&gt;\n\n&lt;script&gt;\n&nbsp;&nbsp;$(function()&nbsp;{\n&nbsp;&nbsp;&nbsp;&nbsp;kit.fill(\'/account/list\',&nbsp;null,&nbsp;\'#account-table\');\n&nbsp;&nbsp;});&nbsp;&nbsp;&nbsp;\n&lt;/script&gt;\n\n其它地方与前面的&nbsp;xxx.html&nbsp;一样，省去....</pre><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp;然后创建一个 action 响应上面代码中的 \"/account/list\"：</span></p><pre class=\"brush:java;toolbar:false\">public&nbsp;void&nbsp;list()&nbsp;{\n&nbsp;&nbsp;set(\"accountList\",&nbsp;srv.getAccountList());\n&nbsp;&nbsp;render(\"_account_table.html\");\n}</pre><p>&nbsp; &nbsp;以上代码中的 _account_table.html 如下：<br></p><pre class=\"brush:html;toolbar:false;\">#for&nbsp;(x&nbsp;:&nbsp;accountList)\n&nbsp;&nbsp;&lt;tr&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;td&gt;#(x.id)&lt;/td&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;td&gt;#(x.nickName)&lt;/td&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;td&gt;#(x.userName)&lt;/td&gt;\n&nbsp;&nbsp;&nbsp;&nbsp;&lt;td&gt;#date(x.created)&lt;/td&gt;\n&nbsp;&nbsp;&lt;/tr&gt;\n#end</pre><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp; 以上 _account.html 以模板形式展示，用 enjoy 进行渲染，使用简单，可读性高。</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp; 从本质上来说传统前后分离与 jfinal-kit.js 前后分离几乎一样，都是先向客户端响应静态 html + css + js，然后通过 ajax 向后端获取数据并渲染出动态内容，区别就在于前者是获取 json 并在客户端进行渲染，而后者是直接获取后端渲染好的 html 片进行简单的填充，仅此而已。</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp; 即便在底层技术实现上是如此的相似，但 jfinal-kit.js 无需引入复杂的技术栈，极大降低了学习成本。</span></p><h2><span style=\"font-family: 微软雅黑; font-size: 18px;\"><span style=\"font-family: 微软雅黑; font-size: 24px;\">四、jfinal blog 实践</span></span></h2><p>&nbsp; &nbsp;&nbsp;<span style=\"font-family: 微软雅黑; font-size: 18px;\">jfinal-kit.js 以 jfinal blog 为载体，演示了 jfinal-kit.js 中的功能用法，实现了一些常见功能：账户管理、文章管理、图片管理、功能管理、登录等功能。</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp; jfinal blog 后台管理 UI 面向实际项目精心设计，可以作为项目起步的蓝本。以下是部分界面截图：</span></p><p><span style=\"font-family: 微软雅黑; font-size: 18px;\">&nbsp; &nbsp; 补充截图</span></p><p>&nbsp; &nbsp; 实践证明，开发效率极大提升，学习成本极低，几乎不用写 js 代码就轻松实现前后交互。<span style=\"font-family: 微软雅黑; font-size: 18px;\">学习成本、开发效率两个方向符合预期目标，符合 jfinal 极简设计思想。</span></p><h2>五、咖啡授权</h2><p>&nbsp; &nbsp; app &amp; coffee 频道所有 application 采用咖啡授权模式，意在请作者喝一杯咖啡即可获得授权。</p>','',1,NULL,NULL,0,'2021-01-23 11:17:15','2021-01-23 09:17:15'),
	(2,1,'app & coffee 是什么？','<p>&nbsp; app &amp; coffee 是 jfinal 社区推出的新频道，目的是建立 jfinal 生态，以及进一步提升开发效率。</p>\r\n\r\n<p>&nbsp; 先说建立生态。jfinal 开源这 9 年，过于专注于技术，生态建设一直被忽视，这是 jfinal 最大的战略失误。jfinal 已经错过了建立生态的黄金时期，常规方式会很难也很慢，所以只能在创新方面想办法。</p>\r\n\r\n<p>&nbsp; app &amp; coffee 的核心在于建立一个正向激励的循环系统，让大家有动力参与生态的建设。简单来说就是俱乐部会员可以在 app &amp; coffee 频道发布符合规范的 application，通过咖啡授权协议获取一定的回报。被授权方可以用一杯咖啡的代价获得需要的 application。</p>\r\n\r\n<p>&nbsp; 再说提升开发效率。app &amp; coffee 频道发布的 application 是要通过严格规范才能上架的，例如对代码量、项目结构、项目类型都是有要求的。简单来说就是要细致入微地针对需求方来打造 application，需求方拿到授权以后，能大大提升开发效率。</p>\r\n\r\n<p>&nbsp; 最后，由于去年小孩出生，app &amp; coffee 的实施已被严重耽误，对于生态建设的时机造成了重大损失，app &amp; coffee 这个模式是否能成，还需要时间的检验，希望得到你的支持</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>&nbsp;</p>\r\n','',1,NULL,NULL,0,'2021-01-23 11:17:15','2021-01-23 10:17:15'),
	(3,1,'JFinal 4.9.03 发布，添加路由扫描功能','<p>JFinal 4.9.03 主要新增了路由扫描功能，开发更快更方便。</p>\r\n\r\n<p>以往未添加路由扫描功能主要有如下原因：</p>\r\n\r\n<p>一是未找到支持 routes 级别拦截器以及 baseViewPath 配置的设计方案。</p>\r\n\r\n<p>二是未找到支持拆分路由的方案。</p>\r\n\r\n<p>三是性能损失降低开发体验，热加载启动速度慢。</p>\r\n\r\n<p>四是有一定的安全隐患。</p>\r\n\r\n<p>本次 jfinal 4.9.03 所使用的方案解决了上述所有问题，找到了最优解。</p>\r\n\r\n<p>新功能在周末开发完成，已经推送至 maven 中心库，现在就可以使用了。</p>\r\n\r\n<p>jfinal 官网已经将 jfinal-club、jfinal-blog、weixin-pay、jfinal-demo 等等下载资源全部改成了路由扫描用法，欢迎你来社区网站下载使用。</p>\r\n\r\n<p>路由扫描功能使用极其简单，首先是在 Controller 之上添加 @Path 注解配置 controllerPath：</p>\r\n\r\n<pre>\r\n<code>@Path(\"/club\")\r\npublic class ClubController extends Controller {\r\n    ......\r\n}</code></pre>\r\n\r\n<p>然后在 configRoute 中开启扫描：</p>\r\n\r\n<pre>\r\n<code>public void configRoute(Routes me) {\r\n   me.addInterceptor(...);\r\n   me.setBaseViewPath(...);\r\n   // 开启路由扫描\r\n   me.scan(\"com.club.\");\r\n}</code></pre>\r\n\r\n<p>如上代码所示，routes 级别拦截器以及 baseViewPath 配置功能依然被支持，路由拆分功能见 jfinal 官方文档。</p>\r\n\r\n<p>最后，借此新版本发布与双十一来临之际，介绍一下 jfinal 俱乐部。</p>\r\n\r\n<p>jfinal 俱乐部成立于 2017 年，目的是尝试提供增值服务获取一定资金用于 jfinal 可持续发展。</p>\r\n\r\n<p>目前俱乐部会员接近 2000 人，俱乐部专享 QQ 群人数已超过 1700 人。</p>\r\n\r\n<p>俱乐部除了提供主打资源以外，还会不定期提供设计、分享、源码等视频资源下载，部分资源下载列表详见：https://jfinal.com/my/club 近期发布了一批同学们关心的技术视频，例如《enjoy设计-算法-源代码.mp4》、《jfinal-route-scan.mp4》等等。</p>\r\n\r\n<p>想学习如何开发一门语言或如何手写一个模板引擎的同学可以关注一下上述视频。</p>\r\n\r\n<p>enjoy 视频介绍了词法、语法分析中独创的 DLRD、DKFF 算法。</p>\r\n\r\n<p>俱乐部下一个重磅级专享福利项目 jfinal-admin 正在快速开发之中，很快将上线。</p>\r\n\r\n<p>该项目是一个通用的前后端开发框架，目的是实现前端后端同时极速开发。</p>\r\n\r\n<p>该项目提供了现成的内容管理、权限管理、账户管理、文件管理、图片管理等等通用功能。</p>\r\n\r\n<p>在此基础之上提供一套常用的UI 组件，用于快速搞定各类个性化 UI 开发需求。</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>&nbsp;</p>\r\n','',1,NULL,NULL,0,'2021-01-23 11:17:15','2021-01-23 11:17:15'),
	(4,1,'jfinal admin 1.0 发布','<p>jfinal 一直都需要一个官方极简的 jfinal&nbsp;admin 前端框架。</p>\r\n\r\n<p>jfinal admin 的意义就如同 JDK 之于 java 语言，如同 rail 框架之于 ruby 语言。</p>\r\n\r\n<p>可惜我认识到这个问题的时间太晚，也由于我个人对前端毫无兴趣，更没有美术功底，jfinal admin 这事就一直没被重视。</p>\r\n\r\n<p>但 jfinal 在极简与极速的道路上挺进了将近 10 年，已趋近极致。要想进一步大大加速开发效率，只能出手前端。</p>\r\n\r\n<p>jfinal admin 是与其它所有 admin 型开源项目很不一样的项目。</p>\r\n\r\n<p>首先，它基于 jfinal 多年来一直在极简设计风格。原因无它，仍然是尽可能降低你的学习成本，尽可能提升你的开发效率。jfinal admin 的极简体现在两个层面。</p>\r\n\r\n<p>第一层面是交互。jfinal admin 抽象出 msg、switch、confirm、open、fill、tab 六种交互模式，并且每种模式都提供了最简单的 API，其中 msg、switch、confirm、fill、tab 这五种交互模式可以做到零 js 代码，极大减少你的开发工作量，从而极大提升开发效率。剩下的 open 只需写极少的 js 代码，后续版本考虑将 open 交互的 js 代码也消除掉。</p>\r\n\r\n<p>第二个层面是 UI 的组织。jfinal admin 重新审视了所有 UI 要素，思考它们存在的必要性，尽可能消除了不必要的 UI 要素，从而展现出一个干净利落的 UI 界面，提升用户使用体验的同时也减少了你开发的工作量。</p>\r\n\r\n<p>其次，jfinal admin 采用 &ldquo;前后端半分离&rdquo; 技术方案，可以无需引入前端技术栈，极大降低学习成本。</p>\r\n\r\n<p>jfinal admin 的 &ldquo;前后端半分离&rdquo; 技术方案是相对于传统的 &ldquo;前后端分离&rdquo; 方案来说的。以下将以 &ldquo;全分离&rdquo;、&ldquo;半分离&rdquo; 为名介绍一下它们之间的相同、不同点以及优缺点。</p>\r\n\r\n<p>&ldquo;全分离&rdquo; 与 &ldquo;半分离&rdquo; 方案的相同点是：静态内容的处理方式完全一样。也即服务端首先响应一个 layout 性质的 html 文件给客户端，浏览器先渲染这部分内容，随后再通过 ajax 异步加载动态内容。</p>\r\n\r\n<p>&ldquo;全分离&rdquo; 与 &ldquo;半分离&rdquo; 方案的不同点是：动态内容处理方式不一样。前者获取后端的 json 响应数据然后用 js 生成动态内容并插入到网页。后者获取后端通过模板引擎已经生成好的动态内容，仅仅只需要插入到网页这一个动作即可。当然，仍然支持&nbsp;json 数据交互，视具体功能去选择。</p>\r\n\r\n<p>全分离的主要缺点是需要引入前端技术栈，增加学习成本。再就是由于动态数据是客户端的&nbsp;js 生成的，所以大型复杂应用在低端设备体验会有卡顿。</p>\r\n\r\n<p>半分离的动态内容生成在服务端进行，客户端体验更好，由于 enjoy 模板引擎性能极高，在服务端的性能消耗几乎可以忽略。</p>\r\n\r\n<p>半分离方案下的 html 维护起来更符合直觉，因为 html 这种模式化文本内容的动态化，这本就是模板引擎最典型的应用场景。</p>\r\n\r\n<p>以上只做一点简单介绍，更深入的介绍，二次开发的方法请在官网下载 jfinal admin 项目的配套视频。</p>\r\n','',1,NULL,NULL,0,'2021-02-03 11:17:15','2021-02-03 12:38:39');

/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table image
# ------------------------------------------------------------

DROP TABLE IF EXISTS `image`;

CREATE TABLE `image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountId` int(11) NOT NULL COMMENT '上传者',
  `path` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '相对于jfinal baseUploadPath 的相对路径',
  `fileName` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '文件名',
  `showName` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '显示名',
  `length` int(11) NOT NULL COMMENT '文件长度',
  `created` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;

INSERT INTO `image` (`id`, `accountId`, `path`, `fileName`, `showName`, `length`, `created`)
VALUES
	(2,1,'/image/','1_20200926171145.jpg','1_20200926171145.jpg',69746,'2020-10-02 10:25:49'),
	(3,1,'/image/','1_20200926171135.jpg','1_20200926171135.jpg',73739,'2020-10-03 10:25:49'),
	(4,1,'/image/','1_20200926171305.jpeg','1_20200926171305.jpeg',134981,'2020-10-04 10:25:49'),
	(5,1,'/image/','1_20200926171251.jpg','1_20200926171251.jpg',52549,'2020-10-05 10:25:49'),
	(6,1,'/image/','1_20200926171153.jpg','1_20200926171153.jpg',98774,'2020-10-06 10:25:49'),
	(7,1,'/image/','1_20200912004925.jpg','1_20200912004925.jpg',96153,'2020-10-07 10:25:49'),
	(8,1,'/image/','1_20200911222106.png','1_20200911222106.png',200785,'2020-10-08 10:25:49'),
	(9,1,'/image/','1_20200926171222.jpg','1_20200926171222.jpg',32091,'2020-10-09 10:25:49'),
	(10,1,'/image/','1_20200926171205.jpg','1_20200926171205.jpg',178034,'2020-10-10 10:25:49');

/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table login_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `login_log`;

CREATE TABLE `login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) NOT NULL DEFAULT '' COMMENT '登录使用的账户名',
  `accountId` int(11) DEFAULT NULL COMMENT '登录成功的accountId',
  `state` int(11) NOT NULL COMMENT '登录成功为1,否则为0',
  `ip` varchar(100) DEFAULT NULL,
  `port` int(11) NOT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actionKey` varchar(512) NOT NULL DEFAULT '',
  `controller` varchar(512) NOT NULL DEFAULT '',
  `remark` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;

INSERT INTO `permission` (`id`, `actionKey`, `controller`, `remark`)
VALUES
	(1,'/admin','com.jfinal.admin.index.IndexAdminController','后台管理首页'),
	(2,'/admin/account','com.jfinal.admin.account.AccountAdminController','账户管理首页'),
	(3,'/admin/account/add','com.jfinal.admin.account.AccountAdminController','创建账户页面'),
	(4,'/admin/account/assignRole','com.jfinal.admin.account.AccountAdminController','账户分配角色'),
	(5,'/admin/account/assignRoles','com.jfinal.admin.account.AccountAdminController','账户分配角色页面'),
	(6,'/admin/account/delete','com.jfinal.admin.account.AccountAdminController','删除账户'),
	(7,'/admin/account/edit','com.jfinal.admin.account.AccountAdminController','修改账户'),
	(8,'/admin/account/exportExcel','com.jfinal.admin.account.AccountAdminController','账户导出为Excel'),
	(9,'/admin/account/save','com.jfinal.admin.account.AccountAdminController','创建账户'),
	(10,'/admin/account/showAdminList','com.jfinal.admin.account.AccountAdminController','显示分配了角色的账户'),
	(11,'/admin/account/switchState','com.jfinal.admin.account.AccountAdminController','切换账户状态'),
	(12,'/admin/account/update','com.jfinal.admin.account.AccountAdminController','更新账户'),
	(13,'/admin/article','com.jfinal.admin.article.ArticleAdminController','文章管理首页'),
	(14,'/admin/article/add','com.jfinal.admin.article.ArticleAdminController','创建文章页面'),
	(15,'/admin/article/delete','com.jfinal.admin.article.ArticleAdminController','删除文章'),
	(16,'/admin/article/edit','com.jfinal.admin.article.ArticleAdminController','修改文章'),
	(17,'/admin/article/publish','com.jfinal.admin.article.ArticleAdminController','发布文章'),
	(18,'/admin/article/save','com.jfinal.admin.article.ArticleAdminController','创建文章'),
	(19,'/admin/article/update','com.jfinal.admin.article.ArticleAdminController','更新文章'),
	(20,'/admin/demo/echarts','com.jfinal.admin.demo.DemoAdminController','整合echarts'),
	(21,'/admin/demo/fontAwesome','com.jfinal.admin.demo.DemoAdminController','整合Font Awesome'),
	(22,'/admin/func','com.jfinal.admin.func.FunctionAdminController','功能调用'),
	(23,'/admin/func/clearCache','com.jfinal.admin.func.FunctionAdminController','清除缓存'),
	(24,'/admin/func/clearExpiredSession','com.jfinal.admin.func.FunctionAdminController','清除过期Session'),
	(25,'/admin/func/getTotalOrdersToday','com.jfinal.admin.func.FunctionAdminController','查看今日订单数'),
	(26,'/admin/func/passData','com.jfinal.admin.func.FunctionAdminController','功能调用之传输数据'),
	(27,'/admin/func/restart','com.jfinal.admin.func.FunctionAdminController','重启系统'),
	(28,'/admin/func/switchAccount','com.jfinal.admin.func.FunctionAdminController','切换账户'),
	(29,'/admin/image','com.jfinal.admin.image.ImageAdminController','图片管理首页'),
	(30,'/admin/image/add','com.jfinal.admin.image.ImageAdminController','上传图片页面'),
	(31,'/admin/image/delete','com.jfinal.admin.image.ImageAdminController','删除图片'),
	(32,'/admin/image/upload','com.jfinal.admin.image.ImageAdminController','上传图片'),
	(33,'/admin/latestImage','com.jfinal.admin.index.IndexAdminController','最后上传图片'),
	(34,'/admin/overview','com.jfinal.admin.index.IndexAdminController','概览'),
	(35,'/admin/permission','com.jfinal.admin.permission.PermissionAdminController','权限管理首页'),
	(36,'/admin/permission/delete','com.jfinal.admin.permission.PermissionAdminController','删除权限'),
	(37,'/admin/permission/edit','com.jfinal.admin.permission.PermissionAdminController','修改权限'),
	(38,'/admin/permission/sync','com.jfinal.admin.permission.PermissionAdminController','权限一键同步'),
	(39,'/admin/permission/update','com.jfinal.admin.permission.PermissionAdminController','更新权限'),
	(40,'/admin/role','com.jfinal.admin.role.RoleAdminController','角色管理首页'),
	(41,'/admin/role/add','com.jfinal.admin.role.RoleAdminController','创建角色页面'),
	(42,'/admin/role/assignPermission','com.jfinal.admin.role.RoleAdminController','角色分配权限'),
	(43,'/admin/role/assignPermissions','com.jfinal.admin.role.RoleAdminController','角色分配权限页面'),
	(44,'/admin/role/delete','com.jfinal.admin.role.RoleAdminController','删除角色'),
	(45,'/admin/role/edit','com.jfinal.admin.role.RoleAdminController','修改角色'),
	(46,'/admin/role/save','com.jfinal.admin.role.RoleAdminController','创建角色'),
	(47,'/admin/role/update','com.jfinal.admin.role.RoleAdminController','更新角色'),
	(48,'/admin/system','com.jfinal.admin.system.SystemAdminController','系统管理首页'),
	(49,'/admin/system/avatar','com.jfinal.admin.system.SystemAdminController','更换头像页面'),
	(50,'/admin/system/changeAvatar','com.jfinal.admin.system.SystemAdminController','更换头像'),
	(51,'/admin/article/preview','com.jfinal.admin.article.ArticleAdminController','文章预览');

/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '',
  `created` datetime NOT NULL,
  `remark` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`id`, `name`, `created`, `remark`)
VALUES
	(1,'超级管理员','2021-01-31 12:03:05','拥有所有权限'),
	(2,'权限管理员','2021-01-31 12:03:05','拥有权限管理模块所有权限'),
	(3,'内容管理员','2021-01-31 12:03:05','拥有内容管理模块所有权限'),
	(4,'后台用户','2021-01-31 12:03:05','拥有后台管理模块最基本权限');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `roleId` int(11) NOT NULL,
  `permissionId` int(11) NOT NULL,
  PRIMARY KEY (`roleId`,`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;

INSERT INTO `role_permission` (`roleId`, `permissionId`)
VALUES
	(2,2),
	(2,3),
	(2,4),
	(2,5),
	(2,6),
	(2,7),
	(2,8),
	(2,9),
	(2,10),
	(2,11),
	(2,12),
	(2,35),
	(2,36),
	(2,37),
	(2,38),
	(2,39),
	(2,40),
	(2,41),
	(2,42),
	(2,43),
	(2,44),
	(2,45),
	(2,46),
	(2,47),
	(3,13),
	(3,14),
	(3,15),
	(3,16),
	(3,17),
	(3,18),
	(3,19),
	(3,29),
	(3,30),
	(3,31),
	(3,32),
	(3,51),
	(4,1),
	(4,2),
	(4,7),
	(4,12),
	(4,20),
	(4,21),
	(4,33),
	(4,34),
	(4,48),
	(4,49),
	(4,50);

/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table session
# ------------------------------------------------------------

DROP TABLE IF EXISTS `session`;

CREATE TABLE `session` (
  `id` varchar(33) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT 'session id',
  `accountId` int(11) NOT NULL COMMENT '账户 id',
  `created` datetime NOT NULL COMMENT '创建时间',
  `expires` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
