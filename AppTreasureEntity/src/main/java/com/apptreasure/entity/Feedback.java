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
    entityName = EntityNames.feedback, 
    keyFields = {"feedbackId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Feedback extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("feedbackId", String.valueOf(this.feedbackId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "feedbackId", fieldType =FieldTypeEnum.BIG_INT , description = " 意见反馈id")
	private long feedbackId;
	public long getFeedbackId() {
	return feedbackId;
	}
 	 public void setFeedbackId(long feedbackId){
	 this.feedbackId = feedbackId;
	}
	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.BIG_INT , description = "用户id")
	private long userId;
	public long getUserId() {
	return userId;
	}
 	 public void setUserId(long userId){
	 this.userId = userId;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "反馈时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "content", fieldType =FieldTypeEnum.CHAR128 , description = "反馈的内容")
	private String content;
	public String getContent() {
	return content;
	}
 	 public void setContent(String content){
	 this.content = content;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("feedbackId", String.valueOf(this.feedbackId));
	 entityMap.put("userId", String.valueOf(this.userId));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 entityMap.put("content", this.content);
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.feedbackId=Long.parseLong(entityMap.get("feedbackId"));
	 this.userId=Long.parseLong(entityMap.get("userId"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	 this.content=entityMap.get("content");

    }
}
