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
    entityName = EntityNames.rewardFlow, 
    keyFields = {"rewardFlowId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class RewardFlow extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("rewardFlowId", String.valueOf(this.rewardFlowId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "rewardFlowId", fieldType =FieldTypeEnum.BIG_INT , description = "流水id")
	private long rewardFlowId;
	public long getRewardFlowId() {
	return rewardFlowId;
	}
 	 public void setRewardFlowId(long rewardFlowId){
	 this.rewardFlowId = rewardFlowId;
	}
	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.BIG_INT , description = "用户id")
	private long userId;
	public long getUserId() {
	return userId;
	}
 	 public void setUserId(long userId){
	 this.userId = userId;
	}
	@FieldConfig(fieldName = "rewardItem", fieldType =FieldTypeEnum.TYINT , description = "酬金项目")
	private byte rewardItem;
	public byte getRewardItem() {
	return rewardItem;
	}
 	 public void setRewardItem(byte rewardItem){
	 this.rewardItem = rewardItem;
	}
	@FieldConfig(fieldName = "rewardAmount", fieldType =FieldTypeEnum.BIG_INT , description = "酬金")
	private long rewardAmount;
	public long getRewardAmount() {
	return rewardAmount;
	}
 	 public void setRewardAmount(long rewardAmount){
	 this.rewardAmount = rewardAmount;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "创建时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "channel", fieldType =FieldTypeEnum.TYINT , description = "来自的渠道")
	private byte channel;
	public byte getChannel() {
	return channel;
	}
 	 public void setChannel(byte channel){
	 this.channel = channel;
	}
	@FieldConfig(fieldName = "ip", fieldType =FieldTypeEnum.CHAR36 , description = "访问的ip")
	private String ip;
	public String getIp() {
	return ip;
	}
 	 public void setIp(String ip){
	 this.ip = ip;
	}
	@FieldConfig(fieldName = "appName", fieldType =FieldTypeEnum.CHAR36 , description = "下载app的名字")
	private String appName;
	public String getAppName() {
	return appName;
	}
 	 public void setAppName(String appName){
	 this.appName = appName;
	}
	@FieldConfig(fieldName = "realAmount", fieldType =FieldTypeEnum.BIG_INT , description = "")
	private long realAmount;
	public long getRealAmount() {
	return realAmount;
	}
 	 public void setRealAmount(long realAmount){
	 this.realAmount = realAmount;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("rewardFlowId", String.valueOf(this.rewardFlowId));
	 entityMap.put("userId", String.valueOf(this.userId));
	 entityMap.put("rewardItem", String.valueOf(this.rewardItem));
	 entityMap.put("rewardAmount", String.valueOf(this.rewardAmount));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 entityMap.put("channel", String.valueOf(this.channel));
	 entityMap.put("ip", this.ip);
	 entityMap.put("appName", this.appName);
	 entityMap.put("realAmount", String.valueOf(this.realAmount));
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.rewardFlowId=Long.parseLong(entityMap.get("rewardFlowId"));
	 this.userId=Long.parseLong(entityMap.get("userId"));
	 this.rewardItem=Byte.parseByte(entityMap.get("rewardItem"));
	 this.rewardAmount=Long.parseLong(entityMap.get("rewardAmount"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	 this.channel=Byte.parseByte(entityMap.get("channel"));
	 this.ip=entityMap.get("ip");
	 this.appName=entityMap.get("appName");
	 this.realAmount=Long.parseLong(entityMap.get("realAmount"));

    }
}
