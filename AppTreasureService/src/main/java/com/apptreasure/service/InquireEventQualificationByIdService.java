package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.EventQualification;
import com.apptreasure.entity.Product;
import com.apptreasure.entity.ProductImage;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.entity.condition.Condition;
import com.framework.entity.dao.EntityDao;
import com.framework.entity.enumeration.ConditionTypeEnum;
import com.framework.entity.pojo.PrimaryKey;
import com.framework.enumeration.CryptEnum;
import com.framework.enumeration.FieldTypeEnum;
import com.framework.enumeration.LoginEnum;
import com.framework.enumeration.ParameterWrapperTypeEnum;
import com.framework.enumeration.ResponseTypeEnum;
import com.framework.enumeration.TerminalTypeEnum;
import com.framework.service.api.Service;
import com.framework.utils.JsonUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.inquireEventQualificationById,
        importantParameters = {"session", "eventQualificationId", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.AES,
        responseType = ResponseTypeEnum.MAP_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"productId", "productName", "productNum", "productDesc", "createTime", "price", "integration", "images","isExchange"},
        description = "查询预订商品的详细",
        validateParameters = {
    @Field(fieldName = "eventQualificationId", fieldType = FieldTypeEnum.BIG_INT, description = "商品id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquireEventQualificationByIdService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireEventQualificationByIdService.class);

    public void execute() {

        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        String eventQualificationId = parameters.get("eventQualificationId");
        EntityDao<EventQualification> eventQualificationDAO = applicationContext.getEntityDAO(EntityNames.eventQualification);
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("eventQualificationId", eventQualificationId);
        primaryKey.putKeyField("userId", String.valueOf(userId));
        EventQualification eventQualification = eventQualificationDAO.inqurieByKey(primaryKey);
        if (eventQualification != null) {
            EntityDao<Product> entityDAO = applicationContext.getEntityDAO(EntityNames.product);
            primaryKey = new PrimaryKey();
            primaryKey.putKeyField("productId", String.valueOf(eventQualification.getProductId()));
            primaryKey.putKeyField("userId", String.valueOf(userId));
            Product product = entityDAO.inqurieByKey(primaryKey);
            if (product != null) {
                EntityDao<ProductImage> productImageDAO = applicationContext.getEntityDAO(EntityNames.productImage);
                List<Condition> productImageConditonList = new ArrayList<Condition>(1);
                Condition productIdCondition = new Condition("productId", ConditionTypeEnum.EQUAL, String.valueOf(eventQualification.getProductId()));
                productImageConditonList.add(productIdCondition);
                List<ProductImage> productImageList = productImageDAO.inquireByCondition(productImageConditonList);
                Map<String, String> productMap;
                List<Map<String, String>> imageMapList;
                Map<String, String> imageMap;
                productMap = product.toMap();
                productMap.put("images", "[]");
                imageMapList = new ArrayList<Map<String, String>>(0);
                for (ProductImage productImage : productImageList) {
                    if (productImage.getProductId() == product.getProductId()) {
                        imageMap = new HashMap<String, String>(4, 1);
                        imageMap.put("mageURL", productImage.getProductImageURL());
                        imageMap.put("type", String.valueOf(productImage.getType()));
                        imageMapList.add(imageMap);
                    }
                }
                if (!imageMapList.isEmpty()) {
                    String imageJson = JsonUtils.mapListToJsonArray(imageMapList);
                    productMap.put("images", imageJson);
                }
                productMap.put("isExchange", String.valueOf(eventQualification.getIsExchange()));
                applicationContext.setMapData(productMap);
                applicationContext.success();
            } else {
                applicationContext.fail();
            }
        } else {
            applicationContext.fail();
        }



    }
}
