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
    entityName = EntityNames.drawAccountFlow, 
    keyFields = {"drawAccountFlowId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class DrawAccountFlow extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("drawAccountFlowId", String.valueOf(this.drawAccountFlowId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "drawAccountFlowId", fieldType =FieldTypeEnum.BIG_INT , description = "兑现记录id")
	private long drawAccountFlowId;
	public long getDrawAccountFlowId() {
	return drawAccountFlowId;
	}
 	 public void setDrawAccountFlowId(long drawAccountFlowId){
	 this.drawAccountFlowId = drawAccountFlowId;
	}
	@FieldConfig(fieldName = "drawWay", fieldType =FieldTypeEnum.TYINT , description = "兑现的方式1-支付宝 2-联通手机")
	private byte drawWay;
	public byte getDrawWay() {
	return drawWay;
	}
 	 public void setDrawWay(byte drawWay){
	 this.drawWay = drawWay;
	}
	@FieldConfig(fieldName = "drawAmount", fieldType =FieldTypeEnum.BIG_INT , description = "兑现的金额")
	private long drawAmount;
	public long getDrawAmount() {
	return drawAmount;
	}
 	 public void setDrawAmount(long drawAmount){
	 this.drawAmount = drawAmount;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "兑现的时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "processStatus", fieldType =FieldTypeEnum.TYINT , description = "处理状态1-处理 2-未处理")
	private byte processStatus;
	public byte getProcessStatus() {
	return processStatus;
	}
 	 public void setProcessStatus(byte processStatus){
	 this.processStatus = processStatus;
	}
	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.BIG_INT , description = "用户id")
	private long userId;
	public long getUserId() {
	return userId;
	}
 	 public void setUserId(long userId){
	 this.userId = userId;
	}
	@FieldConfig(fieldName = "payAccount", fieldType =FieldTypeEnum.CHAR36 , description = "支付的帐号")
	private String payAccount;
	public String getPayAccount() {
	return payAccount;
	}
 	 public void setPayAccount(String payAccount){
	 this.payAccount = payAccount;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("drawAccountFlowId", String.valueOf(this.drawAccountFlowId));
	 entityMap.put("drawWay", String.valueOf(this.drawWay));
	 entityMap.put("drawAmount", String.valueOf(this.drawAmount));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 entityMap.put("processStatus", String.valueOf(this.processStatus));
	 entityMap.put("userId", String.valueOf(this.userId));
	 entityMap.put("payAccount", this.payAccount);
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.drawAccountFlowId=Long.parseLong(entityMap.get("drawAccountFlowId"));
	 this.drawWay=Byte.parseByte(entityMap.get("drawWay"));
	 this.drawAmount=Long.parseLong(entityMap.get("drawAmount"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	 this.processStatus=Byte.parseByte(entityMap.get("processStatus"));
	 this.userId=Long.parseLong(entityMap.get("userId"));
	 this.payAccount=entityMap.get("payAccount");

    }
}
