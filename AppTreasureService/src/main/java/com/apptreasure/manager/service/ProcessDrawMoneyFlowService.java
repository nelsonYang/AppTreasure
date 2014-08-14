package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.entity.DrawAccountFlow;
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
import com.framework.service.api.Service;
import com.framework.utils.DateTimeUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.processDrawFlow,
        importantParameters = {"drawAccountFlowId", "encryptType"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        description = "处理提现记录(后台管理)",
        validateParameters = {
    @Field(fieldName = "drawAccountFlowId", fieldType = FieldTypeEnum.BIG_INT, description = "记录id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class ProcessDrawMoneyFlowService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InsertDrawMoneyFlowService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        long userId = applicationContext.getUserId();
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        logger.debug("parameters={}", parameters);
        EntityDao<DrawAccountFlow> drawAccountFlowDAO = applicationContext.getEntityDAO(EntityNames.drawAccountFlow);
        Map<String, String> drawAccountMap = new HashMap<String, String>(4, 1);
        drawAccountMap.put("userId", String.valueOf(userId));
        drawAccountMap.put("drawAccountFlowId", parameters.get("drawAccountFlowId"));
        String currentTime = DateTimeUtils.currentDay();
        drawAccountMap.put("createTime", currentTime);
        drawAccountMap.put("processStatus", String.valueOf(AppTreasureConstant.PROCESS_STATUS));
        DrawAccountFlow drawAccountFlow = drawAccountFlowDAO.update(drawAccountMap);
        if (drawAccountFlow != null) {
            applicationContext.success();
        } else {
            applicationContext.fail();
        }

    }
}
