package com.apptreasure.manager.service;

import com.apptreasure.service.*;
import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.config.AppTreasureErrorCode;
import com.apptreasure.entity.EntityNames;
import com.apptreasure.entity.Product;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.entity.dao.EntityDao;
import com.framework.entity.pojo.PrimaryKey;
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
        act = AppTreasureActionNames.deleteProduct,
        importantParameters = {"productId", "session", "encryptType"},
        requestEncrypt = CryptEnum.AES,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.NO_DATA_JSON,
        terminalType = TerminalTypeEnum.WEB,
        requireLogin = LoginEnum.REQUIRE,
        description = "删除商品(后台管理)",
        validateParameters = {
    @Field(fieldName = "productId", fieldType = FieldTypeEnum.BIG_INT, description = "商品id"),
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class DeleteProductService implements Service {

    private Logger logger = LogFactory.getInstance().getLogger(DeleteAddressService.class);

    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        Map<String, String> parameters = applicationContext.getSimpleMapParameters();
        logger.debug("parameters={}", parameters);
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.putKeyField("productId", parameters.get("productId"));
        EntityDao<Product> productDAO = applicationContext.getEntityDAO(EntityNames.product);
        boolean isSuccess = productDAO.delete(primaryKey);
        if (isSuccess) {
            String deleteSql = "delete from productImage where productId=" + parameters.get("productId");
            boolean isDelete = productDAO.executeUpdateBySql(new String[]{"productImage"}, deleteSql, new String[]{}, parameters);
            if (isDelete) {
                applicationContext.success();
            } else {
                throw new RollBackException("删除商品失败", AppTreasureErrorCode.FAIL_CODE);
            }
        } else {
            throw new RollBackException("删除商品失败", AppTreasureErrorCode.FAIL_CODE);
        }
    }
}
