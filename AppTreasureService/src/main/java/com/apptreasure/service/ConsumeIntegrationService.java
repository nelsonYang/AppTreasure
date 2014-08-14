package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.Account;
import com.apptreasure.entity.DrawAccountFlow;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.consumeIntegration,
        importantParameters = {"channel", "amount", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "消费积分",
        validateParameters = {
    @Field(fieldName = "channel", fieldType = FieldTypeEnum.TYINT, description = "渠道"),
    @Field(fieldName = "amount", fieldType = FieldTypeEnum.BIG_INT, description = "点币"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class ConsumeIntegrationService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(ConsumeIntegrationService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        long userId = applicationContext.getUserId();
      
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String channel = parameters.get("channel");
        String amount = parameters.get("amount");
        logger.debug("parameters={}", parameters);
        EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user);
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("userId", String.valueOf(userId));
        User user = userDAO.inqurieByKey(primaryKey);
        String payAccount = user.getPayAccount();
        if(payAccount == null || payAccount.isEmpty()){
            payAccount = user.getMobile();
        }
        EntityDao<Account> accountDAO = applicationContext.getEntityDAO(EntityNames.account);
        List<Condition> conditionList = new ArrayList<Condition>(2);
        Condition userIdCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
        conditionList.add(userIdCondition);
        Condition statusCondition = new Condition("status", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.EFFECTIVE_STATUS));
        conditionList.add(statusCondition);
        Condition chanelCondition = new Condition("type", ConditionTypeEnum.EQUAL, channel);
        conditionList.add(chanelCondition);
        List<Account> accountList = accountDAO.inquireByCondition(conditionList);
        if (accountList != null && !accountList.isEmpty()) {
            Account account = accountList.get(0);
            long withDrawAccount = Long.parseLong(amount);

            if (account.getBalance() + account.getRecommandBalance() >= withDrawAccount) {
                long deltValue = account.getBalance() - withDrawAccount;
                logger.debug("deltValue = {},balance:{},reccomandBalacne:{},", deltValue, account.getBalance(), account.getRecommandBalance());
                if (deltValue >= 0) {
                    String updateStr = "update account set balance = balance -" + amount + " where accountId=" + account.getAccountId();
                    String[] tableNames = {"account"};
                    parameters.put("userId", String.valueOf(userId));
                    boolean isSuccess = accountDAO.executeUpdateBySql(tableNames, updateStr, new String[]{}, parameters);
                    if (isSuccess) {
                        applicationContext.success();
                    } else {
                        throw new RollBackException("消费积分失败", AppTreasureErrorCode.FAIL_CODE);
                    }
                } else {
                    String updateStr = "update account set balance = 0 ,recommandBalance = recommandBalance +" + deltValue + "  where accountId=" + account.getAccountId();
                    String[] tableNames = {"account"};
                    parameters.put("userId", String.valueOf(userId));
                    boolean isSuccess = accountDAO.executeUpdateBySql(tableNames, updateStr, new String[]{}, parameters);
                    if (isSuccess) {
                        applicationContext.success();
                    } else {
                        throw new RollBackException("消费积分失败", AppTreasureErrorCode.FAIL_CODE);
                    }
                }

                EntityDao<DrawAccountFlow> drawAccountFlowDAO = applicationContext.getEntityDAO(EntityNames.drawAccountFlow);
                Map<String, String> drawAccountMap = new HashMap<String, String>(4, 1);
                drawAccountMap.put("userId", String.valueOf(userId));
                drawAccountMap.put("payway", "1");
                String currentTime = DateTimeUtils.currentDay();
                drawAccountMap.put("createTime", currentTime);
                drawAccountMap.put("drawAmount", amount);
                drawAccountMap.put("payAccount", payAccount);
                drawAccountMap.put("processStatus", String.valueOf(AppTreasureConstant.UN_PROCESS_STATUS));
                DrawAccountFlow drawAccountFlow = drawAccountFlowDAO.insert(drawAccountMap);
                if (drawAccountFlow != null) {
                    applicationContext.success();
                } else {
                     throw new RollBackException("消费积分失败", AppTreasureErrorCode.FAIL_CODE);
                }
            } else {
                applicationContext.fail(AppTreasureErrorCode.MONEY_NOT_ENOUGH);
            }
        } else {
            applicationContext.fail();
        }
    }
}
