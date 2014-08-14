package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.Address;
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
import com.framework.exception.RollBackException;
import com.framework.service.api.Service;
import com.framework.utils.DateTimeUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.insertAddress,
        importantParameters = {"province", "city", "region", "street", "post", "name", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "添加地址",
        validateParameters = {
    @Field(fieldName = "province", fieldType = FieldTypeEnum.CHAR32, description = "省"),
    @Field(fieldName = "city", fieldType = FieldTypeEnum.CHAR32, description = "市"),
    @Field(fieldName = "region", fieldType = FieldTypeEnum.CHAR32, description = "区"),
    @Field(fieldName = "street", fieldType = FieldTypeEnum.CHAR64, description = "街道"),
    @Field(fieldName = "post", fieldType = FieldTypeEnum.CHAR32, description = "邮编"),
    @Field(fieldName = "name", fieldType = FieldTypeEnum.CHAR32, description = "姓名"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InsertAddressService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertAddressService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        logger.debug("parameters={}", parameters);
        parameters.put("userId", String.valueOf(userId));
        String currentTime = DateTimeUtils.currentDay();
        parameters.put("createTime", currentTime);
        parameters.put("isDefault", String.valueOf(AppTreasureConstant.DEFAULT_ADDRESS));
        EntityDao<Address> addressDAO = applicationContext.getEntityDAO(EntityNames.address);
        Address Address = addressDAO.insert(parameters);
        if (Address != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("地址新增失败",AppTreasureErrorCode.FAIL_CODE);
        }
    }
}
