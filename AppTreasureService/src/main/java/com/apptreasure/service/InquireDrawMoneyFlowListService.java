package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.entity.DrawAccountFlow;
import com.apptreasure.entity.EntityNames;
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
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/**
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.inquireDrawMoneyFlow,
        importantParameters = {"session", "pageCount", "pageNo", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.AES,
        responseType = ResponseTypeEnum.ENTITY_LIST_JSON_PAGE,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"drawWay", "payAccount", "createTime", "processStatus", "drawAmount"},
        description = "查询提现记录类表",
        validateParameters = {
    @Field(fieldName = "pageCount", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "pageNo", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquireDrawMoneyFlowListService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireDrawMoneyFlowListService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        long userId = applicationContext.getUserId();
        String pageCount = parameters.get("pageCount");
        String pageNo = parameters.get("pageNo");
      EntityDao<DrawAccountFlow> drawAccountFlowDAO = applicationContext.getEntityDAO(EntityNames.drawAccountFlow);
        List<Condition> conditionList = new ArrayList<Condition>(1);
        Condition userCondition = new Condition("userId",ConditionTypeEnum.EQUAL,String.valueOf(userId));
        conditionList.add(userCondition);
        List<Order> orderList = new ArrayList<Order>(1);
        Order createTimeOrder = new Order("createTime", OrderEnum.DESC);
        orderList.add(createTimeOrder);
        PageModel drawMoneyPageMode = drawAccountFlowDAO.inquirePageByCondition(conditionList, Integer.parseInt(pageNo), Integer.parseInt(pageCount), orderList);
        List<DrawAccountFlow> rewardFlowList = drawMoneyPageMode.getDataList();
        if (rewardFlowList != null) {
            if (!rewardFlowList.isEmpty()) {
                applicationContext.setEntityList(rewardFlowList);
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
