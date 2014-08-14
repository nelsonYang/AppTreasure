package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureErrorCode;

import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.Feedback;
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
        act = AppTreasureActionNames.insertFeedback,
        importantParameters = {"content", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        description = "添加反馈内容",
        validateParameters = {
    @Field(fieldName = "content", fieldType = FieldTypeEnum.CHAR512, description = "反馈的内容"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InsertFeedbackService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertFeedbackService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        logger.debug("parameters={}", parameters);
        parameters.put("userId", String.valueOf(userId));
        String currentTime = DateTimeUtils.currentDay();
        parameters.put("createTime", currentTime);
        EntityDao<Feedback> feedbackDAO = applicationContext.getEntityDAO(EntityNames.feedback);
        Feedback feedback = feedbackDAO.insert(parameters);
        if (feedback != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("反馈新增失败",AppTreasureErrorCode.FAIL_CODE);
        }
    }
}
