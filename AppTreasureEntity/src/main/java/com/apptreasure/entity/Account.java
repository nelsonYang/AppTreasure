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
    entityName = EntityNames.account, 
    keyFields = {"accountId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Account extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("accountId", String.valueOf(this.accountId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "accountId", fieldType =FieldTypeEnum.BIG_INT , description = "帐号id")
	private long accountId;
	public long getAccountId() {
	return accountId;
	}
 	 public void setAccountId(long accountId){
	 this.accountId = accountId;
	}
	@FieldConfig(fieldName = "accountNO", fieldType =FieldTypeEnum.CHAR36 , description = "帐号")
	private String accountNO;
	public String getAccountNO() {
	return accountNO;
	}
 	 public void setAccountNO(String accountNO){
	 this.accountNO = accountNO;
	}
	@FieldConfig(fieldName = "accountName", fieldType =FieldTypeEnum.CHAR36 , description = "帐号名")
	private String accountName;
	public String getAccountName() {
	return accountName;
	}
 	 public void setAccountName(String accountName){
	 this.accountName = accountName;
	}
	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.BIG_INT , description = "用户id")
	private long userId;
	public long getUserId() {
	return userId;
	}
 	 public void setUserId(long userId){
	 this.userId = userId;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "创建时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "balance", fieldType =FieldTypeEnum.BIG_INT , description = "余额")
	private long balance;
	public long getBalance() {
	return balance;
	}
 	 public void setBalance(long balance){
	 this.balance = balance;
	}
         
                @FieldConfig(fieldName = "recommandBalance", fieldType =FieldTypeEnum.DOUBLE , description = "余额")
	private double recommandBalance;
	public double getRecommandBalance() {
	return recommandBalance;
	}
 	 public void setRecommandBalance(double recommandBalance){
	 this.recommandBalance = recommandBalance;
	}
	@FieldConfig(fieldName = "type", fieldType =FieldTypeEnum.TYINT , description = "帐号类型")
	private byte type;
	public byte getType() {
	return type;
	}
 	 public void setType(byte type){
	 this.type = type;
	}
	@FieldConfig(fieldName = "status", fieldType =FieldTypeEnum.TYINT , description = "帐号的状态1-有效，状态")
	private byte status;
	public byte getStatus() {
	return status;
	}
 	 public void setStatus(byte status){
	 this.status = status;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("accountId", String.valueOf(this.accountId));
	 entityMap.put("accountNO", this.accountNO);
	 entityMap.put("accountName", this.accountName);
	 entityMap.put("userId", String.valueOf(this.userId));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 entityMap.put("balance", String.valueOf(this.balance));
                 entityMap.put("recommandBalance", String.valueOf(this.recommandBalance));
	 entityMap.put("type", String.valueOf(this.type));
	 entityMap.put("status", String.valueOf(this.status));
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.accountId=Long.parseLong(entityMap.get("accountId"));
	 this.accountNO=entityMap.get("accountNO");
	 this.accountName=entityMap.get("accountName");
	 this.userId=Long.parseLong(entityMap.get("userId"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	 this.balance=Long.parseLong(entityMap.get("balance"));
                 this.recommandBalance = Double.parseDouble(entityMap.get("recommandBalance"));
	 this.type=Byte.parseByte(entityMap.get("type"));
	 this.status=Byte.parseByte(entityMap.get("status"));

    }
}
