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
    entityName = EntityNames.product, 
    keyFields = {"productId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class Product extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("productId", String.valueOf(this.productId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "productId", fieldType =FieldTypeEnum.BIG_INT , description = "商品")
	private long productId;
	public long getProductId() {
	return productId;
	}
 	 public void setProductId(long productId){
	 this.productId = productId;
	}
	@FieldConfig(fieldName = "productName", fieldType =FieldTypeEnum.CHAR36 , description = "商品名称")
	private String productName;
	public String getProductName() {
	return productName;
	}
 	 public void setProductName(String productName){
	 this.productName = productName;
	}
	@FieldConfig(fieldName = "productNum", fieldType =FieldTypeEnum.INT , description = "商品数量")
	private int productNum;
	public int getProductNum() {
	return productNum;
	}
 	 public void setProductNum(int productNum){
	 this.productNum = productNum;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "创建时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "productDesc", fieldType =FieldTypeEnum.CHAR128 , description = "商品的描述")
	private String productDesc;
	public String getProductDesc() {
	return productDesc;
	}
 	 public void setProductDesc(String productDesc){
	 this.productDesc = productDesc;
	}
	@FieldConfig(fieldName = "price", fieldType =FieldTypeEnum.BIG_INT , description = "价钱")
	private long price;
	public long getPrice() {
	return price;
	}
 	 public void setPrice(long price){
	 this.price = price;
	}
	@FieldConfig(fieldName = "integration", fieldType =FieldTypeEnum.BIG_INT , description = "积分")
	private long integration;
	public long getIntegration() {
	return integration;
	}
 	 public void setIntegration(long integration){
	 this.integration = integration;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("productId", String.valueOf(this.productId));
	 entityMap.put("productName", this.productName);
	 entityMap.put("productNum", String.valueOf(this.productNum));
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 entityMap.put("productDesc", this.productDesc);
	 entityMap.put("price", String.valueOf(this.price));
	 entityMap.put("integration", String.valueOf(this.integration));
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.productId=Long.parseLong(entityMap.get("productId"));
	 this.productName=entityMap.get("productName");
	 this.productNum=Integer.parseInt(entityMap.get("productNum"));
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	 this.productDesc=entityMap.get("productDesc");
	 this.price=Long.parseLong(entityMap.get("price"));
	 this.integration=Long.parseLong(entityMap.get("integration"));

    }
}
