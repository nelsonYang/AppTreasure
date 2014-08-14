package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.EventQualification;
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
        act = AppTreasureActionNames.updateEventQualification,
        importantParameters = {"eventQualificationId", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "取消报名",
        validateParameters = {
    @Field(fieldName = "eventQualificationId", fieldType = FieldTypeEnum.BIG_INT, description = "报名id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class UpdateEventQualificationService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(UpdateEventQualificationService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String eventQualificationId = parameters.get("eventQualificationId");
        logger.debug("parameters={}", parameters);
        parameters.put("eventQualificationId", eventQualificationId);
        parameters.put("status", String.valueOf(AppTreasureConstant.PAUSE_STATUS));
        EntityDao<EventQualification> entityDAO = applicationContext.getEntityDAO(EntityNames.eventQualification);
        EventQualification eventQualification = entityDAO.update(parameters);
        if (eventQualification != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("取消报名失败",AppTreasureErrorCode.FAIL_CODE);
        }
    }
}
