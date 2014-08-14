package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.entity.Account;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.User;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.entity.condition.Condition;
import com.framework.entity.condition.Order;
import com.framework.entity.dao.EntityDao;
import com.framework.entity.enumeration.ConditionTypeEnum;
import com.framework.entity.enumeration.OrderEnum;
import com.framework.entity.pojo.PageModel;
import com.framework.enumeration.CryptEnum;
import com.framework.enumeration.FieldTypeEnum;
import com.framework.enumeration.LoginEnum;
import com.framework.enumeration.ParameterWrapperTypeEnum;
import com.framework.enumeration.ResponseTypeEnum;
import com.framework.enumeration.TerminalTypeEnum;
import com.framework.service.api.Service;
import com.framework.utils.SetUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.inquirePageAccountList,
        importantParameters = {"session", "pageCount", "pageNo", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.AES,
        responseType = ResponseTypeEnum.MAP_DATA_LIST_JSON_PAGE,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"accountId", "accountName", "accountNO", "balance", "type", "status", "userName", "userId"},
        description = "查询帐号的类表(后台管理)",
        validateParameters = {
    @Field(fieldName = "pageCount", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "pageNo", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquirePageAccountListService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireProductListService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String pageCount = parameters.get("pageCount");
        String pageNo = parameters.get("pageNo");
        EntityDao<Account> entityDAO = applicationContext.getEntityDAO(EntityNames.account);
        List<Condition> conditionList = new ArrayList<Condition>(1);
        List<Order> orderList = new ArrayList<Order>(1);
        Order createTimeOrder = new Order("createTime", OrderEnum.DESC);
        orderList.add(createTimeOrder);
        PageModel productPageMode = entityDAO.inquirePageByCondition(conditionList, Integer.parseInt(pageNo), Integer.parseInt(pageCount), orderList);
        List<Account> accountList = productPageMode.getDataList();
        if (accountList != null) {
            if (!accountList.isEmpty()) {
                EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user);
                Set<Long> userIdSet = new HashSet<Long>(accountList.size());
                List<Map<String, String>> mapList = new ArrayList<Map<String, String>>(accountList.size());
                for (Account account : accountList) {
                    userIdSet.add(account.getUserId());
                }
                List<Condition> userConditionList = new ArrayList<Condition>(1);
                Condition userCondition = new Condition("userId", ConditionTypeEnum.IN, SetUtils.LongSet2Str(userIdSet));
                userConditionList.add(userCondition);
                List<User> userList = userDAO.inquireByCondition(userConditionList);
                Map<String, String> accountMap;
                for (Account account : accountList) {
                    accountMap = account.toMap();
                    for (User user : userList) {
                        if (user.getUserId() == account.getUserId()) {
                            accountMap.put("userName", user.getUserName());
                            break;
                        }
                    }
                    mapList.add(accountMap);
                }
                Map<String, List<Map<String, String>>> dataMapList = new HashMap<String, List<Map<String, String>>>(2, 1);
                dataMapList.put("drawMoneyList", mapList);
                applicationContext.setListMapData(dataMapList);
                applicationContext.setCount(productPageMode.getTotalCount());
                applicationContext.setTotalPage(productPageMode.getTotalPage());
                applicationContext.success();
            } else {
                applicationContext.noData();
            }
        } else {
            applicationContext.fail();
        }

    }
}
