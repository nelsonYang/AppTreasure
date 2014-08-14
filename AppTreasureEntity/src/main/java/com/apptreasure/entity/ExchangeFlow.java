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
        entityName = EntityNames.exchangeFlow,
        keyFields = {"exchangeFlowId"},
        useCache = true,
        timeToIdleSeconds = 3000,
        timeToLiveSeconds = 6000)
public class ExchangeFlow extends Entity {

    public PrimaryKey getKeyValue() {
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("exchangeFlowId", String.valueOf(this.exchangeFlowId));
        return primaryKey;
    }
    @FieldConfig(fieldName = "exchangeFlowId", fieldType = FieldTypeEnum.BIG_INT, description = "兑换id")
    private long exchangeFlowId;

    public long getExchangeFlowId() {
        return exchangeFlowId;
    }

    public void setExchangeFlowId(long exchangeFlowId) {
        this.exchangeFlowId = exchangeFlowId;
    }
    @FieldConfig(fieldName = "exchangeNum", fieldType = FieldTypeEnum.TYINT, description = "兑换的数量")
    private byte exchangeNum;

    public byte getExchangeNum() {
        return exchangeNum;
    }

    public void setExchangeNum(byte exchangeNum) {
        this.exchangeNum = exchangeNum;
    }
    @FieldConfig(fieldName = "exchangeAmount", fieldType = FieldTypeEnum.INT, description = "兑换的金额")
    private int exchangeAmount;

    public int getExchangeAmount() {
        return exchangeAmount;
    }

    public void setExchangeAmount(int exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }
    @FieldConfig(fieldName = "productId", fieldType = FieldTypeEnum.BIG_INT, description = "兑换商品id")
    private long productId;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
    @FieldConfig(fieldName = "createTime", fieldType = FieldTypeEnum.DATETIME, description = "兑换的时间")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @FieldConfig(fieldName = "userId", fieldType = FieldTypeEnum.BIG_INT, description = "用户id")
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    @FieldConfig(fieldName = "processStatus", fieldType = FieldTypeEnum.TYINT, description = "状态")
    private byte processStatus;
    
    public byte getProcessStatus(){
        return processStatus;
    }
    
    public void setProcessStatus(byte processStatus){
        this.processStatus = processStatus;
    }

    public Map<String, String> toMap() {
        Map<String, String> entityMap = new HashMap<String, String>(16, 1);
        entityMap.put("exchangeFlowId", String.valueOf(this.exchangeFlowId));
        entityMap.put("exchangeNum", String.valueOf(this.exchangeNum));
        entityMap.put("exchangeAmount", String.valueOf(this.exchangeAmount));
        entityMap.put("productId", String.valueOf(this.productId));
        try {
            entityMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception ex) {
        }
        entityMap.put("userId", String.valueOf(this.userId));
        entityMap.put("processStatus", String.valueOf(this.processStatus));
        return entityMap;

    }

    public void parseMap(Map<String, String> entityMap) {
        this.exchangeFlowId = Long.parseLong(entityMap.get("exchangeFlowId"));
        this.exchangeNum = Byte.parseByte(entityMap.get("exchangeNum"));
        this.exchangeAmount = Integer.parseInt(entityMap.get("exchangeAmount"));
        this.productId = Long.parseLong(entityMap.get("productId"));
        try {
            this.createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entityMap.get("createTime"));
        } catch (Exception ex) {
        }
        this.userId = Long.parseLong(entityMap.get("userId"));
        this.processStatus = Byte.parseByte(entityMap.get("processStatus"));

    }
}
