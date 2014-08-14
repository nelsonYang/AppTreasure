package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.entity.Account;
import com.apptreasure.entity.Address;
import com.apptreasure.entity.Award;
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
import com.framework.service.api.Service;
import com.framework.utils.ConverterUtils;
import com.framework.utils.JsonUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.inquireUserInfo,
        importantParameters = {"session", "encryptType"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.AES,
        responseType = ResponseTypeEnum.MAP_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"userName", "payAccount", "mobile", "email", "sex", "iconURL", "balance", "accountDetailList", "isAward", "downloadUrl", "street", "name","recommandBalance"},
        description = "查询用户的信息",
        validateParameters = {
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquireUserInfoService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireUserInfoService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        long userId = applicationContext.getUserId();
        EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user);
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("userId", String.valueOf(userId));
        User user = userDAO.inqurieByKey(primaryKey);
        if (user != null) {
            long balance = 0;
            double recommandBalance = 0;
            Map<String, String> userMap = user.toMap();
            List<Condition> condtionList = new ArrayList<Condition>(7);
            Condition userIdCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
            condtionList.add(userIdCondition);
            Condition statusCondition = new Condition("status", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.NORMAL_STATUS));
            condtionList.add(statusCondition);
            EntityDao<Account> accountDAO = applicationContext.getEntityDAO(EntityNames.account);
            List<Account> accountList = accountDAO.inquireByCondition(condtionList);
            for (Account account : accountList) {
                balance = balance + account.getBalance();
                recommandBalance = recommandBalance + account.getRecommandBalance();
            }
            userMap.put("recommandBalance", String.valueOf(recommandBalance));
            logger.debug("balance:{},recommandBalance:{}",balance,recommandBalance);
            userMap.put("balance", String.valueOf(balance + recommandBalance));
            List<Map<String, String>> resultMapList = ConverterUtils.toMapList(accountList);
            String accountListJson = JsonUtils.mapListToJsonArray(resultMapList);
            userMap.put("accountDetailList", accountListJson);
            EntityDao<Award> entityDao = applicationContext.getEntityDAO(EntityNames.award);
            List<Condition> conditionList = new ArrayList<Condition>(1);
            userIdCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
            conditionList.add(userIdCondition);
            List<Award> awardList = entityDao.inquireByCondition(conditionList);
            userMap.put("isAward", "0");
            if (awardList != null && !awardList.isEmpty()) {
                userMap.put("isAward", "1");
            }
            userMap.put("downloadUrl", AppTreasureConstant.DOWNLOAD_URL);
            userMap.put("street", "");
            userMap.put("name", "");
            EntityDao<Address> eventDAO = applicationContext.getEntityDAO(EntityNames.address);
            conditionList = new ArrayList<Condition>(1);
            userIdCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
            conditionList.add(userIdCondition);
            List<Address> addressList = eventDAO.inquireByCondition(conditionList);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                userMap.put("street", address.getStreet());
                userMap.put("name", address.getName());
            }
            applicationContext.setMapData(userMap);
            applicationContext.success();
        } else {
            applicationContext.fail();
        }

    }
}
