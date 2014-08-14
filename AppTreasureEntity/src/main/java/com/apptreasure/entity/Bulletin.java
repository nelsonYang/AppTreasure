package com.apptreasure.entity;

import com.framework.entity.pojo.Entity;
import com.framework.entity.annotation.EntityConfig;
import com.framework.entity.annotation.FieldConfig;
import com.framework.enumeration.FieldTypeEnum;
import com.framework.entity.pojo.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nelson
 */
@EntityConfig(
    entityName = EntityNames.bulletin, 
    keyFields = {"bulletinId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Bulletin extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("bulletinId", String.valueOf(this.bulletinId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "bulletinId", fieldType =FieldTypeEnum.BIG_INT , description = "公告id")
	private long bulletinId;
	public long getBulletinId() {
	return bulletinId;
	}
 	 public void setBulletinId(long bulletinId){
	 this.bulletinId = bulletinId;
	}
	@FieldConfig(fieldName = "title", fieldType =FieldTypeEnum.CHAR36 , description = "公告标题")
	private String title;
	public String getTitle() {
	return title;
	}
 	 public void setTitle(String title){
	 this.title = title;
	}
	@FieldConfig(fieldName = "content", fieldType =FieldTypeEnum.CHAR128 , description = "公告的内容")
	private String content;
	public String getContent() {
	return content;
	}
 	 public void setContent(String content){
	 this.content = content;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "创建的时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("bulletinId", String.valueOf(this.bulletinId));
	 entityMap.put("title", this.title);
	 entityMap.put("content", this.content);
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.bulletinId=Long.parseLong(entityMap.get("bulletinId"));
	 this.title=entityMap.get("title");
	 this.content=entityMap.get("content");
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}

    }
}
