package com.apptreasure.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureConstant;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.Account;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.Product;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.entity.condition.Condition;
import com.framework.entity.condition.Order;
import com.framework.entity.dao.EntityDao;
import com.framework.entity.enumeration.ConditionTypeEnum;
import com.framework.entity.enumeration.OrderEnum;
import com.framework.entity.pojo.PrimaryKey;
import com.framework.enumeration.CryptEnum;
import com.framework.enumeration.FieldTypeEnum;
import com.framework.enumeration.LoginEnum;
import com.framework.enumeration.ParameterWrapperTypeEnum;
import com.framework.enumeration.ResponseTypeEnum;
import com.framework.enumeration.TerminalTypeEnum;
import com.framework.service.api.Service;
import com.frameworkLog.factory.LogFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
@ServiceConfig(
        act = AppTreasureActionNames.inquireConsumeChannelByProduct,
        importantParameters = {"eventQualificationId", "productId", "num", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.MAP_DATA_LIST_JSON,
        terminalType = TerminalTypeEnum.MOBILE,
        requireLogin = LoginEnum.REQUIRE,
        returnParameters = {"channel", "amount"},
        description = "兑换商品查询扣钱的渠道",
        validateParameters = {
    @Field(fieldName = "num", fieldType = FieldTypeEnum.TYINT, description = "兑换数量"),
    @Field(fieldName = "eventQualificationId", fieldType = FieldTypeEnum.BIG_INT, description = "预订id"),
    @Field(fieldName = "productId", fieldType = FieldTypeEnum.BIG_INT, description = "商品id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class InquireConsumeChannelByProductService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(InquireConsumeChannelByProductService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        long userId = applicationContext.getUserId();
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        String productId = parameters.get("productId");
        String numStr = parameters.get("num");
        int num = Integer.parseInt(numStr);
        logger.debug("parameters={}", parameters);
        EntityDao<Account> accountDAO = applicationContext.getEntityDAO(EntityNames.account);
        EntityDao<Product> entityDAO = applicationContext.getEntityDAO(EntityNames.product);
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("productId", productId);
        primaryKey.putKeyField("userId", String.valueOf(userId));
        Product product = entityDAO.inqurieByKey(primaryKey);
        if (product != null) {
            if (product.getProductNum() >= num) {
                List<Condition> conditionList = new ArrayList<Condition>(2);
                Condition userIdCondition = new Condition("userId", ConditionTypeEnum.EQUAL, String.valueOf(userId));
                conditionList.add(userIdCondition);
                Condition statusCondition = new Condition("status", ConditionTypeEnum.EQUAL, String.valueOf(AppTreasureConstant.EFFECTIVE_STATUS));
                conditionList.add(statusCondition);
                List<Order> orderList = new ArrayList<Order>(1);
                Order balanceOrder = new Order("balance", OrderEnum.DESC);
                orderList.add(balanceOrder);
                List<Account> accountList = accountDAO.inquireByCondition(conditionList, orderList);
                long balance = 0;
                for (Account account : accountList) {
                    balance = balance + account.getBalance();
                }
                if (accountList != null && !accountList.isEmpty()) {
                    long productIntegeration = product.getIntegration() * num;
                    if (balance >= productIntegeration) {
                        long accountBalance = 0;
                        List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>();

                        Map<String, String> resultMap = new HashMap<String, String>(2, 1);
                        resultMap.put("channel", String.valueOf(AppTreasureConstant.DOU_MENG_TYPE));
                        resultMap.put("amount", String.valueOf(productIntegeration));
                        resultMapList.add(resultMap);
//                      Map<String, String> resultMap;
//                        for (Account account : accountList) {
//                            if (productIntegeration > 0) {
//                                accountBalance = account.getBalance();
//
//                                resultMap = new HashMap<String, String>(2, 1);
//                                if (productIntegeration >= accountBalance) {
//                                    resultMap.put("channel", String.valueOf(account.getType()));
//                                    resultMap.put("amount", String.valueOf(accountBalance));
//                                    resultMapList.add(resultMap);
//                                    productIntegeration = productIntegeration - accountBalance;
//                                } else {
//                                    resultMap.put("channel", String.valueOf(account.getType()));
//                                    resultMap.put("amount", String.valueOf(productIntegeration));
//                                    resultMapList.add(resultMap);
//                                    productIntegeration = productIntegeration - accountBalance;
//                                }
//                            } else {
//                                break;
//                            }
//                        }
                        Map<String, List<Map<String, String>>> dataMapList = new HashMap<String, List<Map<String, String>>>(2, 1);
                        dataMapList.put("channelList", resultMapList);
                        applicationContext.setListMapData(dataMapList);
                        applicationContext.success();
                    } else {
                        applicationContext.fail(AppTreasureErrorCode.MONEY_NOT_ENOUGH);
                    }
                } else {
                    applicationContext.fail(AppTreasureErrorCode.ACCOUNT_NOT_FOUND);
                }
            } else {
                applicationContext.fail(AppTreasureErrorCode.PRODUCT_NOT_ENOUGH);
            }
        } else {
            applicationContext.fail();
        }
    }
}
