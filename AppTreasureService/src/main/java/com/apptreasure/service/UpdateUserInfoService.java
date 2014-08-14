package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.User;
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
import com.frameworkLog.factory.LogFactory;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.updateUserInfo,
        importantParameters = {"session", "encryptType"},
        minorParameters = {"sex", "mobile", "email", "payAccount", "iconURL"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "修改用户信息",
        validateParameters = {
    @Field(fieldName = "sex", fieldType = FieldTypeEnum.TYINT, description = "性别"),
    @Field(fieldName = "mobile", fieldType = FieldTypeEnum.CHAR11, description = "手机"),
    @Field(fieldName = "email", fieldType = FieldTypeEnum.CHAR64, description = "email"),
    @Field(fieldName = "payAccount", fieldType = FieldTypeEnum.CHAR64, description = "支付宝帐号"),
    @Field(fieldName = "iconURL", fieldType = FieldTypeEnum.CHAR128, description = "头像url"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class UpdateUserInfoService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(UpdateUserInfoService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        String sex = parameters.get("sex");
        String mobile = parameters.get("mobile");
        String email = parameters.get("email");
        String payAccount = parameters.get("payAccount");
        String iconURL = parameters.get("iconURL");
        logger.debug("mobile={}", mobile);
        logger.debug("sex={}", sex);
        logger.debug("email={}", email);
        logger.debug("payAccount={}", payAccount);
        logger.debug("iconURL={}", iconURL);
        Map<String, String> updateMap = new HashMap<String, String>(8, 1);
        updateMap.put("userId", String.valueOf(userId));
        if (mobile != null) {
            updateMap.put("mobile", mobile);
        }
        if (sex != null) {
            updateMap.put("sex", sex);
        }
        if (email != null) {
            updateMap.put("email", email);
        }
        if (payAccount != null) {
            updateMap.put("payAccount", payAccount);
        }
        if (iconURL != null) {
            updateMap.put("iconURL", iconURL);
        }
        EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user); //获取公司id
        User user = userDAO.update(updateMap);
        if (user != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("修改用户信息错误", AppTreasureErrorCode.FAIL_CODE);
        }

    }
}
