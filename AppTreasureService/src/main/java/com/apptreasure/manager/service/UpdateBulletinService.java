package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.Bulletin;
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
        act = AppTreasureActionNames.updateBulletion,
        importantParameters = {"bulletinId", "session", "encryptType"},
        minorParameters = {"title", "content"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        description = "修改公告(后台管理)",
        validateParameters = {
    @Field(fieldName = "bulletinId", fieldType = FieldTypeEnum.BIG_INT, description = "公告id"),
    @Field(fieldName = "title", fieldType = FieldTypeEnum.CHAR32, description = "标题"),
    @Field(fieldName = "content", fieldType = FieldTypeEnum.CHAR512, description = "内容"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class UpdateBulletinService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(UpdateAddressService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        logger.debug("parameters={}", parameters);
        EntityDao<Bulletin> bulletinDAO = applicationContext.getEntityDAO(EntityNames.bulletin);
        Bulletin bulletin = bulletinDAO.update(parameters);
        if (bulletin != null) {
            applicationContext.success();
        } else {
            throw new RollBackException("地址公告失败", AppTreasureErrorCode.FAIL_CODE);
        }
    }
}
