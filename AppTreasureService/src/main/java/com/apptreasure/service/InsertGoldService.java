package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.Account;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.RewardFlow;
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
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.insertGold,
        importantParameters = {"channel", "rewardAmount", "session", "encryptType"},
        minorParameters = {"appName"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "保存点币",
        validateParameters = {
    @Field(fieldName = "appName", fieldType = FieldTypeEnum.CHAR32, description = "app名字"),
    @Field(fieldName = "channel", fieldType = FieldTypeEnum.TYINT, description = "渠道"),
    @Field(fieldName = "rewardAmount", fieldType = FieldTypeEnum.INT, description = "乐币"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InsertGoldService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertGoldService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        HttpServletRequest request = applicationContext.getHttpServletRequest();
        String ip = request.getRemoteAddr();
        String channel = parameters.get("channel");
        String rewardAmount = parameters.get("rewardAmount");
        logger.debug("parameters={}", parameters);
        logger.debug("ip={}", ip);
        parameters.put("ip", ip);
        parameters.put("userId", String.valueOf(userId));
        String currentTime = DateTimeUtils.currentDay();
        parameters.put("createTime", currentTime);
        parameters.put("rewardItem", "1");
        double realAmount = (Double.parseDouble(rewardAmount) * 1.0) / AppTreasureConstant.PERCENT;
        parameters.put("realAmount", String.valueOf(realAmount));
        EntityDao<RewardFlow> rewardFlowDAO = applicationContext.getEntityDAO(EntityNames.rewardFlow);

        EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user);
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("userId", String.valueOf(userId));
        User user = userDAO.inqurieByKey(primaryKey);
        String recommandAccount = user.getRecommandAccount();
        logger.debug("recommandAccount :{}", recommandAccount);
        RewardFlow rewardFlow = rewardFlowDAO.insert(parameters);
        if (rewardFlow != null) {
            List<Condition> userconditionList = new ArrayList<Condition>(2);
            Condition userNameCondition = new Condition("userName", ConditionTypeEnum.EQUAL, recommandAccount);
            userconditionList.add(userNameCondition);
            List<User> userList = userDAO.inquireByCondition(userconditionList);
            EntityDao<Account> accountDAO = applicationContext.getEntityDAO(EntityNames.account);
            if (recommandAccount != null && !recommandAccount.isEmpty() && userList != null && !userList.isEmpty()) {
                List<Condition> conditionList = new ArrayList<Condition>(1);
                Condition userCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
                Condition typeCondition = new Condition("type", ConditionTypeEnum.EQUAL, String.valueOf(channel));
                conditionList.add(userCondition);
                conditionList.add(typeCondition);
                List<Account> accountList = accountDAO.inquireByCondition(conditionList);
                long totalAmount = accountList.get(0).getBalance();
                 long singleAmount  = 0;
                if(totalAmount >= 0){
                   singleAmount = Long.parseLong(rewardAmount) - totalAmount;
                    if (singleAmount < 0) {
                        singleAmount = 0;
                    }
                }
                logger.debug("singleAmount={},totalAmount={}", singleAmount,totalAmount);
                String updateSql = "update account set recommandBalance = recommandBalance + " + (singleAmount * 1.0 / 10) + " where userId=" + userList.get(0).getUserId() + " and type =" + channel;
                logger.debug("updateSql={}", updateSql);
                Map<String, String> partitionMap = new HashMap<String, String>(2, 1);
                partitionMap.put("userId", recommandAccount);
                String[] tableNames = {"account"};
                accountDAO.executeUpdateBySql(tableNames, updateSql, new String[]{}, partitionMap);
            }
            String updateStr = "update account set balance = " + rewardAmount + " where userId=" + userId + " and type =" + channel;
            String[] tableNames = {"account"};
            parameters.put("userId", String.valueOf(userId));
            boolean isSuccess = accountDAO.executeUpdateBySql(tableNames, updateStr, new String[]{}, parameters);
            if (isSuccess) {
                applicationContext.success();
            } else {
                throw new RollBackException("新增点币失败", AppTreasureErrorCode.INSERT_GOLD_FAIL);
            }
        } else {
            throw new RollBackException("新增点币失败", AppTreasureErrorCode.INSERT_GOLD_FAIL);
        }
    }
}
