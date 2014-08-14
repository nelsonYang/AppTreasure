package com.apptreasure.manager.service;

import com.apptreasure.config.AppTreasureActionNames;
import com.apptreasure.service.DeleteAddressService;
import com.framework.annotation.Field;
import com.framework.annotation.ServiceConfig;
import com.framework.context.ApplicationContext;
import com.framework.context.InvocationContext;
import com.framework.entity.FileEntity;
import com.framework.enumeration.CryptEnum;
import com.framework.enumeration.FieldTypeEnum;
import com.framework.enumeration.ParameterWrapperTypeEnum;
import com.framework.enumeration.ResponseTypeEnum;
import com.framework.enumeration.TerminalTypeEnum;
import com.framework.enumeration.UploadEnum;
import com.framework.service.api.Service;
import com.framework.utils.FileUtils;
import com.frameworkLog.factory.LogFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
@ServiceConfig(
        act = AppTreasureActionNames.uploadPhoto,
        importantParameters = {"session", "encryptType"},
        requestEncrypt = CryptEnum.PLAIN,
        parametersWrapperType = ParameterWrapperTypeEnum.SIMPLE_MAP,
        returnParameters = {"filePath"},
        responseEncrypt = CryptEnum.PLAIN,
        responseType = ResponseTypeEnum.MAP_DATA_JSON,
        description = "上传图片的接口(后台管理)",
        terminalType = TerminalTypeEnum.WEB,
        upload = UploadEnum.YES,
         validateParameters = {
    @Field(fieldName = "session", fieldType = FieldTypeEnum.CHAR1024, description = "session信息"),
    @Field(fieldName = "encryptType", fieldType = FieldTypeEnum.TYINT, description = "加密类型")
})
public class UploadPhotoService implements Service {

      private org.slf4j.Logger logger = LogFactory.getInstance().getLogger(DeleteAddressService.class);

    @Override
    public void execute() {
        ApplicationContext applicationContext = ApplicationContext.CTX;
        InvocationContext invocationContext = applicationContext.getInvocationContext();
        List<FileEntity> fileList = invocationContext.getFileList();
        String filePath = "";
        if (fileList != null && !fileList.isEmpty()) {
            String uploadDir = applicationContext.getUploadDir();
            FileEntity fileEntity = fileList.get(0);
            filePath = FileUtils.writeFileToDir(fileEntity.getContents(), fileEntity.getFileName(), fileEntity.getFileName(), uploadDir);
            
        }
        logger.debug("filePath=".concat(filePath));
        Map<String,String> resultMap = new HashMap<String,String>(2,1);
        resultMap.put("filePath", filePath);
        applicationContext.setMapData(resultMap);
        applicationContext.success();
    }
}
