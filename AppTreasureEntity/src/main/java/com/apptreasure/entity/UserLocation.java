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
    entityName = EntityNames.userLocation, 
    keyFields = {"userLocationId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class UserLocation extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("userLocationId", String.valueOf(this.userLocationId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "userLocationId", fieldType =FieldTypeEnum.BIG_INT , description = "用户地理位置")
	private long userLocationId;
	public long getUserLocationId() {
	return userLocationId;
	}
 	 public void setUserLocationId(long userLocationId){
	 this.userLocationId = userLocationId;
	}
	@FieldConfig(fieldName = "latitution", fieldType =FieldTypeEnum.DOUBLE , description = "维度")
	private double latitution;
	public double getLatitution() {
	return latitution;
	}
 	 public void setLatitution(double latitution){
	 this.latitution = latitution;
	}
	@FieldConfig(fieldName = "lagtion", fieldType =FieldTypeEnum.DOUBLE , description = "精度")
	private double lagtion;
	public double getLagtion() {
	return lagtion;
	}
 	 public void setLagtion(double lagtion){
	 this.lagtion = lagtion;
	}
	@FieldConfig(fieldName = "lastUpdateTime", fieldType =FieldTypeEnum.DATETIME , description = "最近更新时间")
	private Date lastUpdateTime;
	public Date getLastUpdateTime() {
	return lastUpdateTime;
	}
 	 public void setLastUpdateTime(Date lastUpdateTime){
	 this.lastUpdateTime = lastUpdateTime;
	}
	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.BIG_INT , description = "用户id")
	private long userId;
	public long getUserId() {
	return userId;
	}
 	 public void setUserId(long userId){
	 this.userId = userId;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("userLocationId", String.valueOf(this.userLocationId));
	 entityMap.put("latitution", String.valueOf(this.latitution));
	 entityMap.put("lagtion", String.valueOf(this.lagtion));
	 try{
	 entityMap.put("lastUpdateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUpdateTime));
	}catch(Exception ex){}
	 entityMap.put("userId", String.valueOf(this.userId));
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.userLocationId=Long.parseLong(entityMap.get("userLocationId"));
	 this.latitution=Double.parseDouble(entityMap.get("latitution"));
	 this.lagtion=Double.parseDouble(entityMap.get("lagtion"));
	try{
	 this.lastUpdateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("lastUpdateTime"));
	}catch(Exception ex){}
	 this.userId=Long.parseLong(entityMap.get("userId"));

    }
}
