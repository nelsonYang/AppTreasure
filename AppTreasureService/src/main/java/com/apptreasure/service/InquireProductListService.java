package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.EventQualification;
import com.apptreasure.entity.Product;
import com.apptreasure.entity.ProductImage;
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
        act = AppTreasureActionNames.inquireProductList,
        importantParameters = {"session", "pageCount", "pageNo", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.AES,
        responseType = ResponseTypeEnum.MAP_DATA_LIST_JSON_PAGE,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"productId", "productName", "productNum", "productDesc", "createTime", "price", "integration", "images", "qualificationCount", "totalQualificationCount", "isExchange"},
        description = "查询商品的类表",
        validateParameters = {
    @Field(fieldName = "pageCount", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "pageNo", fieldType = FieldTypeEnum.INT, description = "分页参数"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquireProductListService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireProductListService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        long userId = applicationContext.getUserId();
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String pageCount = parameters.get("pageCount");
        String pageNo = parameters.get("pageNo");
        EntityDao<Product> entityDAO = applicationContext.getEntityDAO(EntityNames.product);
        List<Condition> conditionList = new ArrayList<Condition>(1);
        List<Order> orderList = new ArrayList<Order>(1);
        Order createTimeOrder = new Order("createTime", OrderEnum.DESC);
        orderList.add(createTimeOrder);
        PageModel productPageMode = entityDAO.inquirePageByCondition(conditionList, Integer.parseInt(pageNo), Integer.parseInt(pageCount), orderList);
        List<Product> productList = productPageMode.getDataList();
        if (productList != null) {
            if (!productList.isEmpty()) {
                Set<Long> productSet = new HashSet<Long>(productList.size());
                List<Map<String, String>> mapList = new ArrayList<Map<String, String>>(productList.size());
                for (Product product : productList) {
                    productSet.add(product.getProductId());
                }
                EntityDao<EventQualification> eventQualificationDAO = applicationContext.getEntityDAO(EntityNames.eventQualification);
                String eventQualificationSql = "select productId,count(*) num from eventQualification where productId in (" + SetUtils.LongSet2Str(productSet) + ") group by productId";
                Map<String, String> dataMap = new HashMap<String, String>(2, 1);
                dataMap.put("userId", String.valueOf(userId));
                List<Map<String, String>> productIdMapList = eventQualificationDAO.executeQueryBySql(new String[]{"eventQualification"}, eventQualificationSql, new String[]{}, dataMap);
                String eventQualificationSqlByUser = "select productId,count(*) num from eventQualification where productId in (" + SetUtils.LongSet2Str(productSet) + ") and userId = " + userId + " group by productId";
                List<Map<String, String>> productUserMapList = eventQualificationDAO.executeQueryBySql(new String[]{"eventQualification"}, eventQualificationSqlByUser, new String[]{}, dataMap);
                Condition productIdCondition = new Condition("productId", ConditionTypeEnum.IN, SetUtils.LongSet2Str(productSet));
                conditionList.add(productIdCondition);
                Condition userIdCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
                conditionList.add(userIdCondition);
                List<EventQualification> eventQualificationList = eventQualificationDAO.inquireByCondition(conditionList);
                EntityDao<ProductImage> productImageDAO = applicationContext.getEntityDAO(EntityNames.productImage);
                List<Condition> productImageConditonList = new ArrayList<Condition>(1);
                productIdCondition = new Condition("productId", ConditionTypeEnum.IN, SetUtils.LongSet2Str(productSet));
                Condition typeCondition = new Condition("type", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.SAMLL_IMAGE_TYPE));
                productImageConditonList.add(typeCondition);
                productImageConditonList.add(productIdCondition);
                List<ProductImage> productImageList = productImageDAO.inquireByCondition(productImageConditonList);
                Map<String, String> productMap;
                List<Map<String, String>> imageMapList;
                Map<String, String> imageMap;
                String totalQualificationCount;
                String qualificationCount;
                String productId;
                for (Product product : productList) {
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
                    productMap.put("qualificationCount", "0");
                    productMap.put("totalQualificationCount", "0");
                    for (Map<String, String> productIdMap : productIdMapList) {
                        productId = productIdMap.get("productId");
                        if (productId != null && Long.parseLong(productId) == product.getProductId()) {
                            totalQualificationCount = productIdMap.get("num");
                            if (totalQualificationCount != null && Integer.parseInt(totalQualificationCount) > 0) {
                                productMap.put("totalQualificationCount", totalQualificationCount);
                                break;
                            }
                        }
                    }
                    for (Map<String, String> productUserMap : productUserMapList) {
                        productId = productUserMap.get("productId");
                        if (productId != null && Long.parseLong(productId) == product.getProductId()) {
                            qualificationCount = productUserMap.get("num");
                            if (qualificationCount != null && Integer.parseInt(qualificationCount) > 0) {
                                productMap.put("qualificationCount", "1");
                                break;
                            }
                        }
                    }
                    productMap.put("isExchange", String.valueOf(AppTreasureConstant.UN_ALREAD_EXCHAGE));
//                    for (EventQualification eventQualification : eventQualificationList) {
//                        if (eventQualification.getProductId() == product.getProductId()) {
//                            productMap.put("isExchange", String.valueOf(eventQualification.getIsExchange()));
//                            break;
//                        }
//                    }
                    mapList.add(productMap);
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
