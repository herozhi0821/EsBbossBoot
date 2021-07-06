package net.cnki.es.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.elasticsearch.ElasticSearchException;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.date.DateUtil;
import net.cnki.es.bean.ProjDoc;

@RequestMapping("ver1")
@RestController
public class EsController {

	@Autowired
    BBossESStarter bbossESStarter;
	
	@RequestMapping("test001")
	public void toTest() {
		//判断索引类型是否存在，false表示不存在，正常返回true表示存在
        boolean exist = bbossESStarter.getRestClient().existIndiceType("projdoc","projdoc");
        System.out.println(exist);
        
        //判读索引是否存在，false表示不存在，正常返回true表示存在
        exist =  bbossESStarter.getRestClient().existIndice("projdoc");
        System.out.println(exist);
	}
	
	//添加索引
	@RequestMapping("add")
	public Object contextLoads() {
		//获取加载读取dsl xml配置文件的api接口实例，可以在代码里面直接通过dsl的配置名称引用dsl即可
        ClientInterface clientUtil = bbossESStarter.getConfigRestClient("esmapper/projdoc.xml");
        try {
            boolean exist = clientUtil.existIndice("projdoc");
            if(exist) {
                String r = clientUtil.dropIndice("projdoc");
                System.out.println("删除索引定义:"+r);
            }
            clientUtil.createIndiceMapping("projdoc","createProInfo");//Index mapping DSL script name
            String demoIndice = clientUtil.getIndice("projdoc");
            System.out.println("获取索引定义:"+demoIndice);
            return demoIndice;
        } catch (ElasticSearchException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	//------------------------------------添加数据-------------------------------
	@RequestMapping("testCRUDAdd1")
    public Object testCRUDAdd1() throws Exception {
		String allContent = "海洋平台结构长期服役在恶劣的海洋环境中，并受到各种载荷的交互作用，如风载荷、海流、波浪载荷、冰载荷等，有时还要遭受到地震、台风、海啸、船碰撞等意外灾害，结构本身还要遭受环境腐蚀、海洋生物附着、海底冲刷等的影响。在这些恶劣的环境载荷的长期作用下，容易产生各种形式的损伤，使结构的承载能力下降，对平台构成潜在威胁，严重的还会导致平台失效。如1980年3月27日，挪威“亚历山大·基兰”号钻井平台在9级海风作用下一根支柱发生断裂，平台15分钟后沉入海底，123人遇难。海洋平台结构长期服役在恶劣的海洋环境中，并受到各种载荷的交互作用，如风载荷、海流、波浪载荷、冰载荷等，有时还要遭受到地震、台风、海啸、船碰撞等意外灾害，结构本身还要遭受环境腐蚀、海洋生物附着、海底冲刷等的影响。在这些恶劣的环境载荷的长期作用下，容易产生各种形式的损伤，使结构的承载能力下降，对平台构成潜在威胁，严重的还会导致平台失效。如1980年3月27日，挪威“亚历山大·基兰”号钻井平台在9级海风作用下一根支柱发生断裂，平台15分钟后沉入海底，123人遇难。";
 		String researchOne = "海洋平台结构长期服役在恶劣的海洋环境中，并受到各种载荷的交互作用，如风载荷、海流、波浪载荷、冰载荷等，有时还要遭受到地震、台风、海啸、船碰撞等意外灾害，结构本身还要遭受环境腐蚀、海洋生物附着、海底冲刷等的影响。在这些恶劣的环境载荷的长期作用下，容易产生各种形式的损伤，使结构的承载能力下降，对平台构成潜在威胁，严重的还会导致平台失效。如1980年3月27日，挪威“亚历山大·基兰”号钻井平台在9级海风作用下一根支柱发生断裂，平台15分钟后沉入海底，123人遇难。";
		ProjDoc projDoc = new ProjDoc("1", "文件名", "项目名", "参与人员", allContent, researchOne, new Date());
		
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		String resultString = clientUtil.addDocument(projDoc);

		return resultString;
    }
	
	@RequestMapping("testCRUDAdd2")
    public Object testCRUDAdd2() throws Exception {
		String allContent = "海洋平台结构长期服役在恶劣的海洋环境中，并受到各种载荷的交互作用，如风载荷、海流、波浪载荷、冰载荷等，有时还要遭受到地震、台风、海啸、船碰撞等意外灾害，结构本身还要遭受环境腐蚀、海洋生物附着、海底冲刷等的影响。在这些恶劣的环境载荷的长期作用下，容易产生各种形式的损伤，使结构的承载能力下降，对平台构成潜在威胁，严重的还会导致平台失效。如1980年3月27日，挪威“亚历山大·基兰”号钻井平台在9级海风作用下一根支柱发生断裂，平台15分钟后沉入海底，123人遇难。海洋平台结构长期服役在恶劣的海洋环境中，并受到各种载荷的交互作用，如风载荷、海流、波浪载荷、冰载荷等，有时还要遭受到地震、台风、海啸、船碰撞等意外灾害，结构本身还要遭受环境腐蚀、海洋生物附着、海底冲刷等的影响。在这些恶劣的环境载荷的长期作用下，容易产生各种形式的损伤，使结构的承载能力下降，对平台构成潜在威胁，严重的还会导致平台失效。如1980年3月27日，挪威“亚历山大·基兰”号钻井平台在9级海风作用下一根支柱发生断裂，平台15分钟后沉入海底，123人遇难。";
 		String researchOne = "海洋平台结构长期服役在恶劣的海洋环境中，并受到各种载荷的交互作用，如风载荷、海流、波浪载荷、冰载荷等，有时还要遭受到地震、台风、海啸、船碰撞等意外灾害，结构本身还要遭受环境腐蚀、海洋生物附着、海底冲刷等的影响。在这些恶劣的环境载荷的长期作用下，容易产生各种形式的损伤，使结构的承载能力下降，对平台构成潜在威胁，严重的还会导致平台失效。如1980年3月27日，挪威“亚历山大·基兰”号钻井平台在9级海风作用下一根支柱发生断裂，平台15分钟后沉入海底，123人遇难。";
		ProjDoc projDoc1 = new ProjDoc("1", "文件名1", "项目名1", "参与人员1", allContent, researchOne, new Date());
		ProjDoc projDoc2 = new ProjDoc("2", "文件名2", "项目名2", "参与人员2", allContent, researchOne, new Date());
		List<ProjDoc> list = new ArrayList<ProjDoc>();
		list.add(projDoc1);
		list.add(projDoc2);
		
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		String resultString = clientUtil.addDocuments(list);

		return resultString;
    }

	//------------------------------------获取数据-------------------------------
	@RequestMapping("testCRUDGet1")
    public Object testCRUDGet1() throws Exception {
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		String resultString = clientUtil.getDocument("projdoc", "projdoc","1");

		return resultString;
    }
	
	@RequestMapping("testCRUDGet2")
    public Object testCRUDGet2() throws Exception {
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		ProjDoc resultString = clientUtil.getDocument("projdoc", "projdoc","1",ProjDoc.class);

		return resultString;
    }
	
	@RequestMapping("testCRUDGet3")
    public Object testCRUDGet3() throws Exception {
		String index = "projdoc";
		String dsl = "searchProById";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("condition","1");
		
		ClientInterface clientUtil = bbossESStarter.getConfigRestClient("esmapper/"+index+".xml");
		ESDatas<ProjDoc> esDatas = clientUtil.searchList(index + "/_search", dsl, params, ProjDoc.class);
		//获取结果对象列表，最多返回1000条记录
		List<ProjDoc> list = esDatas.getDatas();
		return list;
    }
	
	@RequestMapping("testCRUDGet4")
    public Object testCRUDGet4() throws Exception {
		String index = "projdoc";
		String dsl = "searchDatas";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("projectname1","项目名1");
		params.put("projectname2","项目名2");
		params.put("startTime",DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd HH:mm:ss.SSS"));
		params.put("endTime",DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
		
		ClientInterface clientUtil = bbossESStarter.getConfigRestClient("esmapper/"+index+".xml");
		ESDatas<ProjDoc> esDatas = clientUtil.searchList(index + "/_search", dsl, params, ProjDoc.class);
		//获取结果对象列表，最多返回1000条记录
		List<ProjDoc> list = esDatas.getDatas();
		return list;
    }

	@RequestMapping("testCRUDGetHigh1")
    public Object testCRUDGetHigh1() throws Exception {
		String index = "projdoc";
		String dsl = "proHighSearch";
		
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("coml","allContent");//与isall搭配true使用
        params.put("cont","在这些恶劣的环境载荷的长期作用下");
        params.put("isall",false);//require_field_match是否全部字段高亮，false以突出显示所有字段，true指定字段高亮
		
		ClientInterface clientUtil = bbossESStarter.getConfigRestClient("esmapper/"+index+".xml");
		ESDatas<ProjDoc> esDatas = clientUtil.searchList(index + "/_search", dsl, params, ProjDoc.class);
		//获取结果对象列表，最多返回1000条记录
		List<ProjDoc> list = esDatas.getDatas();
		list.stream().forEach(proDocu->{
        	Map<String,List<Object>> highLights = proDocu.getHighlight();
        	highLights.forEach( (k,v)->{
                v.stream().forEach(fieldCon->{
                	System.out.println("fieldName:"+k+",fieldCon:"+fieldCon);
                });
        	});
        });
		return list;
    }
	
	//------------------------------------删除数据-------------------------------
	
	@RequestMapping("testCRUDDelete1")
    public Object testCRUDDelete1() throws Exception {
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		String resultString = clientUtil.deleteDocument("projdoc", "projdoc", "1");
		System.out.println(resultString);
		return resultString;
    }

	@RequestMapping("testCRUDDelete2")
    public Object testCRUDDelete2() throws Exception {
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		String result = clientUtil.deleteDocuments("projdoc","projdoc", new String[]{"1","2"});
		System.out.println(result);
		return result;
    }
    
}
