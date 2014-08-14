package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.entity.DrawAccountFlow;
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
        act = AppTreasureActionNames.inquirePageDrawFlowList,
        importantParameters = {"session", "pageCount", "pageNo", "encryptType"},
        minorParameters = {"type"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.MAP_DATA_LIST_JSON_PAGE,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"drawAccountFlowId", "userId", "userName", "drawWay", "payAccount", "createTime", "processStatus", "drawAmount"},
        description = "查询提现记录类表(后台管理)",
        validateParameters = {
    @Field(fieldName = "pageCount", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "pageNo", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "type", fieldType = FieldTypeEnum.TYINT, description = "是否已经兑换2-已经兑换，1-未兑换 没有输入全部查询"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquirePageDrawMoneyFlowListService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireDrawMoneyFlowListService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
      String type = parameters.get("type");
        List<Condition> conditionList = new ArrayList<Condition>(1);
        if ("1".equals(type)) {
            Condition isExchangeCondition = new Condition("processStatus", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.PROCESS_STATUS));
            conditionList.add(isExchangeCondition);
        } else {
            Condition isExchangeCondition = new Condition("processStatus", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.UN_PROCESS_STATUS));
            conditionList.add(isExchangeCondition);
        }
        String pageCount = parameters.get("pageCount");
        String pageNo = parameters.get("pageNo");
        EntityDao<DrawAccountFlow> drawAccountFlowDAO = applicationContext.getEntityDAO(EntityNames.drawAccountFlow);
       List<Order> orderList = new ArrayList<Order>(1);
        Order createTimeOrder = new Order("createTime", OrderEnum.DESC);
        orderList.add(createTimeOrder);
        PageModel drawMoneyPageMode = drawAccountFlowDAO.inquirePageByCondition(conditionList, Integer.parseInt(pageNo), Integer.parseInt(pageCount), orderList);
        List<DrawAccountFlow> rewardFlowList = drawMoneyPageMode.getDataList();
        if (rewardFlowList != null) {
            if (!rewardFlowList.isEmpty()) {
                EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user);
                Set<Long> userIdSet = new HashSet<Long>(rewardFlowList.size());
                for (DrawAccountFlow drawAccountFlow : rewardFlowList) {
                    userIdSet.add(drawAccountFlow.getUserId());
                }
                List<Condition> userConditionList = new ArrayList<Condition>(1);
                Condition userCondition = new Condition("userId", ConditionTypeEnum.IN, SetUtils.LongSet2Str(userIdSet));
                userConditionList.add(userCondition);
                List<User> userList = userDAO.inquireByCondition(userConditionList);
                List<Map<String, String>> mapList = new ArrayList<Map<String, String>>(rewardFlowList.size());
                Map<String, String> drawAccountMap;
                for (DrawAccountFlow drawAccountFlow : rewardFlowList) {
                    drawAccountMap = drawAccountFlow.toMap();
                    drawAccountMap.put("userName", "");
                    for (User user : userList) {
                        if (user.getUserId() == drawAccountFlow.getUserId()) {
                            drawAccountMap.put("userName", user.getUserName());
                            break;
                        }
                    }
                    mapList.add(drawAccountMap);
                }
                Map<String, List<Map<String, String>>> dataMapList = new HashMap<String, List<Map<String, String>>>(2, 1);
                dataMapList.put("drawMoneyList", mapList);
                applicationContext.setListMapData(dataMapList);
                applicationContext.setCount(drawMoneyPageMode.getTotalCount());
                applicationContext.setTotalPage(drawMoneyPageMode.getTotalPage());
                applicationContext.success();
            } else {
                applicationContext.noData();
            }
        } else {
            applicationContext.fail();
        }

    }
}
