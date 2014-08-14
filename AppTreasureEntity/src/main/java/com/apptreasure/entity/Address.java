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
    entityName = EntityNames.address, 
    keyFields = {"addressId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Address extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("addressId", String.valueOf(this.addressId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "addressId", fieldType =FieldTypeEnum.INT , description = "")
	private int addressId;
	public int getAddressId() {
	return addressId;
	}
 	 public void setAddressId(int addressId){
	 this.addressId = addressId;
	}
	@FieldConfig(fieldName = "province", fieldType =FieldTypeEnum.CHAR36 , description = "省")
	private String province;
	public String getProvince() {
	return province;
	}
 	 public void setProvince(String province){
	 this.province = province;
	}
	@FieldConfig(fieldName = "city", fieldType =FieldTypeEnum.CHAR36 , description = "市")
	private String city;
	public String getCity() {
	return city;
	}
 	 public void setCity(String city){
	 this.city = city;
	}
	@FieldConfig(fieldName = "region", fieldType =FieldTypeEnum.CHAR36 , description = "区")
	private String region;
	public String getRegion() {
	return region;
	}
 	 public void setRegion(String region){
	 this.region = region;
	}
	@FieldConfig(fieldName = "street", fieldType =FieldTypeEnum.CHAR36 , description = "街道")
	private String street;
	public String getStreet() {
	return street;
	}
 	 public void setStreet(String street){
	 this.street = street;
	}
	@FieldConfig(fieldName = "post", fieldType =FieldTypeEnum.CHAR36 , description = "邮编")
	private String post;
	public String getPost() {
	return post;
	}
 	 public void setPost(String post){
	 this.post = post;
	}
	@FieldConfig(fieldName = "mobile", fieldType =FieldTypeEnum.CHAR8 , description = "手机")
	private String mobile;
	public String getMobile() {
	return mobile;
	}
 	 public void setMobile(String mobile){
	 this.mobile = mobile;
	}
	@FieldConfig(fieldName = "isDefault", fieldType =FieldTypeEnum.TYINT , description = "是否默认0-默认1-非默认")
	private byte isDefault;
	public byte getIsDefault() {
	return isDefault;
	}
 	 public void setIsDefault(byte isDefault){
	 this.isDefault = isDefault;
	}
	@FieldConfig(fieldName = "name", fieldType =FieldTypeEnum.CHAR36 , description = "收件人")
	private String name;
	public String getName() {
	return name;
	}
 	 public void setName(String name){
	 this.name = name;
	}
	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.CHAR36 , description = "用户id")
	private String userId;
	public String getUserId() {
	return userId;
	}
 	 public void setUserId(String userId){
	 this.userId = userId;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("addressId", String.valueOf(this.addressId));
	 entityMap.put("province", this.province);
	 entityMap.put("city", this.city);
	 entityMap.put("region", this.region);
	 entityMap.put("street", this.street);
	 entityMap.put("post", this.post);
	 entityMap.put("mobile", this.mobile);
	 entityMap.put("isDefault", String.valueOf(this.isDefault));
	 entityMap.put("name", this.name);
	 entityMap.put("userId", this.userId);
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.addressId=Integer.parseInt(entityMap.get("addressId"));
	 this.province=entityMap.get("province");
	 this.city=entityMap.get("city");
	 this.region=entityMap.get("region");
	 this.street=entityMap.get("street");
	 this.post=entityMap.get("post");
	 this.mobile=entityMap.get("mobile");
	 this.isDefault=Byte.parseByte(entityMap.get("isDefault"));
	 this.name=entityMap.get("name");
	 this.userId=entityMap.get("userId");

    }
}
