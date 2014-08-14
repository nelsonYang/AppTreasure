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
    entityName = EntityNames.eventQualification, 
    keyFields = {"eventQualificationId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class EventQualification extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("eventQualificationId", String.valueOf(this.eventQualificationId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "eventQualificationId", fieldType =FieldTypeEnum.BIG_INT , description = "活动资格id")
	private long eventQualificationId;
	public long getEventQualificationId() {
	return eventQualificationId;
	}
 	 public void setEventQualificationId(long eventQualificationId){
	 this.eventQualificationId = eventQualificationId;
	}
	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.BIG_INT , description = "用户id")
	private long userId;
	public long getUserId() {
	return userId;
	}
 	 public void setUserId(long userId){
	 this.userId = userId;
	}
	@FieldConfig(fieldName = "productId", fieldType =FieldTypeEnum.BIG_INT , description = "产品id")
	private long productId;
	public long getProductId() {
	return productId;
	}
 	 public void setProductId(long productId){
	 this.productId = productId;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "创建时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "expireTime", fieldType =FieldTypeEnum.DATETIME , description = "过期时间")
	private Date expireTime;
	public Date getExpireTime() {
	return expireTime;
	}
 	 public void setExpireTime(Date expireTime){
	 this.expireTime = expireTime;
	}
	@FieldConfig(fieldName = "status", fieldType =FieldTypeEnum.TYINT , description = "状态1-正常 2-删除")
	private byte status;
	public byte getStatus() {
	return status;
	}
 	 public void setStatus(byte status){
	 this.status = status;
	}
	@FieldConfig(fieldName = "isExchange", fieldType =FieldTypeEnum.TYINT , description = "是否兑换1-兑换 2-未兑换")
	private byte isExchange;
	public byte getIsExchange() {
	return isExchange;
	}
 	 public void setIsExchange(byte isExchange){
	 this.isExchange = isExchange;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("eventQualificationId", String.valueOf(this.eventQualificationId));
	 entityMap.put("userId", String.valueOf(this.userId));
	 entityMap.put("productId", String.valueOf(this.productId));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 try{
	 entityMap.put("expireTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expireTime));
	}catch(Exception ex){}
	 entityMap.put("status", String.valueOf(this.status));
	 entityMap.put("isExchange", String.valueOf(this.isExchange));
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.eventQualificationId=Long.parseLong(entityMap.get("eventQualificationId"));
	 this.userId=Long.parseLong(entityMap.get("userId"));
	 this.productId=Long.parseLong(entityMap.get("productId"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	try{
	 this.expireTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("expireTime"));
	}catch(Exception ex){}
	 this.status=Byte.parseByte(entityMap.get("status"));
	 this.isExchange=Byte.parseByte(entityMap.get("isExchange"));

    }
}
