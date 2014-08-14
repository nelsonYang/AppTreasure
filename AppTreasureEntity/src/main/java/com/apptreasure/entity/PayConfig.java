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
    entityName = EntityNames.payConfig, 
    keyFields = {"payConfigId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class PayConfig extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("payConfigId", String.valueOf(this.payConfigId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "payConfigId", fieldType =FieldTypeEnum.BIG_INT , description = "支付方式的配置")
	private long payConfigId;
	public long getPayConfigId() {
	return payConfigId;
	}
 	 public void setPayConfigId(long payConfigId){
	 this.payConfigId = payConfigId;
	}
	@FieldConfig(fieldName = "payName", fieldType =FieldTypeEnum.CHAR36 , description = "支付的方式")
	private String payName;
	public String getPayName() {
	return payName;
	}
 	 public void setPayName(String payName){
	 this.payName = payName;
	}
	@FieldConfig(fieldName = "payCode", fieldType =FieldTypeEnum.TYINT , description = "支付的编码")
	private byte payCode;
	public byte getPayCode() {
	return payCode;
	}
 	 public void setPayCode(byte payCode){
	 this.payCode = payCode;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("payConfigId", String.valueOf(this.payConfigId));
	 entityMap.put("payName", this.payName);
	 entityMap.put("payCode", String.valueOf(this.payCode));
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.payConfigId=Long.parseLong(entityMap.get("payConfigId"));
	 this.payName=entityMap.get("payName");
	 this.payCode=Byte.parseByte(entityMap.get("payCode"));

    }
}
