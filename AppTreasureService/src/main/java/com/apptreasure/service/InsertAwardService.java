package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.Award;
import com.apptreasure.entity.DrawAccountFlow;
import com.apptreasure.entity.EntityNames;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.entity.condition.Condition;
import com.framework.entity.dao.EntityDao;
import com.framework.entity.enumeration.ConditionTypeEnum;
import com.framework.enumeration.CryptEnum;
import com.framework.enumeration.FieldTypeEnum;
import com.framework.enumeration.LoginEnum;
import com.framework.enumeration.ParameterWrapperTypeEnum;
import com.framework.enumeration.ResponseTypeEnum;
import com.framework.enumeration.TerminalTypeEnum;
import com.framework.service.api.Service;
import com.framework.utils.DateTimeUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.insertAward,
        importantParameters = {"amount", "session", "payAccount", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "达到500点币奖励1元",
        validateParameters = {
    @Field(fieldName = "amount", fieldType = FieldTypeEnum.INT, description = "点币"),
    @Field(fieldName = "payAccount", fieldType = FieldTypeEnum.CHAR64, description = "支付帐号"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InsertAwardService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertAwardService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;

        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String amountStr = parameters.get("amount");
        logger.debug("parameters={}", parameters);
        long userId = applicationContext.getUserId();
        EntityDao<Award> entityDao = applicationContext.getEntityDAO(EntityNames.award);
        List<Condition> conditionList = new ArrayList<Condition>(1);
        Condition userIdCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
        conditionList.add(userIdCondition);
        List<Award> awardList = entityDao.inquireByCondition(conditionList);
        if (awardList == null || awardList.isEmpty()) {
            Map<String, String> drawAccountMap = new HashMap<String, String>(8, 1);
            drawAccountMap.put("userId", String.valueOf(userId));
            String currentTime = DateTimeUtils.currentDay();
            drawAccountMap.put("createTime", currentTime);
            drawAccountMap.put("amount", amountStr);
            drawAccountMap.put("payAccount", parameters.get("payAccount"));
            drawAccountMap.put("processStatus", String.valueOf(AppTreasureConstant.UN_PROCESS_STATUS));
            Award award = entityDao.insert(drawAccountMap);
            if (award != null) {
                applicationContext.success();
            } else {
                applicationContext.fail();
            }
        } else {
            applicationContext.fail(AppTreasureErrorCode.ALREAD_AWARD);
        }

    }
}
