/**
 * jfinal-kit.js 是针对常见页面交互而封装的极简工具箱
 * 
 * jfinal-kit.js 是 jfinal 官方第一次轻度介入前端领域，
 * 目的是为了进一步提升 jfinal 的开发效率、降低学习成本
 * 
 * 结合 jfinal 的 enjoy 模板引擎实现前后分离，
 * 无需引入 vue angular react 这样的前端技术栈，
 * 极大降低学习成本、提升开发效率
 * 
 * TODO: 下一版本考虑添加 kit.prompt/kit.bindPrompt 支持带有数据输入的对话框
 *       支持三种格式数据：value、key=value&k2=v2、{key:value, k2:v2}
 * 
 */
var kit = {
	
	// env : {config:{}},
	
	/**
	 * ajax POST 请求
	 * 
	 * 注意：返回值被指定为了 json，而非 html。需要返回 html 时请使用 jquery
	 *      提供的 $.ajax(...) 函数
	 */
	post : function(url, data, callback) {
		return $.ajax({
			url: url, data: data,
			type: 'POST', cache: false, dataType: 'json', // html, json, xml, jsonp, script, text
			// beforeSend: function() {},
			error: function(ret) {alert(ret.statusText);},
			success: function(ret) {
				if (callback) {callback(ret);}
				else {kit.msg(ret);}
			}
		});
	},
	
	/**
	 * ajax GET 请求
	 */
	get : function(url, callback) {
		return $.ajax({url: url, type: 'GET', cache: false, dataType: 'json',
			success: function(ret) {
				if (callback) {callback(ret);}
				else {kit.msg(ret);}
			}
		});
	},
	
	/**
	 * ajax POST 请求发送 json 格式 string 类型的数据，后端通过 getRawData() 获取
	 * 适合向后端传递复杂结构数据
	 * 
	 * 例子：
	 *   kit.postJsonString('/article/update', kit.formToJsonString('form'));
	 */
	postJsonString : function(url, jsonString, callback) {
		return $.ajax({
			url: url, data: jsonString,
			contentType: 'application/json; charset=UTF-8',		// 关键参数
			type: 'POST', cache: false, dataType: 'json',
			error: function(ret) {alert(ret.statusText);},
			success: function(ret) {
				if (callback) {callback(ret);}
				else {kit.msg(ret);}
			}
		});
	},
	
	/**
	 * 将 form 表单中的数据转换成 json 对象，便于快速组装 post 方法的 data 参数
	 * 
	 * 注意：jquery 的 ajax 方法的 data 参数能将 string 自动转成 json，
	 *      所以可以使用 serialize(...) 代替 fromToJson(...)：
	 *        kit.post('/article/save', $('form').serialize());
	 * 
	 * 注意：checkbox 要提供 value 属性，否则选中时的 value 值为 'on'
	 * 
	 * 例子：
	 *   kit.post('/article/save', kit.formToJson('#myFormId'));
	 */
	formToJson : function(formSelector) {
		var paramArray = $(formSelector).serializeArray();
		var jsonObj = {};
		$(paramArray).each(function(index, element) {
			jsonObj[element.name] = element.value;  
		});
		return jsonObj;
	},
	
	/**
	 * 表单序列化成 json 字符串
	 */
	formToJsonString : function(formSelector) {
		var jsonObj = kit.formToJson(formSelector);
		return JSON.stringify(jsonObj);	// json 对象转 json 字符串
	},
	
	/**
	 * 页面跳转
	 */
	go : function(url) {
		window.location.href = url;
	},
	
	/**
	 * 用于 ajax 请求时的通用加载动画
	 * 
	 * 交互完成以后，需要调用返回值的 close() 或者 kit.closeLoading()
	 * 方法关闭来关闭，例如：
	 *   var ref = kit.loading({msg: '正在加载, 请稍候 .....'});
	 *   kit.post(url, data, function(ret) {
	 *   	ref.close();
	 *   	kit.msg(ret);
	 *   });
	 */
	loading : function(options) {
		var opts = {icon:16, time:0, shade:0.1, offset:'139px'};
		if (typeof(options) == 'string') {opts.msg = options;}
		else if (options) {$.extend(opts, options);}
		
		var index = layer.msg(opts.msg ? opts.msg : '正在处理, 请稍候 .....', opts);
		var ret = {index:index, close:function() {layer.close(ret.index);}};
		return ret;
	},
	
	/**
	 * 关闭 kit.loading(...) 打开的加载动画
	 * 
	 * 关闭最新弹出的层：layer.close(layer.index); 开发 esc 键关闭层的功能有用
	 */
	closeLoading : function(index) {
		// 由于加载层基于 layer.msg(...)，所以是关闭 'dialog'
		if (index) {layer.close(index);} else {layer.closeAll('dialog');}
	},
	
	/**
	 * 搭配 jfinal 的 com.jfinal.kit.Ret 对象对应的 json，弹出层显示提示信息
	 */
	msg : function(ret, options) {
		if (ret && ret.state) {
			if (ret.state == 'ok') {
				kit.ok(ret.msg ? ret.msg : '操作成功', options);
			} else if (ret.state == 'fail') {
				kit.fail(ret.msg ? ret.msg : '操作失败', options);
			} else {
				alert("state 必须为 'ok' 或者 'fail'");
			}
		}
	},
	
	/**
	 * 绑定 click 事件请求后端 action，得到返回值以后执行 kit.msg(...) 显示操作结果
	 * 
	 * button 组件示例：
	 *   <button msg="{offset:'139px'}" url="/admin/func/getTotalOrders" class="btn btn-default btn-sm">
	 *      查看今日订单数
	 *   </button>
	 * 
	 * 以上 button 组件示例中展示的规则：
	 *   1：url 用指向后端操作的 action 路由
	 *   2：msg 用于指定打开弹出层时的 options 配置
	 *   3：该交互模式常用于 button、a 两种标签，可通过修改 jquery 选择器绑定到其它组件上
	 */
	bindMsg : function(selector, childSelector, loadingOptions) {
		$(selector).on('click', childSelector, function(event) {
			event.preventDefault();
			
			var $this = $(this);
			var url = $this.attr('url');
			
			// 支持 json 格式与 key=value 格式。例如：data="{k1:123, k2:'abc'}" data="k1=a&k2=1"
			var data = $this.attr('data');
			data = kit.strToJson(data);
			
			// 支持 json 格式：{offset:'139px'}
			var options = $this.attr('msg');
			if (options) {options = kit.strToJson(options);}
			
			var ref;
			if (loadingOptions) {ref = kit.loading(loadingOptions);}
			kit.post(url, data, function(ret) {
				if (loadingOptions) {ref.close();}
				kit.msg(ret, options);
				
				// 如果存在 callback 属性值，则将其当成函数进行回调
				var callback = $this.attr('callback');
				if (callback) {
					eval(callback + '(ret, $this)');
				}
				
			});
		});
	},
	
	/**
	 * ajax 操作成功，弹出层显示成功信息
	 * options 参考文档：https://www.layui.com/doc/modules/layer.html
	 */
	ok : function(msg, options) {
		var opts = {icon:1, anim:0, shade:0.1, offset:'139px', time:1500};
		if (options) {$.extend(opts, options);}
		layer.msg(msg, opts);
	},
	
	/**
	 * ajax 操作失败，弹出层显示失败信息
	 */
	fail : function(msg, options) {
		var opts = {icon:2, anim:6, shade:0.3, offset:'139px', time:3100};
		if (options) {$.extend(opts, options);}
		layer.msg(msg, opts);
	},
	
	/**
	 * 字符串格式的数据转成 json 对象
	 * 不能转换的数据原样返回，对于 k1=v1&k2=v2 格式数据，仍然可供 $.ajax(...) data 参数使用
	 * 警告：此方法仅供内部使用
	 */
	strToJson : function(str) {
		if (str) {
			var temp = str.trim();
			if (temp.length >= 2) {
				temp = temp.charAt(0);
				// 必须判断字符 '{'、'['，否则 eval(key=value) 将返回 value
				if (temp == '{' || temp == '[') {
					try {return eval('(' + str + ')');} catch (error) {}
				}
			}
		}
		return str;
	},
	
	/**
	 * 判断 ajax 返回值是否为 json 字符串
	 * 
	 * 注意：必须配置 dataType: 'html' 或 'text'，返回值才是字符串，本方法不能判断 dateType : 'json' 的返回值
	 */
	isJsonRet : function(ret) {
		if (ret && ret.length > 6) {
			var temp = ret.substring(0, 6);
			if (temp === '{"msg"' || temp === '{"stat') {
				return true;
			}
		}
		return false;
	},
	
	/**
	 * ajax 填充 html 片段
	 * container 参数支持任意 jquery 选择器语法，例如：'#id'、'.content-box .edit-profile'
	 */
	fill : function(url, data, container, loadingOptions) {
		var ref;
		if (loadingOptions) {ref = kit.loading(loadingOptions);}
		
		return $.ajax({
			url: url, data: data,
			type: (data ? 'POST' : 'GET'),
			cache: false, dataType: 'html',
			error: function(ret) {alert(ret.statusText);},
			complete: function() {if (loadingOptions) {ref.close();}},
			// document.getElementById(...).innerHTML = ret; 不会执行返回值中的 js
			success: function(ret) {
				// 如果返回值为 json 字符串，将其转成 json 对象并用 kit.msg(...) 输出
				if (kit.isJsonRet(ret)) {
					kit.msg(kit.strToJson(ret), {time:3900, closeBtn:2});
				} else {
					$(container).html(ret);
				}
			}
		});
	},
	
	/**
	 * ajax 填充 html 片段以后，再调用 pushState，用于支持浏览器回退、前进功能
	 */
	fillAndPushState : function(url, data, container, loadingOptions) {
		var ret = kit.fill(url, data, container, loadingOptions);
		
		// 改变地址栏 url 并添加新的历史记录，支持浏览器回退、前进功能
		var state = {url: url};
		window.history.pushState(state, '', state.url);
		
		return ret;
	},
	
	/**
	 * 绑定 popstate 事件，支持浏览器前进、回退功能
	 * 
	 * 注意：需要 window.history.pushState(state, "", state.url) 配合才能支持
	 *      具体用法参考 kit.fillAndPushState(...)
	 */
	bindPopstate : function() {
		// $(window).on('popstate'...) 绑定需要 event.originalEvent.state 获取 state
		window.addEventListener('popstate', function(event) {
			// state 为 PopStateEvent 事件持有的属性，指向 pushState 函数的第一个参数
			var state = event.state;
			if (state && state.url) {
				location.href = state.url;
			} else {
				location.href = document.location;
			}
		});
	},
	
	/**
	 * 绑定 esc 键的 keydown 事件，支持 esc 键关闭 layer 弹出层
	 */
	bindEscKeydown : function() {
		$(document).on('keydown', function(event) {
			if (event.keyCode === 27) {
				layer.close(layer.index);
			}
		});
	},
	
	/**
	 * 绑定 click 事件执行 kit.fill(...) 操作
	 * 
	 * 注意：被绑定元素需要有 href 属性值，该值被用于获取填充数据
	 * 
	 * 例子：
	 *   kit.bindFill('#content-box', 'a[fill],.paginate a[href]', '#content-box', true);
	 *   
	 *   kit.bindFill('ul.left-menu', 'a[href]', '#content-box', true, function(url, data, container, event) {
	 *       kit.fillAndPushState(url, data, container, '正在加载, 请稍候 .....');
	 *       admin.setActiveMenu(url);
	 *   });
	 * 
	 * 参考 pjax 绑定设计：
	 *   $(document).pjax('[data-pjax] a, a[data-pjax]', '#pjax-container');
	 */
	bindFill : function(selector, childSelector, container, pushState, callback) {
		$(selector).on('click', childSelector, function(event) {
			event.preventDefault();
			
			// event.target.href 或者 $(event.target).attr('href')
			var $this = $(this);
			var url = $this.attr('href');
			
			// 支持 url 代替 href，注意绑定时选择器需要正确绑定
			// fill 功能沿用 href 原因：搜索引擎收录友好，对 css 样式友好，如：a:not([href]) {color: inherit;}
			url = url || $this.attr('url');
			
			// 跳过 href="javascript:void(0);"
			if (url.indexOf('javascript:') == 0) {
				return ;
			}
			
			// 支持 json 格式与 key=value 格式。例如：data="{k1:123, k2:'abc'}" data="k1=a&k2=1"
			var data = $this.attr('data');
			data = kit.strToJson(data);
			
			// 优先使用 a 标签上 fill 属性值指定的 container
			var ctn = $this.attr('fill');
			ctn = ctn ? ctn : container;
			
			// 优先使用 a 标签上的 pushState 属性值
			var ps = $this.attr('pushState');
			if (ps == 'true') {
				ps = true;
			} else if (ps == 'false') {
				ps = false;
			} else {
				ps = pushState;
			}
			
			if (callback) {
				// 对外开放的回调函数可以使用五个参数
				callback(url, data, ctn, ps, event);
			} else {
				var func = ps ? kit.fillAndPushState : kit.fill;
				func(url, data, ctn, '正在加载, 请稍候 .....');
			}
		});
	},
	
	/**
	 * ajax 加载远程 html 片段，将 html 片段作为 content 参数打开页面层
	 * 例子：
	 *   kit.open(url, {id:123}, {area:'400px', offset:'139px'});
	 */
	open : function(url, data, options, loadingOptions) {
		var ref;
		if (loadingOptions) {ref = kit.loading(loadingOptions);}
		
		$.ajax({
			url: url, data: data,
			type: (data ? 'POST' : 'GET'),
			cache: false, dataType: 'html',
			error: function(ret) {alert(ret.statusText);},
			complete: function() {if (loadingOptions) {ref.close();}},
			success: function(ret) {
				if (loadingOptions) {ref.close();}
				
				
				// 处理 AdminAuthInterceptor 返回的没有操作权限或者已退出登录返回的 json 数据
				if (kit.isJsonRet(ret)) {
					kit.msg(kit.strToJson(ret), {time:3900, closeBtn:2});
					return ;
				}
				
				
				var opts = {
					title:false, content:ret,
					type:1, time:0, shadeClose:false, shade:0.3, closeBtn:2
				};
				if (options) {$.extend(opts, options);}
				layer.open(opts);
			}
		});
	},
	
	/**
	 * 绑定 click 事件执行 kit.open(...) 操作
	 * 
	 * button 组件示例：
	 *   <button open="{area:'500px', offset:'139px'}" url="/admin/account/add" class="btn btn-success btn-sm">
	 *   	<i class="fa fa-plus mr-1"></i>创建
	 *   </button>
	 * 
	 * 以上 button 组件示例中展示的规则：
	 *   1：url 用指向后端操作的 action 路由
	 *   2：open 用于指定打开弹出层时的 options 配置
	 *   3：该交互模式常用于 button、a 两种标签，可通过修改 jquery 选择器绑定到其它组件上
	 *   
	 *  遵守以上规则，在使用的时候前端只需调用 admin.initOpen() 即可使用，
	 *  后端只需要创建相应的 action 获取 render(...) 响应需要在弹出层显示的
	 *  html 片段即可，返回的 html 中可以包含 js、css、html
	 *  从而实现 UI 交互、功能的模块化
	 */
	bindOpen : function(selector, childSelector, loadingOptions) {
		$(selector).on('click', childSelector, function(event) {
			event.preventDefault();
			
			var $this = $(this);
			var url = $this.attr('url');
			
			// 支持 json 格式与 key=value 格式。例如：data="{k1:123, k2:'abc'}" data="k1=a&k2=1"
			var data = $this.attr('data');
			data = kit.strToJson(data);
			
			// 支持 json 格式：{area:'500px', offset:'139px'}
			var options = getAttr($this, 'open');
			if (options) {options = kit.strToJson(options);}
			
			// yes 为确定按钮回调方法，指定该函数用于避免点击之后对话框被自动关闭
			if (options) {options.yes = function(index) {};}
			
			kit.open(url, data, options, loadingOptions);
		});
		
		// 由于 jquery attr(...) 无法获取标签上的 open 属性，在此改用原生 js
		function getAttr(jqObj, attrName) {
			var ret = null;
			if (jqObj.length > 0) {
				ret = jqObj[0].attributes[attrName];
			}
			return ret && ret.value ? ret.value : null;
		}
	},
	
	/**
	 * 封装 layer.confirm(...) 确认对话框，添加一些默认配置
	 * 文档：https://www.layui.com/doc/modules/layer.html
	 * 
	 * 所有参数均与 layer.confirm(...) 保持一致：
	 *   content: 对话框内容
	 *   options: layer 弹是出层 confirm(...) 函数的 options 参数，该参数可以省略
	 *   yes: 确定按钮回调函数
	 *   cancel: 取消按钮回调函数
	 */
	confirm : function(content, options, yes, cancel) {
		// 支持省略 options 参数
		if ( typeof(options) === 'function' ) {
			kit.confirm(content, null, options, yes);
			return ;
		}
		
		// 右上角关闭按钮事件回调需添加属性 cancel:function() {}
		var opts = {title:'信息', closeBtn:2, scrollbar:true, shade:0.3, icon:3, offset:'139px'};
		if (cancel) {opts.cancel = cancel}	// 右上角关闭按钮和取消按钮一样，都使用 cancel 回调函数
		if (options) {$.extend(opts, options);}
		
		layer.confirm(content, opts,
			function(index) {if (yes) yes(index);},
			function(index) {if (cancel) return cancel(index);}
		);
	},
	
	/**
	 * 绑定 click 事件执行 kit.confirm(...) 操作
	 * 
	 * 注意：选择器要避开 input[confirm]，因为 switch 组件满足选择器 input[confirm]
	 * 
	 * 超链接约定规则如下：
	 *   1：提供 confirm 属性用于弹出确认对话框显示内容
	 *   2：提供 url 属性指向服务端 action
	 *   3：去除 href 属性，避免在程序开发阶段未确认就执行了某些操作，例如删除操作
	 * 
	 * 例子：
	 *   <a confirm="确定删除 #escape(x.title) ？" url="/admin/blog/delete?id=#(x.id)">删除</a>
	 */
	bindConfirm : function(selector, childSelector, options, yes, cancel) {
		$(selector).on('click', childSelector, function(event) {
			event.preventDefault();
			
			var $this = $(this);
			var confirm = $this.attr('confirm');
			var url = $this.attr('url');
			
			// 支持 json 格式与 key=value 格式。例如：data="{k1:123, k2:'abc'}" data="k1=a&k2=1"
			var data = $this.attr('data');
			data = kit.strToJson(data);
			
			kit.confirm(confirm, options, function(index) {
				kit.post(url, data, function(ret) {
					if (yes) {
						// 对外开放的回调函数可以使用三个参数
						yes(ret, $this, index);
					} else {
						layer.close(index);
						kit.msg(ret);
					}
				});
			},
			function(index) {
				if (cancel) {return cancel($this, index);}
			});
		});
	},
	
	/**
	 * checkbox 控件切换状态并调用 url 所指向的 action
	 */
	switch_ : function(selector, callback) {
		kit.doSwitch($(selector), callback);
	},
	
	// 从 url 属性得到后端 action 路由，并发起 ajax 请求
	doSwitch : function($this, callback) {
		// prop 方法可确定取到 true/false 值
		var checked = $this.prop('checked');
		var url = $this.attr('url');
		kit.post(url, {checked: checked}, function(ret) {
			if (ret.state == 'fail') {
				// 还原 checked 状态
				$this.prop('checked', !checked);
			}
			
			if (callback) {
				// 对外开放的回调函数可以使用三个参数
				callback(ret, $this, checked);
			} else {
				kit.msg(ret);
			}
		});
	},
	
	/**
	 * 绑定 click 事件到 checkbox 实现的 switch 开关组件
	 * 
	 * input 组件示例：
	 *   <input confirm="确定操作？" #(x.state == 1 ? 'checked':'') url='/admin/blog/publish?id=#(x.id)' type="checkbox" class="custom-control-input" id="id-#(x.id)">
	 * 
	 * 以上input 组件示例中展示的规则：
	 *   1：url 用指向后端操作的 action 路由
	 *   2：代码 #(x.state == 1 ? 'checked':'') 用于输出 input 标签的 checked 属性
	 *   3：confirm 用于显示确认对话框，不配置时不弹出确认对话框 
	 *   4：注意 id="id-#(x.id)" 仅仅遵循了 bootstrap 4 样式要求，该值与紧邻的 label 标签
	 *        中的 id 值保持一致即可，仅用于实现 switch 按钮的基本 UI 交互，不参与后端交互
	 * 
	 *  遵守以上规则，在使用的时候前端只需调用 admin.initSwitch() 即可使用，后端只需要
	 *  创建相应的 action 获取 id、checked 这两个参数实现相应业务即可，具体参考
	 *    BlogAdminController.publish()
	 * 
	 */
	bindSwitch : function(selector, childSelector, callback) {
		$(selector).on('click', childSelector, function(e) {
			$this = $(this);
			var confirmMsg = $this.attr('confirm');
			if (confirmMsg) {
				kit.confirm(confirmMsg, function(index) {
					layer.close(index);
					kit.doSwitch($this, callback);
				},
				function() {
					// 还原 checked 状态
					$this.prop('checked', !$this.prop('checked'));
				});
			}
			else {
				kit.doSwitch($this, callback);
			}
		});
	}
	
	
	
	
};	/*** end of kit ***/


/**
 * 扩展 jquery
 * 
 * TODO 考虑将 formToJson、formToJsonString 做成 jquery 扩展
 */
$.fn.extend({
	
	/**
	 * 判断是否存在指定的属性名
	 */
	hasAttr : function(attrName) {
		return this.length > 0 && this[0].hasAttribute(attrName);
	}
});














