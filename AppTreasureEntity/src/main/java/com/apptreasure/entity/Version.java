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
    entityName = EntityNames.version, 
    keyFields = {"versionId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Version extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("versionId", String.valueOf(this.versionId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "versionId", fieldType =FieldTypeEnum.BIG_INT , description = "版本id")
	private long versionId;
	public long getVersionId() {
	return versionId;
	}
 	 public void setVersionId(long versionId){
	 this.versionId = versionId;
	}
	@FieldConfig(fieldName = "versionName", fieldType =FieldTypeEnum.CHAR36 , description = "版本名称")
	private String versionName;
	public String getVersionName() {
	return versionName;
	}
 	 public void setVersionName(String versionName){
	 this.versionName = versionName;
	}
	@FieldConfig(fieldName = "versionCode", fieldType =FieldTypeEnum.CHAR36 , description = "版本号")
	private String versionCode;
	public String getVersionCode() {
	return versionCode;
	}
 	 public void setVersionCode(String versionCode){
	 this.versionCode = versionCode;
	}
	@FieldConfig(fieldName = "versionUpdateTime", fieldType =FieldTypeEnum.DATETIME , description = "版本更新的时间")
	private Date versionUpdateTime;
	public Date getVersionUpdateTime() {
	return versionUpdateTime;
	}
 	 public void setVersionUpdateTime(Date versionUpdateTime){
	 this.versionUpdateTime = versionUpdateTime;
	}
	@FieldConfig(fieldName = "versionContent", fieldType =FieldTypeEnum.CHAR128 , description = "版本更新的内容")
	private String versionContent;
	public String getVersionContent() {
	return versionContent;
	}
 	 public void setVersionContent(String versionContent){
	 this.versionContent = versionContent;
	}
	@FieldConfig(fieldName = "size", fieldType =FieldTypeEnum.BIG_INT , description = "包的大小")
	private long size;
	public long getSize() {
	return size;
	}
 	 public void setSize(long size){
	 this.size = size;
	}
	@FieldConfig(fieldName = "forceUpdate", fieldType =FieldTypeEnum.TYINT , description = "是否强制更新")
	private byte forceUpdate;
	public byte getForceUpdate() {
	return forceUpdate;
	}
 	 public void setForceUpdate(byte forceUpdate){
	 this.forceUpdate = forceUpdate;
	}
	@FieldConfig(fieldName = "downloadUrl", fieldType =FieldTypeEnum.CHAR128 , description = "软件的下载地址")
	private String downloadUrl;
	public String getDownloadUrl() {
	return downloadUrl;
	}
 	 public void setDownloadUrl(String downloadUrl){
	 this.downloadUrl = downloadUrl;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("versionId", String.valueOf(this.versionId));
	 entityMap.put("versionName", this.versionName);
	 entityMap.put("versionCode", this.versionCode);
	 try{
	 entityMap.put("versionUpdateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionUpdateTime));
	}catch(Exception ex){}
	 entityMap.put("versionContent", this.versionContent);
	 entityMap.put("size", String.valueOf(this.size));
	 entityMap.put("forceUpdate", String.valueOf(this.forceUpdate));
	 entityMap.put("downloadUrl", this.downloadUrl);
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.versionId=Long.parseLong(entityMap.get("versionId"));
	 this.versionName=entityMap.get("versionName");
	 this.versionCode=entityMap.get("versionCode");
	try{
	 this.versionUpdateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("versionUpdateTime"));
	}catch(Exception ex){}
	 this.versionContent=entityMap.get("versionContent");
	 this.size=Long.parseLong(entityMap.get("size"));
	 this.forceUpdate=Byte.parseByte(entityMap.get("forceUpdate"));
	 this.downloadUrl=entityMap.get("downloadUrl");

    }
}
