package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Type;

import java.io.IOException;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MappingFilePreparatory extends Preparatory{

    public static void prepareFile( OperationSettings operationSettings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        if( operationSettings.getType() == Type.DEPO ){
            insertPackageLine( operationSettings.getMapperPackage(), builder );
            commonImports( operationSettings, builder );

            insertImport( builder, "java.util.*" );
            insertImport( builder, "org.apache.ibatis.annotations.*" );
            //TODO блок комментариев
            insertClassDeclaration(
                    ClassType.Interface,
                    builder,
                    mapperFileName( operationSettings ),
                    null,
                    null
            );
        } else {
            builder.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
            builder.append( "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis" +
                    ".org/dtd/mybatis-3-mapper.dtd\">\n" );
            builder.append( "<mapper namespace=\"" ).append( operationSettings.getDaoPackage() ).append( "." )
                    .append( interfaceFileName( operationSettings ) ).append( "\">\n" );
        }
        Utils.appendByteToFile( mappingFile( operationSettings ), builder.toString().getBytes() );
    }
}
