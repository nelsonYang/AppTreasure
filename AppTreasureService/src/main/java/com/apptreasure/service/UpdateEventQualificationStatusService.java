package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.EventQualification;
import com.apptreasure.entity.ExchangeFlow;
import com.apptreasure.entity.Product;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.entity.dao.EntityDao;
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
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.updateEventQualificationStatus,
        importantParameters = {"eventQualificationId", "num", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "更新状态",
        validateParameters = {
    @Field(fieldName = "num", fieldType = FieldTypeEnum.TYINT, description = "数量"),
    @Field(fieldName = "eventQualificationId", fieldType = FieldTypeEnum.BIG_INT, description = "报名id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class UpdateEventQualificationStatusService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(UpdateEventQualificationStatusService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        long userId = applicationContext.getUserId();
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String eventQualificationId = parameters.get("eventQualificationId");
        String num = parameters.get("num");
        logger.debug("parameters={}", parameters);
        parameters.put("eventQualificationId", eventQualificationId);
        parameters.put("isExchange", String.valueOf(AppTreasureConstant.ALREAD_EXCHAGE));
        EntityDao<EventQualification> eventQualificationDAO = applicationContext.getEntityDAO(EntityNames.eventQualification);
        EventQualification eventQualification = eventQualificationDAO.update(parameters);
        if (eventQualification != null) {
            EntityDao<ExchangeFlow> exchangeDAO = applicationContext.getEntityDAO(EntityNames.exchangeFlow);
            Map<String, String> exchangeMap = new HashMap<String, String>(4, 1);
            exchangeMap.put("userId", String.valueOf(userId));
            exchangeMap.put("productId", String.valueOf(eventQualification.getProductId()));
            String currentTime = DateTimeUtils.currentDay();
            exchangeMap.put("createTime", currentTime);
            EntityDao<Product> entityDAO = applicationContext.getEntityDAO(EntityNames.product);
            PrimaryKey primaryKey = new PrimaryKey();
            primaryKey.putKeyField("productId", String.valueOf(eventQualification.getProductId()));
            primaryKey.putKeyField("userId", String.valueOf(userId));
            Product product = entityDAO.inqurieByKey(primaryKey);
            exchangeMap.put("exchangeAmount", String.valueOf(product.getIntegration() * Integer.parseInt(num)));
            exchangeMap.put("exchangeNum", num);
            exchangeMap.put("processStatus", String.valueOf(AppTreasureConstant.UN_PROCESS_STATUS));
            ExchangeFlow exchangeFlow = exchangeDAO.insert(exchangeMap);
            if (exchangeFlow != null) {
                applicationContext.success();
            } else {
                throw new RollBackException("兑换失败", AppTreasureErrorCode.EXCHANGE_FAIL);
            }
            applicationContext.success();
        } else {
            throw new RollBackException("更新状态", AppTreasureErrorCode.FAIL_CODE);
        }
    }
}
