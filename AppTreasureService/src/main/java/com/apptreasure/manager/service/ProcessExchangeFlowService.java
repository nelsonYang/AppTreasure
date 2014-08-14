package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.EventQualification;
import com.apptreasure.entity.ExchangeFlow;
import com.apptreasure.entity.Product;
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
import com.framework.utils.DateTimeUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.processExchangeFlow,
        importantParameters = {"exchangeFlowId", "session", "encryptType"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        description = "处理兑换记录(后台管理)",
        validateParameters = {
    @Field(fieldName = "exchangeFlowId", fieldType = FieldTypeEnum.BIG_INT, description = "流水id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class ProcessExchangeFlowService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(UpdateEventQualificationStatusService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        //long userId = applicationContext.getUserId();
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        logger.debug("parameters={}", parameters);
        EntityDao<ExchangeFlow> exchangeDAO = applicationContext.getEntityDAO(EntityNames.exchangeFlow);
        Map<String, String> exchangeMap = new HashMap<String, String>(4, 1);
     //   exchangeMap.put("userId", String.valueOf(userId));
        exchangeMap.put("exchangeFlowId", parameters.get("exchangeFlowId"));
        String currentTime = DateTimeUtils.currentDay();
        exchangeMap.put("createTime", currentTime);
        exchangeMap.put("processStatus", String.valueOf(AppTreasureConstant.PROCESS_STATUS));
        ExchangeFlow exchangeFlow = exchangeDAO.update(exchangeMap);
        if (exchangeFlow != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("兑换失败", AppTreasureErrorCode.EXCHANGE_FAIL);
        }
  
    }
}
