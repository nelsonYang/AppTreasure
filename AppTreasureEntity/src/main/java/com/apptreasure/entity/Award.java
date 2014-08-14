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
    entityName = EntityNames.award, 
    keyFields = {"awardId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Award extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("awardId", String.valueOf(this.awardId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "awardId", fieldType =FieldTypeEnum.BIG_INT , description = "奖励id")
	private long awardId;
	public long getAwardId() {
	return awardId;
	}
 	 public void setAwardId(long awardId){
	 this.awardId = awardId;
	}
	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.BIG_INT , description = "奖励的用户id")
	private long userId;
	public long getUserId() {
	return userId;
	}
 	 public void setUserId(long userId){
	 this.userId = userId;
	}
	@FieldConfig(fieldName = "payAccount", fieldType =FieldTypeEnum.CHAR36 , description = "奖励的支付宝帐号")
	private String payAccount;
	public String getPayAccount() {
	return payAccount;
	}
 	 public void setPayAccount(String payAccount){
	 this.payAccount = payAccount;
	}
	@FieldConfig(fieldName = "amount", fieldType =FieldTypeEnum.BIG_INT , description = "奖励的数量")
	private long amount;
	public long getAmount() {
	return amount;
	}
 	 public void setAmount(long amount){
	 this.amount = amount;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "奖励的时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "processStatus", fieldType =FieldTypeEnum.TYINT , description = "1-已处理 2-未处理")
	private byte processStatus;
	public byte getProcessStatus() {
	return processStatus;
	}
 	 public void setProcessStatus(byte processStatus){
	 this.processStatus = processStatus;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("awardId", String.valueOf(this.awardId));
	 entityMap.put("userId", String.valueOf(this.userId));
	 entityMap.put("payAccount", this.payAccount);
	 entityMap.put("amount", String.valueOf(this.amount));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 entityMap.put("processStatus", String.valueOf(this.processStatus));
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.awardId=Long.parseLong(entityMap.get("awardId"));
	 this.userId=Long.parseLong(entityMap.get("userId"));
	 this.payAccount=entityMap.get("payAccount");
	 this.amount=Long.parseLong(entityMap.get("amount"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	 this.processStatus=Byte.parseByte(entityMap.get("processStatus"));

    }
}
