package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.entity.Address;
import com.apptreasure.entity.EntityNames;
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
        act = AppTreasureActionNames.inquireAddress,
        importantParameters = {"session", "encryptType"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.AES,
        responseType = ResponseTypeEnum.ENTITY_LIST_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"addressId","province", "city", "region", "street", "post", "name","isDefault"},
        description = "查询地址类表",
        validateParameters = {
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquireAddressService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireAddressService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        long userId = applicationContext.getUserId();
        EntityDao<Address> eventDAO = applicationContext.getEntityDAO(EntityNames.address); 
        List<Condition> conditionList = new ArrayList<Condition>(1);
        Condition userIdCondition = new Condition("userId",ConditionTypeEnum.EQUAL,String.valueOf(userId));
        conditionList.add(userIdCondition);
        List<Address> addressList = eventDAO.inquireByCondition(conditionList);
        if (addressList != null) {
            applicationContext.setEntityList(addressList);
            applicationContext.success();
        } else {
            applicationContext.fail();
        }

    }
}
