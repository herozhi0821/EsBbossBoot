package net.cnki.es.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.elasticsearch.ElasticSearchException;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.cnki.es.bean.ProjDoc;

@Service
public class EsUtilService {
	
	@Autowired
    BBossESStarter bbossESStarter;
	
	
	
	/**
	 * 删除索引
	 * @param indiceName 索引名称
	 */
	public void dropIndice(String indiceName){
        ClientInterface clientUtil = bbossESStarter.getRestClient();
        try {
            boolean exist = clientUtil.existIndice(indiceName);
            if(exist) {
                String r = clientUtil.dropIndice(indiceName);
                System.out.println("删除索引定义:"+r);
            }
        } catch (ElasticSearchException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 重新创建索引，若已存在则删除后新建索引
	 * @param indiceName 索引名
	 * @return 返回索引定义
	 */
	public String dropAndCreateAndGetIndice(String indiceName){
        ClientInterface clientUtil = bbossESStarter.getConfigRestClient("esmapper/"+indiceName+".xml");
        try {
            boolean exist = clientUtil.existIndice(indiceName);
            if(exist) {
                String r = clientUtil.dropIndice(indiceName);
                System.out.println("删除索引定义:"+r);
            }
            clientUtil.createIndiceMapping(indiceName,"createProInfo");
            String demoIndice = clientUtil.getIndice(indiceName);
            return demoIndice;
        } catch (ElasticSearchException e) {
            e.printStackTrace();
        }
		return null;

    }
	
	 /**
	 * 添加或修改文档
	 * @param index：索引名称
	 * @param indexType：索引类型名称（文档）
	 * @param list：集合
	  * */
	 public <T> void addOrUpdateDocuments(String index,String indexType,List<T> list) {
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		clientUtil.addDocuments(index,indexType,list);
	 }
	
	 
	 /**
	  * 添加或修改单个文档
	  * @param index
	  * @param indexType
	  * @param bean
	  * @return
	  */
	 public String addOrUpdateDocument(String index,String indexType,Object bean) {
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		//索引表 索引类型
		String result = clientUtil.addDocument(index,indexType,bean);
		return result;
	 }
	 /**
	  * 创建或者更新索引文档(实体类@ESIndex注解后才可使用)
	  * indexName,indexType索引类型和type必须通过bean对象的ESIndex来指定，否则抛出异常
	  * @param bean
	  * @return
	  */
	 public String addOrUpdateDocument(Object bean) {
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		String result = clientUtil.addDocument(bean);
		return result;
	 }
	 /**
	 * 获取文档
	 */
	public String getDocumentById(String index,String type,String id){
		//获取文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		//索引表 索引类型 id
		String res = clientUtil.getDocument(index,type,id);
		return res;
	}
	/**
	 * 获取文档
	 */
	public <T> T getDocumentById(String index,String type,String id,Class<T> t){
		//获取文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		//索引表 索引类型 id 对象
		return clientUtil.getDocument(index,type,id,t);
	}
	
	/**
	 * 根据id删除
	 * @param index：索引名称
	 * @param indexType：索引类型名称（文档）
	 * @param id：id
	 */
	 public void deleteDocumentById(String index,String indexType ,String id) {
		//获取文档的客户端对象，单实例多线程安全
		 ClientInterface clientUtil = bbossESStarter.getRestClient();
		//索引表 索引类型 id 对象
		 clientUtil.deleteDocument(index, indexType, id); 
	 }
	 
	/**
	 * 删除文档
	 */
	public void deleteDocumentByIds(String indexName,String indexType,String[] ids){
		//获取文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = bbossESStarter.getRestClient();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < ids.length; ++i) {
			String id = ids[i];
			builder.append("{ \"delete\" : { \"_index\" : \"").append(indexName).append("\", \"_type\" : \"").append(indexType).append("\", \"_id\" : \"").append(id).append("\" } }\n");
		}
		//索引表 索引类型 id 对象
		clientUtil.executeHttp("_bulk",builder.toString(),"post");
	}
		
	 /**
	 * 通过mapper文件执行查询语句
	 * @param index 索引
	 * @param params 传递的参数
	 * @param dsl 对应mapper文件的name
	 */
	public <T> List<T> exec(String index,T params,String dsl){
		//获取索引对应的客户端
		ClientInterface clientUtil = bbossESStarter.getRestClient(index);
		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//执行查询，index为索引表，_search为检索操作action
		//使用反射获取Class<T>
		Class<T> clazz = GenericsUtils.getSuperClassGenricType(params.getClass());
		ESDatas<T> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList(index+"/_search",//demo为索引表，_search为检索操作action
						dsl,//esmapper/xxx.xml中定义的dsl语句的name
						params,//变量参数
						clazz);//返回的文档封装对象类型
		//获取结果对象列表，最多返回1000条记录
		List<T> list = esDatas.getDatas();
		return list;
	}
	
	/**
	 * 根据ID进行获取(不如getDocumentById简单)
	 * @param <T>
	 * @param index 索引
	 * @param param 实体类
	 * @param id 数据ID
	 * @param dsl mapping中的方法名
	 * @return
	 */
	public <T> List<T> searchById(String index,T param,String id,String dsl){
		//获取索引对应的客户端
		ClientInterface clientUtil = bbossESStarter.getRestClient(index);
		
		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		Map<String,Object> paras = new HashMap<String,Object>();
		paras.put("condition",id);

		//使用反射获取Class<T>
		Class<T> clazz = GenericsUtils.getSuperClassGenricType(param.getClass());
		
		//执行查询，index为索引表，_search为检索操作action
		ESDatas<T> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList(index+"/_search",//demo为索引表，_search为检索操作action
						dsl,//esmapper/xxx.xml中定义的dsl语句的name
						paras,//变量参数
						clazz);//返回的文档封装对象类型
		List<T> list = esDatas.getDatas();
		return list;
	}
	
	/**
	 * 字段高亮检索
	 * @param index 索引
	 * @param field 指定字段，与isall搭配true使用
	 * @param content 查询内容
	 * @param isall 为false时全部字段查询高亮
	 * @return
	 * @throws ParseException
	 */
	public List<ProjDoc> proHightLightSearch(String index,String field, String content,boolean isall) throws ParseException {
        ClientInterface clientUtil = bbossESStarter.getRestClient(index);;
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("coml",field);
        params.put("cont",content);
        params.put("isall",isall);
        
        ESDatas<ProjDoc> esDatas =  
                clientUtil.searchList(index+"/_search",//demo为索引表，_search为检索操作action
                		"proHighSearch",//esmapper/demo.xml中定义的dsl语句
                        params,//变量参数
                        ProjDoc.class);//返回的文档封装对象类型
        List<ProjDoc> pros = esDatas.getDatas();
        pros.stream().forEach(proDocu->{//lamada
        	Map<String,List<Object>> highLights = proDocu.getHighlight();//多字段
        	highLights.forEach( (k,v)->{
                v.stream().forEach(fieldCon->{
//                	System.out.println("fieldName:"+k+",fieldCon:"+fieldCon);
                });
        	});
        });
		return pros;
    }

	/**
	 * 字段高亮检索
	 * @param <T>
	 * @param index 索引
	 * @param field 指定字段，与isall搭配true使用
	 * @param content 查询内容
	 * @param isall 为false时全部字段查询高亮
	 * @param t
	 * @return
	 * @throws ParseException
	 */
	public <T> List<T> hightLightSearch(String index,String field, String content,boolean isall,Class<T> t) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("coml",field);
        params.put("cont",content);
        params.put("isall",isall);
        
        ClientInterface clientUtil = bbossESStarter.getConfigRestClient("esmapper/"+index+".xml");
        ESDatas<T> esDatas = clientUtil.searchList(index+"/_search", "proHighSearch", params, t);
        List<T> pros = esDatas.getDatas();
		return pros;
    }
	
	/**
	 * 字段高亮检索
	 * @param <T>
	 * @param index 索引
	 * @param field 指定字段，与isall搭配true使用
	 * @param content 查询内容
	 * @param startTime
	 * @param endTime
	 * @param isall 为false时全部字段查询高亮
	 * @param t
	 * @return
	 */
	public <T> List<T> testHighlightSearch(String index,String field, String content,String startTime,String endTime,boolean isall,Class<T> t) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("coml",field);//检索字段
        params.put("cont",content);//检索内容
        params.put("startTime",startTime);//统计开始时间
        params.put("endTime",endTime);//统计截止时间
        params.put("isall",isall);//是否全部高亮
        
        ClientInterface clientUtil = bbossESStarter.getConfigRestClient("esmapper/"+index+".xml");
        ESDatas<T> esDatas = clientUtil.searchList(index+"/_search", "testHighlightSearch", params, t);
        List<T> pros = esDatas.getDatas();
		return pros;
    }
}
