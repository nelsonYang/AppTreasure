package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.Product;
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
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.insertProduct,
        importantParameters = {"productName", "productNum", "productDesc", "price", "integration", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        description = "添加商品(后台管理)",
        validateParameters = {
    @Field(fieldName = "productName", fieldType = FieldTypeEnum.CHAR64, description = "商品名称"),
    @Field(fieldName = "productNum", fieldType = FieldTypeEnum.TYINT, description = "数量"),
    @Field(fieldName = "productDesc", fieldType = FieldTypeEnum.CHAR512, description = "描述"),
    @Field(fieldName = "price", fieldType = FieldTypeEnum.BIG_INT, description = "价钱"),
    @Field(fieldName = "integration", fieldType = FieldTypeEnum.BIG_INT, description = "点比"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InsertProductService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertAddressService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        logger.debug("parameters={}", parameters);
        String currentTime = DateTimeUtils.currentDay();
        parameters.put("createTime", currentTime);
        EntityDao<Product> entityDAO = applicationContext.getEntityDAO(EntityNames.product);
        Product product = entityDAO.insert(parameters);
        if (product != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("商品新增失败", AppTreasureErrorCode.FAIL_CODE);
        }
    }
}
