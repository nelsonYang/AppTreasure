package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.PayConfig;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.entity.condition.Condition;
import com.framework.entity.dao.EntityDao;
import com.framework.enumeration.CryptEnum;
import com.framework.enumeration.FieldTypeEnum;
import com.framework.enumeration.LoginEnum;
import com.framework.enumeration.ParameterWrapperTypeEnum;
import com.framework.enumeration.ResponseTypeEnum;
import com.framework.enumeration.TerminalTypeEnum;
import com.framework.service.api.Service;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.inquirePayConfig,
        importantParameters = {"session", "encryptType"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.AES,
        responseType = ResponseTypeEnum.ENTITY_LIST_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"payName", "payCode"},
        description = "查询支付方式的配置",
        validateParameters = {
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquirePayConfigService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquirePayConfigService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;

        EntityDao<PayConfig> payConfigDAO = applicationContext.getEntityDAO(EntityNames.payConfig); //获取公司id
        List<Condition> conditionList = new ArrayList<Condition>(0);
        List<PayConfig> payConfigList = payConfigDAO.inquireByCondition(conditionList);
        if (payConfigList != null) {
            applicationContext.setEntityList(payConfigList);
            applicationContext.success();
        } else {
            applicationContext.fail();
        }

    }
}
