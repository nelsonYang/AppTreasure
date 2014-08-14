package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.Product;
import com.apptreasure.entity.ProductImage;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.entity.dao.EntityDao;
import com.framework.enumeration.CryptEnum;
import com.framework.enumeration.FieldTypeEnum;
import com.framework.enumeration.LoginEnum;
import com.framework.enumeration.ParameterWrapperTypeEnum;
import com.framework.enumeration.ResponseTypeEnum;
import com.framework.enumeration.TerminalTypeEnum;
import com.framework.exception.RollBackException;
import com.framework.service.api.Service;
import com.framework.utils.DateTimeUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.insertProductImage,
        importantParameters = {"productId", "productImageURL", "type", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.BATCH_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        description = "添加商品图片(后台管理)",
        validateParameters = {
    @Field(fieldName = "type", fieldType = FieldTypeEnum.TYINT, description = "图片类型"),
    @Field(fieldName = "productImageURL", fieldType = FieldTypeEnum.CHAR512, description = "地址"),
    @Field(fieldName = "productId", fieldType = FieldTypeEnum.BIG_INT, description = "商品id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InsertProductImageService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertAddressService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String[]> parameters = applicationContext.getBatchMapParameters();
        String[] types = parameters.get("type");
        String[] productImageURLs = parameters.get("productImageURL");
        String[] productIds = parameters.get("productId");
        logger.debug("parameters={}", parameters);
        String currentTime = DateTimeUtils.currentDay();
        EntityDao<ProductImage> entityDAO = applicationContext.getEntityDAO(EntityNames.productImage);
        Map<String, String> productImageMap;
        for (int index = 0; index < productIds.length; index++) {
            productImageMap = new HashMap<String, String>(4, 1);
            productImageMap.put("createTime", currentTime);
            productImageMap.put("type", types[index]);
            productImageMap.put("productImageURL", productImageURLs[index]);
            productImageMap.put("productId", productIds[index]);
            entityDAO.insert(productImageMap);
        }
        applicationContext.success();

    }
}
