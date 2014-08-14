package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.UserLocation;
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
import java.util.List;

import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.saveOrUpdateUserLocation,
        importantParameters = {"session", "encryptType", "latitution", "lagtion"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "修改用户的地理信息",
        validateParameters = {
    @Field(fieldName = "latitution", fieldType = FieldTypeEnum.DOUBLE, description = "精度"),
    @Field(fieldName = "lagtion", fieldType = FieldTypeEnum.DOUBLE, description = "维度"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class SaveOrUpdateUserLocationService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(SaveOrUpdateUserLocationService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        logger.debug("parameters={}", parameters);
        EntityDao<UserLocation> userLoactionDAO = applicationContext.getEntityDAO(EntityNames.userLocation);
        List<Condition> conditionList = new ArrayList<Condition>(1);
        Condition userIdCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
        conditionList.add(userIdCondition);
        UserLocation userLocation = null;
        synchronized (this) {
            List<UserLocation> locationList = userLoactionDAO.inquireByCondition(conditionList);
            if (locationList != null && !locationList.isEmpty()) {
                 userLocation = locationList.get(0);
                String currentTime = DateTimeUtils.currentDay();
                parameters.put("lastUpdateTime", currentTime);
                parameters.put("userId", String.valueOf(userId));
                parameters.put("userLocationId", String.valueOf(userLocation.getUserLocationId()));
                userLoactionDAO.update(parameters);
            } else {
                String currentTime = DateTimeUtils.currentDay();
                parameters.put("lastUpdateTime", currentTime);
                parameters.put("userId", String.valueOf(userId));
                userLoactionDAO.insert(parameters);
            }
        }
        if (userLocation != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("修改地理信息错误",AppTreasureErrorCode.FAIL_CODE);
        }

    }
}
