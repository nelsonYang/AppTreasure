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
    entityName = EntityNames.event, 
    keyFields = {"eventId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Event extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("eventId", String.valueOf(this.eventId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "eventId", fieldType =FieldTypeEnum.BIG_INT , description = "活动id")
	private long eventId;
	public long getEventId() {
	return eventId;
	}
 	 public void setEventId(long eventId){
	 this.eventId = eventId;
	}
	@FieldConfig(fieldName = "eventName", fieldType =FieldTypeEnum.CHAR36 , description = "活动名称")
	private String eventName;
	public String getEventName() {
	return eventName;
	}
 	 public void setEventName(String eventName){
	 this.eventName = eventName;
	}
	@FieldConfig(fieldName = "status", fieldType =FieldTypeEnum.BIG_INT , description = "活动状态1-启用2-暂停")
	private long status;
	public long getStatus() {
	return status;
	}
 	 public void setStatus(long status){
	 this.status = status;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "创建时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("eventId", String.valueOf(this.eventId));
	 entityMap.put("eventName", this.eventName);
	 entityMap.put("status", String.valueOf(this.status));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.eventId=Long.parseLong(entityMap.get("eventId"));
	 this.eventName=entityMap.get("eventName");
	 this.status=Long.parseLong(entityMap.get("status"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}

    }
}
