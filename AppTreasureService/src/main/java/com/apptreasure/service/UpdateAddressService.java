package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
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
import com.frameworkLog.factory.LogFactory;
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.updateAddress,
        importantParameters = {"addressId", "session", "encryptType"},
        minorParameters = { "province", "city", "region", "street", "post", "name"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "修改地址",
        validateParameters = {
    @Field(fieldName = "addressId", fieldType = FieldTypeEnum.BIG_INT, description = "地址id"),
    @Field(fieldName = "province", fieldType = FieldTypeEnum.CHAR32, description = "省"),
    @Field(fieldName = "city", fieldType = FieldTypeEnum.CHAR32, description = "市"),
    @Field(fieldName = "region", fieldType = FieldTypeEnum.CHAR32, description = "区"),
    @Field(fieldName = "region", fieldType = FieldTypeEnum.CHAR64, description = "街道"),
    @Field(fieldName = "post", fieldType = FieldTypeEnum.CHAR32, description = "邮编"),
    @Field(fieldName = "name", fieldType = FieldTypeEnum.CHAR32, description = "姓名"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class UpdateAddressService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(UpdateAddressService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        logger.debug("parameters={}", parameters);
        EntityDao<Address> addressDAO = applicationContext.getEntityDAO(EntityNames.address);
        Address Address = addressDAO.update(parameters);
        if (Address != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("地址修改失败",AppTreasureErrorCode.FAIL_CODE);
        }
    }
}
