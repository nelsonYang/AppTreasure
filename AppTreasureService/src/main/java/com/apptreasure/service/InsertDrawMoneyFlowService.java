package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.entity.DrawAccountFlow;
import com.apptreasure.entity.EntityNames;
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
import com.framework.service.api.Service;
import com.framework.utils.DateTimeUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.insertDrawMoneyFlow,
        importantParameters = {"amount", "session", "payway", "payAccount", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "提现记录",
        validateParameters = {
    @Field(fieldName = "payway", fieldType = FieldTypeEnum.TYINT, description = "支付方式"),
    @Field(fieldName = "amount", fieldType = FieldTypeEnum.INT, description = "点币"),
    @Field(fieldName = "payAccount", fieldType = FieldTypeEnum.CHAR64, description = "支付帐号"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InsertDrawMoneyFlowService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertDrawMoneyFlowService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;

        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String amountStr = parameters.get("amount");
        logger.debug("parameters={}", parameters);
        long userId = applicationContext.getUserId();
        EntityDao<DrawAccountFlow> drawAccountFlowDAO = applicationContext.getEntityDAO(EntityNames.drawAccountFlow);
        Map<String, String> drawAccountMap = new HashMap<String, String>(4, 1);
        drawAccountMap.put("userId", String.valueOf(userId));
        drawAccountMap.put("payway", parameters.get("payway"));
        String currentTime = DateTimeUtils.currentDay();
        drawAccountMap.put("createTime", currentTime);
        drawAccountMap.put("drawAmount", amountStr);
        drawAccountMap.put("payAccount", parameters.get("payAccount"));
        drawAccountMap.put("processStatus", String.valueOf(AppTreasureConstant.UN_PROCESS_STATUS));
        DrawAccountFlow drawAccountFlow = drawAccountFlowDAO.insert(drawAccountMap);
        if (drawAccountFlow != null) {
            applicationContext.success();
        } else {
            applicationContext.fail();
        }

    }
}
