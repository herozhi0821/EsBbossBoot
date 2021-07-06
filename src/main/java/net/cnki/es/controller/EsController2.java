package net.cnki.es.controller;

import java.util.Date;
import java.util.List;

import org.frameworkset.elasticsearch.ElasticSearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.date.DateUtil;
import net.cnki.es.bean.ProjDoc;
import net.cnki.es.service.EsUtilService;

@RequestMapping("ver2")
@RestController
public class EsController2 {

	@Autowired
	EsUtilService esUtilService;

	//删除索引
	@RequestMapping("drop")
	public void drop() {
        try {
        	esUtilService.dropIndice("projdoc");
        } catch (ElasticSearchException e) {
            e.printStackTrace();
        }
	}
	
	@RequestMapping("hightLightSearch")
    public Object hightLightSearch() throws Exception {
		List<ProjDoc> list = esUtilService.hightLightSearch("projdoc", "allContent", "在这些恶劣的环境载荷的长期作用下", false,ProjDoc.class);
		return list;
    }
	
	@RequestMapping("testHighlightSearch")
    public Object testHighlightSearch() throws Exception {
		String startTime = DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd HH:mm:ss.SSS");
		String endTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
		List<ProjDoc> list = esUtilService.testHighlightSearch("projdoc", "allContent", "在这些恶劣的环境载荷的长期作用下",startTime,endTime, false,ProjDoc.class);
		return list;
    }
    
}
