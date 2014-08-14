package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.User;
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
import com.frameworkLog.factory.LogFactory;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.updatePassword,
        importantParameters = {"session", "oldPassword", "newPassword", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "修改密码",
        validateParameters = {
    @Field(fieldName = "oldPassword", fieldType = FieldTypeEnum.CHAR64, description = "旧密码"),
    @Field(fieldName = "newPassword", fieldType = FieldTypeEnum.CHAR64, description = "新密码"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class UpdatePasswordService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(UpdatePasswordService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        String oldPassword = parameters.get("oldPassword");
        String newPassword = parameters.get("newPassword");
        logger.debug("oldPassword={}", oldPassword);
        logger.debug("newPassword={}", newPassword);
        EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user); //获取公司id
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("userId", String.valueOf(userId));
        User user = userDAO.inqurieByKey(primaryKey);
        if (user != null) {
            if (user.getPassword().equals(oldPassword)) {
                Map<String, String> updateMap = new HashMap<String, String>(2, 1);
                updateMap.put("userId", String.valueOf(userId));
                updateMap.put("password", newPassword);
                user = userDAO.update(updateMap);
                if (user != null) {
                    applicationContext.success();
                } else {
                    throw new RollBackException("修改密码失败", AppTreasureErrorCode.FAIL_CODE);
                }
            } else {
                applicationContext.fail(AppTreasureErrorCode.PASSWORD_NOT_MAPPING);
            }
        } else {
            applicationContext.fail();
        }

    }
}
