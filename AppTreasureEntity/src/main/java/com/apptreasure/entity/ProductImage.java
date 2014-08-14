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
    entityName = EntityNames.productImage, 
    keyFields = {"productImage"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class ProductImage extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("productImage", String.valueOf(this.productImage));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "productImage", fieldType =FieldTypeEnum.BIG_INT , description = "商品图id")
	private long productImage;
	public long getProductImage() {
	return productImage;
	}
 	 public void setProductImage(long productImage){
	 this.productImage = productImage;
	}
	@FieldConfig(fieldName = "productImageName", fieldType =FieldTypeEnum.CHAR36 , description = "图片名称")
	private String productImageName;
	public String getProductImageName() {
	return productImageName;
	}
 	 public void setProductImageName(String productImageName){
	 this.productImageName = productImageName;
	}
	@FieldConfig(fieldName = "productImageURL", fieldType =FieldTypeEnum.CHAR64 , description = "图片url")
	private String productImageURL;
	public String getProductImageURL() {
	return productImageURL;
	}
 	 public void setProductImageURL(String productImageURL){
	 this.productImageURL = productImageURL;
	}
	@FieldConfig(fieldName = "type", fieldType =FieldTypeEnum.TYINT , description = "图片类型1-大图2-小图")
	private byte type;
	public byte getType() {
	return type;
	}
 	 public void setType(byte type){
	 this.type = type;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "创建时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "productId", fieldType =FieldTypeEnum.BIG_INT , description = "商品id")
	private long productId;
	public long getProductId() {
	return productId;
	}
 	 public void setProductId(long productId){
	 this.productId = productId;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("productImage", String.valueOf(this.productImage));
	 entityMap.put("productImageName", this.productImageName);
	 entityMap.put("productImageURL", this.productImageURL);
	 entityMap.put("type", String.valueOf(this.type));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 entityMap.put("productId", String.valueOf(this.productId));
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.productImage=Long.parseLong(entityMap.get("productImage"));
	 this.productImageName=entityMap.get("productImageName");
	 this.productImageURL=entityMap.get("productImageURL");
	 this.type=Byte.parseByte(entityMap.get("type"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	 this.productId=Long.parseLong(entityMap.get("productId"));

    }
}
