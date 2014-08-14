package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.Account;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.User;
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
import com.framework.exception.RollBackException;
import com.framework.service.api.Service;
import com.framework.utils.DateTimeUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.register,
        importantParameters = {"password"},
        minorParameters = {"recommandAccount"},
        requestEncrypt = CryptEnum.SIGN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.MAP_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.NOT_REQUIRE,
        returnParameters ={"userName"},
        description = "注册",
        validateParameters = {
    @Field(fieldName = "password", fieldType = FieldTypeEnum.CHAR64, description = "密码"),
    @Field(fieldName = "recommandAccount", fieldType = FieldTypeEnum.CHAR64, description = "推荐人"),
    @Field(fieldName = "sign", fieldType = FieldTypeEnum.CHAR1024, description = "签名验证"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class RegisterService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(RegisterService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String password = parameters.get("password");
        String recommandAccount = parameters.get("recommandAccount");
        logger.debug("password={},recommandAccount={}", password,recommandAccount);
        EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user);
        EntityDao<Account> accountDAO = applicationContext.getEntityDAO(EntityNames.account);
        // 生成用户名
        int value = new Random().nextInt(1000000);
        String userName = String.valueOf(value);
        List<Condition> conditionList = new ArrayList<Condition>(1);
        Condition userNameCondition = new Condition("userName", ConditionTypeEnum.EQUAL, userName);
        conditionList.add(userNameCondition);
        List<User> userList = userDAO.inquireByCondition(conditionList);
        if (!userList.isEmpty()) {
            while (true) {
                value = new Random().nextInt(1000000);
                userName = String.valueOf(value);
                conditionList = new ArrayList<Condition>(1);
                userNameCondition = new Condition("userName", ConditionTypeEnum.EQUAL, userName);
                conditionList.add(userNameCondition);
                userList = userDAO.inquireByCondition(conditionList);
                if (userList.isEmpty()) {
                    break;
                }
            }

        }
        if(recommandAccount != null && !recommandAccount.isEmpty()){
            parameters.put("recommandAccount", recommandAccount);
        }
        
        parameters.put("userName", userName);
        //获取公司id
        String currentTime = DateTimeUtils.currentDay();
        parameters.put("createTime", currentTime);
        parameters.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
        parameters.put("type", String.valueOf(AppTreasureConstant.NORMAL_TYPE));
        User user = userDAO.insert(parameters);
        if (user != null) {
            Map<String, String> accountMap = new HashMap<String, String>(8, 1);
            accountMap.put("accountNO", AppTreasureConstant.DIAN_RU_PREFIX + userName);
            accountMap.put("accountName", userName);
            accountMap.put("userId", String.valueOf(user.getUserId()));
            accountMap.put("createTime", currentTime);
            accountMap.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
            accountMap.put("type", String.valueOf(AppTreasureConstant.DIAN_RU_TYPE));
            Account dianRuAccount = accountDAO.insert(accountMap);

            accountMap = new HashMap<String, String>(8, 1);
            accountMap.put("accountNO", AppTreasureConstant.DIAN_YOU_QIAN_PREFIX + userName);
            accountMap.put("accountName", userName);
            accountMap.put("userId", String.valueOf(user.getUserId()));
            accountMap.put("createTime", currentTime);
            accountMap.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
            accountMap.put("type", String.valueOf(AppTreasureConstant.DIAN_YOU_QIAN_TYPE));
            Account dianYouQianAccount = accountDAO.insert(accountMap);

            accountMap = new HashMap<String, String>(8, 1);
            accountMap.put("accountNO", AppTreasureConstant.DOU_MENG_PREFIX + userName);
            accountMap.put("accountName", userName);
            accountMap.put("userId", String.valueOf(user.getUserId()));
            accountMap.put("createTime", currentTime);
            accountMap.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
            accountMap.put("type", String.valueOf(AppTreasureConstant.DOU_MENG_TYPE));
            Account douMengAccount = accountDAO.insert(accountMap);

            accountMap = new HashMap<String, String>(8, 1);
            accountMap.put("accountNO", AppTreasureConstant.LI_MEI_PREFIX + userName);
            accountMap.put("accountName", userName);
            accountMap.put("userId", String.valueOf(user.getUserId()));
            accountMap.put("createTime", currentTime);
            accountMap.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
            accountMap.put("type", String.valueOf(AppTreasureConstant.LI_MEI_TYPE));
            Account liMengAccount = accountDAO.insert(accountMap);

            accountMap = new HashMap<String, String>(8, 1);
            accountMap.put("accountNO", AppTreasureConstant.MI_DI_PREFIX + userName);
            accountMap.put("accountName", userName);
            accountMap.put("userId", String.valueOf(user.getUserId()));
            accountMap.put("createTime", currentTime);
            accountMap.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
            accountMap.put("type", String.valueOf(AppTreasureConstant.MI_DI_TYPE));
            Account miDiAccount = accountDAO.insert(accountMap);

            accountMap = new HashMap<String, String>(8, 1);
            accountMap.put("accountNO", AppTreasureConstant.YI_JI_FEN_PREFIX + userName);
            accountMap.put("accountName", userName);
            accountMap.put("userId", String.valueOf(user.getUserId()));
            accountMap.put("createTime", currentTime);
            accountMap.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
            accountMap.put("type", String.valueOf(AppTreasureConstant.YI_JI_FEN_TYPE));
            Account yiJiFenAccount = accountDAO.insert(accountMap);

            accountMap = new HashMap<String, String>(8, 1);
            accountMap.put("accountNO", AppTreasureConstant.YOU_MI_PREFIX + userName);
            accountMap.put("accountName", userName);
            accountMap.put("userId", String.valueOf(user.getUserId()));
            accountMap.put("createTime", currentTime);
            accountMap.put("status", String.valueOf(AppTreasureConstant.NORMAL_STATUS));
            accountMap.put("type", String.valueOf(AppTreasureConstant.YOU_MI_TYPE));
            Account youMiAccount = accountDAO.insert(accountMap);
            if (dianRuAccount != null && dianYouQianAccount != null && douMengAccount != null && liMengAccount != null && miDiAccount != null && yiJiFenAccount != null && youMiAccount != null) {
                Map<String,String> resultMap = new HashMap<String,String>(2,1);
                resultMap.put("userName", userName);
                applicationContext.setMapData(resultMap);
                applicationContext.success();
            } else {
                throw new RollBackException("注册失败", AppTreasureErrorCode.FAIL_CODE);
            }
        } else {
            throw new RollBackException("注册失败", AppTreasureErrorCode.FAIL_CODE);
        }

    }
}
