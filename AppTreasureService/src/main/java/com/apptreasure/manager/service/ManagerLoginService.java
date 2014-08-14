package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.Manager;
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
import com.framework.spi.entity.SessionEntity;
import com.framework.utils.Base64Utils;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.managerLogin,
        importantParameters = {"userName", "password"},
        requestEncrypt = CryptEnum.SIGN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.MAP_DATA_JSON,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.NOT_REQUIRE,
        returnParameters = {"session", "key"},
        description = "管理员登录(后台管理)",
        validateParameters = {
    @Field(fieldName = "userName", fieldType = FieldTypeEnum.CHAR24, description = "用户名"),
    @Field(fieldName = "password", fieldType = FieldTypeEnum.CHAR64, description = "密码"),
    @Field(fieldName = "sign", fieldType = FieldTypeEnum.CHAR1024, description = "签名验证"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class ManagerLoginService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(LoginService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String userName = parameters.get("userName");
        String password = parameters.get("password");
        logger.debug("useName={}", userName);
        logger.debug("password={}", password);
        EntityDao<Manager> userDAO = applicationContext.getEntityDAO(EntityNames.manager);
        List<Condition> conditionList = new ArrayList<Condition>(2);
        Condition userNameCondition = new Condition("userName", ConditionTypeEnum.EQUAL, userName);
        Condition passwordCondition = new Condition("password", ConditionTypeEnum.EQUAL, password);
        conditionList.add(userNameCondition);
        conditionList.add(passwordCondition);
        List<Manager> userList = userDAO.inquireByCondition(conditionList);
        if (userList != null && !userList.isEmpty()) {
            try {
                Manager manager = userList.get(0);
                HttpSession httpSession = applicationContext.getHttpSession();
                String token = httpSession.getId();
                StringBuilder content = new StringBuilder(60);
                content.append("{\"userName\":\"").append(manager.getUserName())
                        .append("\",\"token\":\"").append(token).append("\",\"userId\":\"").append(manager.getManagerId())
                        .append("\",\"orgUserId\":\"").append("-1").append("\",\"orgId\":\"").append("-1").append("\",\"orgType\":\"").append("-1")
                        .append("\",\"regionId\":\"").append("-1")
                        .append("\",\"childOrgIds\":\"").append("-1")
                        .append("\"}");
                logger.debug("content :{}", content.toString());
                String base64Content = Base64Utils.encode(content.toString().getBytes("UTF-8"));
                if (base64Content != null) {
                    HttpSession session = applicationContext.getHttpSession();
                    //注册
                    base64Content = base64Content.replace("\n", "");
                    base64Content = base64Content.replace("\r", "");
                    SessionEntity sessionEntity = new SessionEntity();
                    sessionEntity.setToken(token);
                    sessionEntity.setSession(base64Content);
                    sessionEntity.setTouchTime(System.currentTimeMillis());
                    sessionEntity.setSessionId(httpSession.getId());
                    boolean isSuccess = applicationContext.getWebSessionManager().putKey(session.getId() + "-" + manager.getManagerId(), sessionEntity);
                    if (isSuccess) {
                        //获取key
                        String key = UUID.randomUUID().toString();
                        String realKey = key.replace("-", "").substring(0, 16);
                        applicationContext.putKey(session.getId() + "-" + manager.getManagerId(), realKey);
                        logger.debug("realKey :{}", realKey);
                        Map<String, String> resultMap = new HashMap<String, String>(2, 1);
                        resultMap.put("session", base64Content);
                        resultMap.put("key", realKey);

                        applicationContext.setMapData(resultMap);
                        applicationContext.success();
                    } else {
                        applicationContext.fail();
                    }
                } else {
                    applicationContext.fail();
                }
            } catch (Exception ex) {
                logger.error("ex={}", ex);
            }
        } else {
            applicationContext.fail();
        }

    }
}
