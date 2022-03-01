/**
 * @license Copyright (c) 2003-2020, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	
	
	/**
	 * 本配置文件初始没有任何配置项，以下所有内容为添加的自定义配置项
	 * 官方配置文档：https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_config.html
	 */
	
	
	
	config.toolbar = [
		{ name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike' ] },
		{ name: 'links', items: [ 'Link', 'Unlink' ] },
		{ name: 'insert', items: [ 'Image', 'Table' ] },
		{ name: 'colors', items: [ 'TextColor', 'BGColor' ] },
		{ name: 'paragraph', items: [ 'NumberedList', 'BulletedList', '-', 'JustifyLeft', 'JustifyCenter' ] },
		{ name: 'styles', items: [ 'Format' ] },
		// { name: 'tools', items: [ 'Source', 'Maximize' ] }	// 全屏功能无法使用
		{ name: 'tools', items: [ 'Blockquote', 'CodeSnippet', 'Source' ] }
	];
	
	config.height = '350px';
	
	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Subscript,Superscript,Anchor';
	
	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;h4;pre';
	
	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;image:Link;link:advanced;link:upload;flash:Upload';
	
	config.uiColor = '#ffffff';
	config.resize_dir = 'vertical';
	
	config.defaultLanguage = 'zh-cn';
	config.skin = 'moono-lisa';
	
	config.font_names =
	'默认/Lantinghei SC, Open Sans, Arial, Hiragino Sans GB, Microsoft YaHei, STHeiti, WenQuanYi Micro Hei, SimSun, sans-serif;' +
	'宋体/宋体, SimSun;' +
	'微软雅黑/微软雅黑, Microsoft YaHei;' +
	'楷体/楷体, 楷体_BG2312, SimKai;' +
	'黑体/黑体, SimHei;' +
	'隶书/隶书, SimLi;' +
	'Arial/Arial, Helvetica, sans-serif;' +
	'Comic Sans MS/Comic Sans MS, cursive;' +
	'Courier New/Courier New, Courier, monospace;' +
	'Georgia/Georgia, serif;' +
	'Lucida Sans Unicode/Lucida Sans Unicode, Lucida Grande, sans-serif;' +
	'Tahoma/Tahoma, Geneva, sans-serif;' +
	'Times New Roman/Times New Roman, Times, serif;' +
	'Trebuchet MS/Trebuchet MS, Helvetica, sans-serif;' +
	'Verdana/Verdana, Geneva, sans-serif';
	
	// 优选代码高亮主题： darcula-for-jfinal darcula zenburn dark monokai_sublime obsidian
	config.codeSnippet_theme = 'darcula-for-jfinal';
	
	config.codeSnippet_languages = {
			java : 'Java',
			javascript : 'JavaScript',
			html : 'HTML',
			xml : 'XML',
			sql : 'SQL',
			json : 'JSON',
			css : 'CSS',
			bash : 'Bash'
		};
	
	config.allowedContent = false; // 允许输入任何标签
	config.disallowedContent = 'img{width,height,float,margin,border-width,border-style}'; // 禁止的标签
	config.extraAllowedContent = 'img[width,height,align];video[controls,id,height,poster,width,autoplay];source[src,type];div(ref);hr;span{color};p{color};strong{color}'; // 允许的标签 div(ref) 为引用评论
	config.tabSpaces = 4;
	
	config.pasteFilter = null;
	
	config.font_defaultLabel = '默认';
	
    // config.removePlugins = 'flash';
    config.removePlugins = 'flash,markdown';
	config.extraPlugins = 'emoji,uploadimage';
	
	// 粘贴图片上传
	config.uploadUrl = '/admin/image/upload';
	
	// 工具栏图像按钮图片上传
	config.filebrowserUploadUrl = '/admin/image/upload';
	
	// 上传图片对话框预览框显示文本，不配置则会显示一堆官方文本
	config.image_previewText = " ";
};




