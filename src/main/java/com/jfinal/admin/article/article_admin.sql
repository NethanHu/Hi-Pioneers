分页，title、content 两个参数模糊查询
#sql("paginate")
	select * from article
	where 1 = 1
	#if (title)
	  title like concat('%', #para(title), '%')
	#end
	#if (content)
	  content like concat('%', #para(content), '%')
	#end
#end


