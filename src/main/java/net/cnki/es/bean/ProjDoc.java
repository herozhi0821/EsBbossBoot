package net.cnki.es.bean;

import org.frameworkset.elasticsearch.entity.ESBaseData;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.ESId;
import com.frameworkset.orm.annotation.ESIndex;
import com.frameworkset.orm.annotation.ESMetaHighlight;

import java.util.Date;
import java.util.List;
import java.util.Map;

@ESIndex(name = "projdoc",type = "projdoc")
public class ProjDoc extends ESBaseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8050683009548474267L;
	@ESId
    private String id;
	private String filename;
    private String projectname;
    private String projectmember;
    private String allContent;
    private String researchOne;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Column(dataformat = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTime;
    @ESMetaHighlight //文档对应的高亮检索信息
    Map<String,List<Object>> highlights;
    
    
	public Map<String, List<Object>> getHighlights() {
		return highlights;
	}
	public void setHighlights(Map<String, List<Object>> highlights) {
		this.highlights = highlights;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getProjectmember() {
		return projectmember;
	}
	public void setProjectmember(String projectmember) {
		this.projectmember = projectmember;
	}
	public String getAllContent() {
		return allContent;
	}
	public void setAllContent(String allContent) {
		this.allContent = allContent;
	}
	public String getResearchOne() {
		return researchOne;
	}
	public void setResearchOne(String researchOne) {
		this.researchOne = researchOne;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public ProjDoc() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProjDoc(String id, String filename, String projectname, String projectmember, String allContent,
			String researchOne, Date createTime) {
		super();
		this.id = id;
		this.filename = filename;
		this.projectname = projectname;
		this.projectmember = projectmember;
		this.allContent = allContent;
		this.researchOne = researchOne;
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "ProjDoc [id=" + id + ", filename=" + filename + ", projectname=" + projectname + ", projectmember="
				+ projectmember + ", allContent=" + allContent + ", researchOne=" + researchOne + ", createTime="
				+ createTime + "]";
	}

   
}
