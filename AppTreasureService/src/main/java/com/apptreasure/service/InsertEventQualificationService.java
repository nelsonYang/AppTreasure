package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.EventQualification;
import com.apptreasure.entity.Product;
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
import com.framework.exception.RollBackException;
import com.framework.service.api.Service;
import com.framework.utils.DateTimeUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.insertEventQualification,
        importantParameters = {"productId", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "报名",
        validateParameters = {
    @Field(fieldName = "productId", fieldType = FieldTypeEnum.BIG_INT, description = "商品id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InsertEventQualificationService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertEventQualificationService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        String productId = parameters.get("productId");
        logger.debug("parameters={}", parameters);
        parameters.put("userId", String.valueOf(userId));
        String currentTime = DateTimeUtils.currentDay();
        parameters.put("createTime", currentTime);
        parameters.put("isExchange", String.valueOf(AppTreasureConstant.UN_ALREAD_EXCHAGE));
        parameters.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
        EntityDao<EventQualification> entityDAO = applicationContext.getEntityDAO(EntityNames.eventQualification);
//        List<Condition> conditionList = new ArrayList<Condition>(1);
//        Condition userCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
//        conditionList.add(userCondition);
//        Condition statusCondition = new Condition("status", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.NORMAL_STATUS));
//        conditionList.add(statusCondition);
//        Condition productCondition = new Condition("productId", ConditionTypeEnum.EQUAL, productId);
//        conditionList.add(productCondition);
//        List<EventQualification> eventQualificationList = entityDAO.inquireByCondition(conditionList);
        EntityDao<Product> productDAO = applicationContext.getEntityDAO(EntityNames.product);
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("productId", parameters.get("productId"));
        Product product = productDAO.inqurieByKey(primaryKey);
        if (product != null) {
            if (product.getProductNum() > 0) {
//                if (eventQualificationList == null || eventQualificationList.isEmpty()) {
                    EventQualification eventQualification = entityDAO.insert(parameters);
                    if (eventQualification != null) {
                        String updateStr = "update product set productNum = productNum - 1  where productId =" + productId;
                        String[] tableNames = {"product"};
                        parameters.put("userId", String.valueOf(userId));
                        boolean isSuccess = productDAO.executeUpdateBySql(tableNames, updateStr, new String[]{}, parameters);
                        if (isSuccess) {
                            applicationContext.success();
                        } else {
                            throw new RollBackException("报名失败", AppTreasureErrorCode.FAIL_CODE);
                        }
                    } else {
                        throw new RollBackException("报名失败", AppTreasureErrorCode.FAIL_CODE);
                    }
//                } else {
//                    applicationContext.fail(AppTreasureErrorCode.ALREAD_QULIFACATION);
//                }
            } else {
                applicationContext.fail(AppTreasureErrorCode.PRODUCT_NUM_NOT_ENOUGH);
            }
        } else {
            applicationContext.fail(AppTreasureErrorCode.NO_PRODUCT);
        }

    }
}
