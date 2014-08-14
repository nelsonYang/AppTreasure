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
    entityName = EntityNames.user, 
    keyFields = {"userId"},
    useCache = true,
    timeToIdleSeconds=3000,
    timeToLiveSeconds=6000
)
public class User extends Entity {
  
 
    public PrimaryKey getKeyValue() {
       	PrimaryKey primaryKey = new PrimaryKey();
	 primaryKey.putKeyField("userId", String.valueOf(this.userId));
	 return primaryKey;
    }

    	@FieldConfig(fieldName = "userId", fieldType =FieldTypeEnum.BIG_INT , description = "用户id，用户的帐号")
	private long userId;
	public long getUserId() {
	return userId;
	}
 	 public void setUserId(long userId){
	 this.userId = userId;
	}
	@FieldConfig(fieldName = "userName", fieldType =FieldTypeEnum.CHAR36 , description = "用户名")
	private String userName;
	public String getUserName() {
	return userName;
	}
 	 public void setUserName(String userName){
	 this.userName = userName;
	}
	@FieldConfig(fieldName = "password", fieldType =FieldTypeEnum.CHAR36 , description = "密码md5加密")
	private String password;
	public String getPassword() {
	return password;
	}
 	 public void setPassword(String password){
	 this.password = password;
	}
	@FieldConfig(fieldName = "status", fieldType =FieldTypeEnum.TYINT , description = "用户状态1启用2冻结")
	private byte status;
	public byte getStatus() {
	return status;
	}
 	 public void setStatus(byte status){
	 this.status = status;
	}
	@FieldConfig(fieldName = "type", fieldType =FieldTypeEnum.TYINT , description = "用户类型1-普通用户2-vip用户")
	private byte type;
	public byte getType() {
	return type;
	}
 	 public void setType(byte type){
	 this.type = type;
	}
	@FieldConfig(fieldName = "mobile", fieldType =FieldTypeEnum.CHAR8 , description = "手机号")
	private String mobile;
	public String getMobile() {
	return mobile;
	}
 	 public void setMobile(String mobile){
	 this.mobile = mobile;
	}
	@FieldConfig(fieldName = "sex", fieldType =FieldTypeEnum.TYINT , description = "性别1-男 0-女")
	private byte sex;
	public byte getSex() {
	return sex;
	}
 	 public void setSex(byte sex){
	 this.sex = sex;
	}
	@FieldConfig(fieldName = "email", fieldType =FieldTypeEnum.CHAR36 , description = "邮箱")
	private String email;
	public String getEmail() {
	return email;
	}
 	 public void setEmail(String email){
	 this.email = email;
	}
	@FieldConfig(fieldName = "createTime", fieldType =FieldTypeEnum.DATETIME , description = "创建时间")
	private Date createTime;
	public Date getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(Date createTime){
	 this.createTime = createTime;
	}
	@FieldConfig(fieldName = "payAccount", fieldType =FieldTypeEnum.CHAR36 , description = "支付宝帐号")
	private String payAccount;
	public String getPayAccount() {
	return payAccount;
	}
 	 public void setPayAccount(String payAccount){
	 this.payAccount = payAccount;
	}
	@FieldConfig(fieldName = "iconURL", fieldType =FieldTypeEnum.CHAR64 , description = "图片的url")
	private String iconURL;
	public String getIconURL() {
	return iconURL;
	}
 	 public void setIconURL(String iconURL){
	 this.iconURL = iconURL;
	}
         
            @FieldConfig(fieldName = "recommandAccount", fieldType =FieldTypeEnum.CHAR64 , description = "推荐人")
	private String recommandAccount;
	public String getRecommandAccount() {
	return recommandAccount;
	}
 	 public void setRecommandAccount(String recommandAccount){
	 this.recommandAccount = recommandAccount;
	}



 
    public Map<String, String> toMap() {
        	Map<String, String> entityMap = new HashMap<String, String>(16, 1);
	 entityMap.put("userId", String.valueOf(this.userId));
	 entityMap.put("userName", this.userName);
	 entityMap.put("password", this.password);
	 entityMap.put("status", String.valueOf(this.status));
	 entityMap.put("type", String.valueOf(this.type));
	 entityMap.put("mobile", this.mobile);
	 entityMap.put("sex", String.valueOf(this.sex));
	 entityMap.put("email", this.email);
	 try{
	 entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
	}catch(Exception ex){}
	 entityMap.put("payAccount", this.payAccount);
	 entityMap.put("iconURL", this.iconURL);
                 entityMap.put("recommandAccount", this.recommandAccount);
	 return entityMap;

    }
    
   
    public void parseMap(Map<String, String> entityMap) {
        	 this.userId=Long.parseLong(entityMap.get("userId"));
	 this.userName=entityMap.get("userName");
	 this.password=entityMap.get("password");
	 this.status=Byte.parseByte(entityMap.get("status"));
	 this.type=Byte.parseByte(entityMap.get("type"));
	 this.mobile=entityMap.get("mobile");
	 this.sex=Byte.parseByte(entityMap.get("sex"));
	 this.email=entityMap.get("email");
	try{
	 this.createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
	}catch(Exception ex){}
	 this.payAccount=entityMap.get("payAccount");
	 this.iconURL=entityMap.get("iconURL");
                 this.recommandAccount = entityMap.get("recommandAccount");

    }
}
