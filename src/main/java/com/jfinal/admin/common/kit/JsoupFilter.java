/**
 * 本项目采用《JFinal 俱乐部授权协议》，保护知识产权，就是在保护我们自己身处的行业。
 * 
 * Copyright (c) 2011-2021, jfinal.com
 */

package com.jfinal.admin.common.kit;

import com.jfinal.admin.common.model.Account;
import com.jfinal.plugin.activerecord.Model;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import java.util.List;

/**
 * 使用 Jsoup 对 html 进行过滤
 */
@SuppressWarnings("rawtypes")
public class JsoupFilter {

	/**
	 * 用于过滤 content 字段的白名单，需要允许比较多的 tag
	 */
	private static final Whitelist contentWhitelist = createContentWhitelist();
	private static final Document.OutputSettings notPrettyPrint = new Document.OutputSettings().prettyPrint(false);

	private static Whitelist createContentWhitelist() {
		return  Whitelist.relaxed()
		/**
		 * 必须要删除应用在 a 与 img 上的 protocols，否则就只有使用了这些 protocol 的才不被过滤，比较蛋疼
		 * 在 remove 的时候，后面的 protocols 要完全一个不露的对应上 jsoup 默认已经添加的，否则仍然会被过滤掉
		 * 在升级 jsoup 后需要测试这 a 与 img 的过滤是否正常
		 */
		.removeProtocols("a", "href", "ftp", "http", "https", "mailto")
		.removeProtocols("img", "src", "http", "https")

		.addAttributes("a", "href", "title", "target")  // 官方默认会将 target 给过滤掉
		
		/**
		 * 放开 video
		 */
		.addAttributes("video", "src", "controls", "width", "height", "poster", "preload", "muted", "loop", "autoplay")
		
		/**
		 * 在 Whitelist.relaxed() 之外添加额外的白名单规则
         */
		.addTags("div", "span", "embed", "object", "param")
		.addAttributes(":all", "style", "class", "id", "name")
		.addAttributes("object", "width", "height", "classid", "codebase")
		.addAttributes("param", "name", "value")
		.addAttributes("embed", "src", "quality", "width", "height", "allowFullScreen", "allowScriptAccess", "flashvars", "name", "type", "pluginspage");
	}

	/**
	 * 过滤 model 中的 title 与 content 字段，其中 title 过滤为纯 text
	 * content 使用 contentWhitelist 过滤
	 */
	public static void filterTitleAndContent(Model m) {
		String title = m.getStr("title");
		if (title != null) {
			m.set("title", getText(title));
		}
		String content = m.getStr("content");
		if (content != null) {
			m.set("content", filterArticleContent(content));
		}
	}

	/**
	 * 过滤 Account 中的 nickName，过滤为纯 text
	 */
	public static void filterAccountNickName(Account account) {
		String nickName = account.getNickName();
		if (nickName != null) {
			account.setNickName(getText(nickName));
		}
	}

	/**
	 * 对要显示在列表中的 article list 进行过滤，将其中的 title content 转成纯文本
	 */
	public static void filterArticleList(List<? extends Model> modelList, int titleLen, int contentLen) {
		for (Model m : modelList) {
			String title = getText(m.getStr("title"));
			if (title.length() > titleLen) {
				title = title.substring(0, titleLen - 1);
			}

			String content = getText(m.getStr("content")).replaceAll("&nbsp;", " ");
			if (content.length() > contentLen) {
				content = content.substring(0, contentLen - 1);
			}
			m.set("title", title);
			m.set("content", content);
		}
	}

	/**
	 * 对文章 content 字段过滤
	 */
	public static String filterArticleContent(String content) {
		checkXssAndCsrf(content);
		
		// return content != null ? Jsoup.clean(content, contentWhitelist) : null;
		// 添加 notPrettyPrint 参数，避免重新格式化，主要是 at me 时候不会在超链前面添加 "\n"
		return content != null ? Jsoup.clean(content, "", contentWhitelist, notPrettyPrint) : null;
	}
	
	public static void checkXssAndCsrf(String html) {
		Document doc = Jsoup.parseBodyFragment(html);
		if (linkHrefContainsJavascript(doc)) {
			throw new RuntimeException("链接包含 javascript 脚本，html 内容为: " + html);
		}
		
		if (srcPointToAction(doc)) {
			throw new RuntimeException("src 未指向静态资源，html 内容为: " + html);
		}
	}
	
	/**
	 * 防止 img 等标签的 src 属性指向 action 进行 csrf 攻击
	 */
	public static boolean srcPointToAction(Document doc) {
		Elements elements = doc.select("[src]");
		for (Element e : elements) {
			String src = e.attr("src");
			if (src != null) {
				int questionCharIndex = src.indexOf('?');
				if (questionCharIndex != -1) {
					src = src.substring(0, questionCharIndex);
				}
				
				if (src.indexOf('.') == -1) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 防止 a 标签的 href 属性中注入 javascript 脚本，例如：
	 * <a href="javascript:alert(0);">点我</a>
	 */
	public static boolean linkHrefContainsJavascript(Document doc) {
		Elements elements = doc.select("a[href]");
		for (Element e : elements) {
			String href = e.attr("href");
			if (href != null && href.toLowerCase().indexOf("javascript") != -1) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 对文章 title 过滤过滤
	 */
	public static String filterArticleTitle(String title) {
		return getText(title);
	}

	/**
	 * 过滤 content，但保留换行回车符
	 */
	public static String filterContentKeepNewline(String content) {
		return content != null ? Jsoup.clean(content, "", contentWhitelist, notPrettyPrint) : null;
	}

	/**
	 * 将回车换行符过滤成 <br> 标记。三次 replace 为兼容 windows、linux、mac os 输入
	 * windows换行为：\r\n
	 * linux 换行为：\n
	 * mac os 换行为：\r
	 */
	public static String filterNewlineToBrTag(String content) {
		return content != null ? content.replaceAll("\r\n", "<br>").replaceAll("\r", "<br>").replaceAll("\n", "<br>") : null;
	}

	/**
	 * 获取 html 中的纯文本信息，过滤所有 tag
 	 */
	public static String getText(String html) {
		return html != null ? Jsoup.clean(html, Whitelist.none()) : null;
	}

	/**
	 * 使用Whitelist.simpleText() 白名单，获取 simple html 内容
	 * 允许的 tag："b", "em", "i", "strong", "u"
	 */
	public static String getSimpleHtml(String html) {
		return html != null ? Jsoup.clean(html, Whitelist.simpleText()) : null;
	}

	/**
	 * 使用Whitelist.simpleText() 白名单，获取的 basic 内容
	 * 允许的 tag："a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em",
	 *                     "i", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub",
	 *                     "sup", "u", "ul"
	 */
	public static String getBasic(String html) {
		checkXssAndCsrf(html);
		
		return html != null ? Jsoup.clean(html, Whitelist.basic()) : null;
	}
	
	/**
	 * 使用Whitelist.basicWithImages() 白名单，获取的 basic with images 内容
	 */
	public static String getBasicWithImages(String html) {
		checkXssAndCsrf(html);
		
		return html != null ? Jsoup.clean(html, Whitelist.basicWithImages()) : null;
	}

	/**
	 * 使用Whitelist.relaxed() 白名单，获取比较宽松的内容
	 * 允许的 tag: "a", "b", "blockquote", "br", "caption", "cite", "code", "col",
	 *                   "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6",
	 *                   "i", "img", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong",
	 *                   "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul"
	 */
	public static String getRelaxed(String html) {
		return html != null ? Jsoup.clean(html, Whitelist.relaxed()) : null;
	}
	
	/**
	 * 使用指定的 Whitelist 进行过滤
	 */
	public static String getWithWhitelist(String html, Whitelist whitelist) {
		return html != null ? Jsoup.clean(html, whitelist) : null;
	}

	/**
	 * 使用指定的 tags 进行过滤
	 */
	public static String getWithTags(String html, String... tags) {
		return html != null ? Jsoup.clean(html, Whitelist.none().addTags(tags)) : null;
	}

	/**
	 * 获取第一个 img 的 src 属性值
	 */
	public static String getFirstImgSrc(String html) {
		if (html != null) {
			Document doc = Jsoup.parseBodyFragment(html);
			Element image = doc.select("img").first();
			return image != null ? image.attr("src") : null;
		} else {
			return null;
		}
	}
}
