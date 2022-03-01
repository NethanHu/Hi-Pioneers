/**
 * 初始化
 */
$(function() {
	admin.initCache();					// 初始化缓存
	
	admin.fillContentBox();				// 填充 #content-box
	
	admin.initLeftMenu();				// 初始化左侧菜单
	admin.initFill();					// 初始化 fill
	admin.initOpen();					// 初始化 open
	admin.initConfirm();				// 初始化 confirm
	admin.initSwitch();					// 初始化 switch
	admin.initMsg();					// 初始化 msg
	admin.initPopstate();				// 初始化 popstate 事件，支持浏览器前进、回退功能
	
	admin.initMainMenuArrow();			// 初始化主菜单箭头（方向）
	
	kit.bindEscKeydown();				// 绑定 esc 键 keydown 事件，esc 键支持关闭弹出层
});

/**
 * 后台命名空间为 admin
 */
var admin = {
	
	/**
	 * 缓存菜单、面包屑容器，这些对象被高频使用
	 */
	menuItems : null,
	crumbBox : null,
	
	initCache : function() {
		admin.menuItems = $('div.left-menu a');
		admin.crumbBox = $('#crumb-box');
	},
	
	/**
	 * 寻找最大匹配长度的 menuItem
	 */
	findMaxMatchingMenu : function(pathName) {
		var maxMatchingLength = -1;		// 最大匹配长度
		var maxMatchingMenu = null;		// 最大匹配菜单
		
		// 寻找最大匹配长度的 menuItem
		admin.menuItems.each(function(index, menuItem) {
			var $menuItem = $(menuItem);
			var href = $menuItem.attr('href');
			href = href || $menuItem.attr('url');		// 同时支持 href 与 url
			if (href && pathName.indexOf(href) != -1) {
				if (href.length > maxMatchingLength) {
					maxMatchingLength = href.length;
					maxMatchingMenu = $menuItem;
				}
			}
		});
		
		return maxMatchingMenu;
	},
	
	/**
	 * 初始化主菜单箭头（方向）
	 */
	initMainMenuArrow : function() {
		var maxMatchingMenu = admin.findMaxMatchingMenu(location.pathname);
		if (maxMatchingMenu) {
			// maxMatchingMenu.parent().prev().find('i.menu-arrow').toggleClass('change')	// 样式 .left-main-menu i.change 配合使用
			
			// 只有在子菜单默认处于关闭状态时，才需要在刷新页面时 change 主菜单箭头方向
			var arrow = maxMatchingMenu.parent().prev().find('i.menu-arrow');
			if (arrow.hasClass('fa-angle-up')) {
				arrow.toggleClass('change');
			}
		}
	},
	
	/**
	 * 设置活动菜单
	 *   location.pathname 可获取当前页面的 pathName
	 */
	setActiveMenu : function(pathName) {
		var maxMatchingMenu = admin.findMaxMatchingMenu(pathName);
		
		if (maxMatchingMenu) {
			admin.menuItems.removeClass('active');
			maxMatchingMenu.addClass('active');
			
			// 如果二级菜单初始未被展开，需要将其展开
			// maxMatchingMenu.closest('div').show();
			maxMatchingMenu.closest('div').css('display', 'block');
			
			// 设置面包屑
			admin.setCrumb(maxMatchingMenu);
		}
	},
	
	/**
	 * 设置面包屑
	 */
	setCrumb : function(menu) {
		var crumb;
		var href = menu.attr('href');
		if (href == '/admin') {
			crumb = '<span>首页</span>';
		} else {
			var mainMenuName = menu.parent().prev('div.left-main-menu').find('span').text();
			var subMenuName = menu.text();
			crumb = '<span>' + mainMenuName + '</span><span class="pathchar">/</span><span>' + subMenuName + '</span>';
		}
		
		admin.crumbBox.html(crumb);
	},
	
	/**
	 * 填充 #content-box，并设置当前活动菜单
	 */
	fillContentBox : function() {
		var url = location.pathname + location.search;
		kit.fill(url, null, '#content-box', '正在加载, 请稍候 .....');
		admin.setActiveMenu(location.pathname);
	},
	
	/**
	 * 工具栏刷新按钮，比 fillContentBox 少了一个 setActiveMenu
	 * 
	 * 用法：
	 *   <button class="btn btn-primary btn-sm" onclick="admin.refresh()">
	 *   	<i class="fa fa-rotate-right mr-1"></i>刷新
	 *   </button>
	 *   
	 * 按钮可选颜色：
	 *   btn-success、btn-default、btn-primary、btn-danger、btn-warning
	 *   btn-info、btn-secondary、btn-light、btn-dark
	 */
	refresh : function() {
		var url = location.pathname + location.search;
		kit.fill(url, null, '#content-box', '正在加载, 请稍候 .....');
	},
	
	/**
	 * 初始化 popstate 事件，支持浏览器前进、回退功能
	 */
	initPopstate : function() {
		kit.bindPopstate();
	},
	
	/**
	 * 初始化左侧菜单
	 */
	initLeftMenu : function() {
		// 子菜单点击事件
		kit.bindFill('#left-box', 'a', '#content-box', true, function(url, data, container) {
			kit.fillAndPushState(url, data, container, '正在加载, 请稍候 .....');
			admin.setActiveMenu(url);
		});
		
		// 主菜单点击事件
		$('div.left-main-menu').on('click', function(event) {
			event.preventDefault();
			var $this = $(this);
			
			var mainMenu = $this.find('i.menu-arrow');
			mainMenu.toggleClass('change');	// 样式 .left-main-menu i.change 配合使用 
			
			var subMenu = $this.next('div.left-sub-menu');
			subMenu.slideToggle('fast');
		});
	},
	
	/**
	 * 初始化 fill，点击绑定了 fill 的组件时请求服务端并填充 html 到 #content-box
	 */
	initFill : function() {
		// 'a[href][fill],.paginate a[href]'
		kit.bindFill('#content-box', 'a[fill],button[fill],ul.pagination a[href]', '#content-box', true, function(url, data, container, pushState, event) {
			var func = pushState ? kit.fillAndPushState : kit.fill;
			func(url, data, container, '正在加载, 请稍候 .....');
			admin.setActiveMenu(url);
		});
	},
	
	/**
	 * 初始化 open，点击绑定了 open 的组件时弹出操作界面
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
	initOpen : function() {
		// kit.bindOpen('#content-box', 'a[open],button[open]', '正在加载, 请稍候 .....');
		kit.bindOpen('#root-box', 'a[open],button[open]', '正在加载, 请稍候 .....');
	},
	
	/**
	 * 初始化 confirm，点击绑定了 confirm 的组件时弹出确认对话框
	 * 
	 * 注意：选择器要避开 input[confirm]，因为 switch 组件满足选择器 input[confirm]
	 * 
	 * 超链接约定规则如下：
	 *   1：提供 confirm 属性用于弹出确认对话框显示内容
	 *   2：提供 url 属性指向服务端 action
	 *   3：去除 href 属性，避免在程序开发阶段未确认就执行了某些操作，例如删除操作
	 *   4：可选属性 ok-remove 用于指定在 state 返回值为 'ok' 时删除最近的 html 节点 
	 *   5：可选属性 callback 可以指定回调函数，配置该属性后 ok-remove 将失效
	 *   
	 * 
	 * 例子：
	 *   <a confirm="确定删除 #escape(x.title) ？" ok-remove="tr" url="/admin/article/delete?id=#(x.id)">删除</a>
	 * 
	 *   上例中的 ok-remove="tr" 属性指定了在 state 为 'ok' 时删除最新的 tr 节点
	 */
	initConfirm : function() {
		kit.bindConfirm('#content-box', 'a[confirm],button[confirm]', {shadeClose:true},
			function(ret, $this, index) {
				layer.close(index);
				
				// 如果存在 callback 属性值，则将其当成函数进行回调
				var callback = $this.attr('callback');
				if (callback) {
					eval(callback + '(ret, $this)');
				} else {
					// 经测试 layer.msg(...) 能主动关掉 layer.confirm(...) 弹出层
					kit.msg(ret);
					if (ret.state == 'ok') {
						// 操作成功时删掉 ok-remove 属性指向的目标
						var ok_remove = $this.attr('ok-remove');
						if (ok_remove) {$this.closest(ok_remove).remove();}
					}
				}
			}
		);
	},
	
	/**
	 * 初始化 switch，绑定 input 实现的 ios 风格的开关，实现交互功能
	 * 
	 * input 组件示例：
	 *   <input confirm="确定操作？" #(x.state == 1 ? 'checked':'') url='/admin/article/publish?id=#(x.id)' type="checkbox" class="custom-control-input" id="id-#(x.id)">
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
	 *    ArticleAdminController.publish()
	 * 
	 */
	initSwitch : function() {
		kit.bindSwitch('#content-box', 'div.custom-switch input[url]');
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
	initMsg : function(selector, childSelector, loadingOptions) {
		kit.bindMsg('#content-box', 'button[msg],a[msg]', '正在处理, 请稍候 .....');
	},
	
	/**
	 * 搜索
	 * <form action='/admin/...' id="search-form">
	 * 		<input value="#(keyword)" id="search-keyword">
	 * </form>
	 */
	search : function() {
		var url = $('#search-form').attr('action');
		var keyword = $('#search-keyword').val();
		
		kit.loading({msg:'正在搜索, 请稍候 .....'});
		$.ajax({url: url, data: {keyword: keyword},
			type: 'POST', cache: false, dataType: 'html',
			success: function(ret) {
				kit.closeLoading();
				
				// 如果返回 json 格式数据，则 msg 输出，例如后端验证参数失败会返回 json
				if (kit.isJsonRet(ret)) {
					kit.msg(kit.strToJson(ret), {time:3900, closeBtn:2});
				} else {
					$('#content-box').html(ret);
					$('#search-keyword').select();
				}
			}
		});
		
		// 返回 false 避免表单提交，请求已经使用上面的 $.ajax 发送完成
		return false;
	},
	
	/**
	 * 打开对话框，输入值以后向后端发请求完成交互
	 * 
	 * 示例：
	 *   <button onclick="admin.openDialog(this);" url="/admin/func/dialog" class="btn btn-default btn-sm">
	 *     对话框传参
	 *   </button>
	 */
	openDialog : function(self) {
		var $self = $(self);
		var url = $self.attr('url');
		
		// 对话框可选参数
		var options = {
			formType: 0,		// 输入框类型，支持 0（文本）默认，1（密码），2（多行文本）
			value: '',			// 初始时的值，默认空字符
			maxlength: 140,		// 可输入文本的最大长度，默认 500
			offset: '139px',
			title: '请输入想要切换账户ID'
		};
		
		// 弹出对话框
		layer.prompt(options, function(value, index, elem) {
			layer.close(index);
			kit.post(url, {value : value}, function(ret) {
				kit.msg(ret);
			});
		});
		
	},
	
	/**
	 * 退出登录
	 */
	logout : function() {
		kit.confirm('确定要退出登录 ?', {title: '信息', area: '350px'}, function(index) {
			$.get('/admin/logout', function(ret) {
				if (ret.state == 'ok') {
					window.location.href = '/admin';
				}
			});
			layer.close(index);
		});
	}
	
	
	
}; /*** end of admin ***/

/**
 * 用于将文本复制到剪贴板
 * 
 * 用法：
 *   document.addEventListener('copy', clipboard.hook);
 *   clipboard.copy('被复制的内容');
 */
var clipboard = {
	
	// 用于控制只在调用 clipboard.copy(...) 时拦截事件，放行其它情况下触发的事件
	intercept : false,
	data : '',
	
	hook : function (evt) {
		if (clipboard.intercept) {
			var data = clipboard.data;
			
			clipboard.intercept = false;
			clipboard.data = '';
			
			evt.preventDefault();
			evt.clipboardData.setData('text/plain', data);
		}
	},
	
	copy : function(data) {
		if (window.clipboardData) {
			window.clipboardData.setData('Text', data);
		} else {
			clipboard.intercept = true;
			clipboard.data = data;
			document.execCommand('copy');
		}
	}
};







