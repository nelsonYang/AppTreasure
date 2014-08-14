package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.ExchangeFlow;
import com.apptreasure.entity.Product;
import com.apptreasure.entity.ProductImage;
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
import com.framework.utils.JsonUtils;
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
        act = AppTreasureActionNames.inquirePageExchangeFlowList,
        importantParameters = {"session", "pageCount", "pageNo", "encryptType"},
        minorParameters = {"type"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.MAP_DATA_LIST_JSON_PAGE,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"userId", "userName", "payAccount", "exchangeFlowId", "productId", "productName", "createTime", "price", "integration", "images", "processStatus"},
        description = "查询预订商品的类表(后台管理)",
        validateParameters = {
    @Field(fieldName = "pageCount", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "pageNo", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "type", fieldType = FieldTypeEnum.TYINT, description = "是否已经兑换2-已经兑换，1-未兑换 没有输入全部查询"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquirePageExchangeFlowListService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireEventQualificationListService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String pageCount = parameters.get("pageCount");
        String pageNo = parameters.get("pageNo");
        String type = parameters.get("type");
        EntityDao<ExchangeFlow> exchangeFlowDAO = applicationContext.getEntityDAO(EntityNames.exchangeFlow);
        List<Condition> conditionList = new ArrayList<Condition>(1);
        if ("1".equals(type)) {
            Condition isExchangeCondition = new Condition("processStatus", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.ALREAD_EXCHAGE));
            conditionList.add(isExchangeCondition);
        } else {
            Condition isExchangeCondition = new Condition("processStatus", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.UN_ALREAD_EXCHAGE));
            conditionList.add(isExchangeCondition);
        }
        List<Order> orderList = new ArrayList<Order>(1);
        Order createTimeOrder = new Order("createTime", OrderEnum.DESC);
        orderList.add(createTimeOrder);
        PageModel productPageMode = exchangeFlowDAO.inquirePageByCondition(conditionList, Integer.parseInt(pageNo), Integer.parseInt(pageCount), orderList);
        List<ExchangeFlow> eventQualificationList = productPageMode.getDataList();
        if (eventQualificationList != null) {
            if (!eventQualificationList.isEmpty()) {
                EntityDao<User> userDAO = applicationContext.getEntityDAO(EntityNames.user);
                Set<Long> userIdSet = new HashSet<Long>(eventQualificationList.size());
                for (ExchangeFlow exchangeFlow : eventQualificationList) {
                    userIdSet.add(exchangeFlow.getUserId());
                }
                List<Condition> userConditionList = new ArrayList<Condition>(1);
                Condition userCondition = new Condition("userId", ConditionTypeEnum.IN, SetUtils.LongSet2Str(userIdSet));
                userConditionList.add(userCondition);
                List<User> userList = userDAO.inquireByCondition(userConditionList);

                Set<Long> productSet = new HashSet<Long>(eventQualificationList.size());
                EntityDao<Product> entityDAO = applicationContext.getEntityDAO(EntityNames.product);
                List<Map<String, String>> mapList = new ArrayList<Map<String, String>>(eventQualificationList.size());
                for (ExchangeFlow exchangeFlow : eventQualificationList) {
                    productSet.add(exchangeFlow.getProductId());
                }
                List<Condition> productConditonList = new ArrayList<Condition>(1);
                Condition productIdCondition = new Condition("productId", ConditionTypeEnum.IN, SetUtils.LongSet2Str(productSet));
                productConditonList.add(productIdCondition);
                List<Product> productList = entityDAO.inquireByCondition(productConditonList);

                EntityDao<ProductImage> productImageDAO = applicationContext.getEntityDAO(EntityNames.productImage);
                List<Condition> productImageConditonList = new ArrayList<Condition>(1);
                productIdCondition = new Condition("productId", ConditionTypeEnum.IN, SetUtils.LongSet2Str(productSet));
                Condition typeCondition = new Condition("type", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.SAMLL_IMAGE_TYPE));
                productImageConditonList.add(typeCondition);
                productImageConditonList.add(productIdCondition);
                List<ProductImage> productImageList = productImageDAO.inquireByCondition(productImageConditonList);
                Map<String, String> productMap = null;
                List<Map<String, String>> imageMapList;
                Map<String, String> eventQualificationMap;
                Map<String, String> imageMap;
                for (ExchangeFlow exchangeFlow : eventQualificationList) {
                    eventQualificationMap = exchangeFlow.toMap();
                    for (Product product : productList) {
                        if (product.getProductId() == exchangeFlow.getProductId()) {
                            productMap = product.toMap();
                            productMap.put("images", "[]");
                            imageMapList = new ArrayList<Map<String, String>>(0);
                            for (ProductImage productImage : productImageList) {
                                if (productImage.getProductId() == product.getProductId()) {
                                    imageMap = new HashMap<String, String>(4, 1);
                                    imageMap.put("smallImage", productImage.getProductImageURL());
                                    imageMapList.add(imageMap);
                                }
                            }
                            if (!imageMapList.isEmpty()) {
                                String imageJson = JsonUtils.mapListToJsonArray(imageMapList);
                                productMap.put("images", imageJson);
                            }
                            productMap.remove("createTime");
                            eventQualificationMap.putAll(productMap);
                        }

                    }
                    eventQualificationMap.put("userName", "");
                    eventQualificationMap.put("payAccount", "");
                    for (User user : userList) {
                        if (user.getUserId() == exchangeFlow.getUserId()) {
                            eventQualificationMap.put("userName", user.getUserName());
                            eventQualificationMap.put("payAccount", user.getPayAccount());
                            break;
                        }
                    }

                    mapList.add(eventQualificationMap);
                }
                Map<String, List<Map<String, String>>> dataMapList = new HashMap<String, List<Map<String, String>>>(2, 1);
                dataMapList.put("productList", mapList);
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
