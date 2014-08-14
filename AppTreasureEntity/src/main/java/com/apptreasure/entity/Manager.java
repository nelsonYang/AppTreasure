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
    entityName = EntityNames.manager, 
    keyFields = {"managerId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Manager extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("managerId", String.valueOf(this.managerId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "managerId", fieldType =FieldTypeEnum.BIG_INT , description = "管理员id")
	private long managerId;
	public long getManagerId() {
	return managerId;
	}
 	 public void setManagerId(long managerId){
	 this.managerId = managerId;
	}
	@FieldConfig(fieldName = "userName", fieldType =FieldTypeEnum.CHAR36 , description = "管理员帐号")
	private String userName;
	public String getUserName() {
	return userName;
	}
 	 public void setUserName(String userName){
	 this.userName = userName;
	}
	@FieldConfig(fieldName = "password", fieldType =FieldTypeEnum.CHAR36 , description = "密码")
	private String password;
	public String getPassword() {
	return password;
	}
 	 public void setPassword(String password){
	 this.password = password;
	}
	@FieldConfig(fieldName = "status", fieldType =FieldTypeEnum.TYINT , description = "状态1-正常 2-暂停")
	private byte status;
	public byte getStatus() {
	return status;
	}
 	 public void setStatus(byte status){
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
	 entityMap.put("managerId", String.valueOf(this.managerId));
	 entityMap.put("userName", this.userName);
	 entityMap.put("password", this.password);
	 entityMap.put("status", String.valueOf(this.status));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.managerId=Long.parseLong(entityMap.get("managerId"));
	 this.userName=entityMap.get("userName");
	 this.password=entityMap.get("password");
	 this.status=Byte.parseByte(entityMap.get("status"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}

    }
}
