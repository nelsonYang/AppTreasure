package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.PayConfig;
import com.apptreasure.entity.Version;
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
        act = AppTreasureActionNames.inquireVersion,
        importantParameters = {"session", "encryptType"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.AES,
        responseType = ResponseTypeEnum.ENTITY_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"versionId", "versionName", "versionCode", "versionUpdateTime", "versionContent", "size", "forceUpdate", "downloadUrl"},
        description = "查询版本号",
        validateParameters = {
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquireVersionService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireVersionService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;

        EntityDao<Version> entityDAO = applicationContext.getEntityDAO(EntityNames.version); //获取公司id
        List<Condition> conditionList = new ArrayList<Condition>(0);
        List<Version> versionList = entityDAO.inquireByCondition(conditionList);
        if (versionList != null && !versionList.isEmpty()) {
            Version version = versionList.get(0);
            applicationContext.setEntityData(version);
            applicationContext.success();
        } else {
            applicationContext.noData();
        }

    }
}
