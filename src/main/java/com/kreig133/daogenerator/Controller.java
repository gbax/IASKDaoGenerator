package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettingsImpl;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.InOutClass;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.daogenerator.files.parsers.InputFileParser;
import com.kreig133.daogenerator.files.parsers.settings.SettingsReader;
import com.kreig133.daogenerator.gui.MainForm;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.settings.PropertiesFileController;
import com.kreig133.daogenerator.sql.ProcedureCallCreator;
import com.kreig133.daogenerator.sql.SelectQueryConverter;
import com.kreig133.daogenerator.sql.wrappers.GenerateGenerator;
import com.kreig133.daogenerator.sql.wrappers.GeneroutGenerator;
import com.kreig133.daogenerator.testing.Tester;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.kreig133.daogenerator.settings.SettingName.*;
import static com.kreig133.daogenerator.common.Utils.checkToNeedOwnInClass;
import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Controller {

    private static final List<FunctionSettings> settingsList = new ArrayList<FunctionSettings>();

    public static void doAction() {

        final OperationSettings operationSettings = DaoGenerator.getCurrentOperationSettings();

        saveProperties( operationSettings );

        try {
            MyBatis.prepareFiles( operationSettings );

            SettingsReader.readProperties( operationSettings );

        } catch ( Throwable e ) {
            System.err.println( ">>>Controller: Ошибка! При предварительной записи в файлы или " +
                    "считывание настроек, произошла ошибка!" );
            e.printStackTrace();
        }

        for(
                String s:
                    ( new File( operationSettings.getSourcePath() ) )
                            .list(
                                    new FilenameFilter() {
                                        public boolean accept( File dir, String name ) {
                                            return name.endsWith( "txt" );
                                        }
                                    }
                            )
        ) {
            Controller.readFile( Utils.getFileFromDirectoryByName(operationSettings.getSourcePath(), s ), operationSettings );
        }

        createQueries();

        try {
            if( !operationSettings.skipTesting() ){
                for( FunctionSettings functionSettings: settingsList ){
                    Tester.startFunctionTesting( operationSettings, functionSettings );
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace(); 
        }

        try {
            writeFiles( operationSettings );
            MyBatis.closeFiles( operationSettings );
        } catch ( IOException e ) {
            System.err.println( ">>>Controller: Ошибка! При записи в файлы, произошла ошибка!" );
            throw new RuntimeException( e );
        }
    }

    private static void createQueries() {
        for( FunctionSettings settings : settingsList ){
            switch ( settings.getSelectType() ){
                case CALL:
                    ProcedureCallCreator.generateProcedureCall( settings );
                    break;
                case GENERATE:
                    GenerateGenerator.generateWrapper( settings );
                    break;
                case GENEROUT:
                    GeneroutGenerator.generateWrapper( settings );
                    break;
                default:
                    SelectQueryConverter.processSelectQueryString( settings );
                    break;
            }
        }
    }

    private static void readFile(
            File fileWithData,
            OperationSettings operationSettings
    ) {
        try {
            FunctionSettings currentSettings = new FunctionSettingsImpl( operationSettings );

            settingsList.add( currentSettings );

            //считываем название из файла ( название файла = название хранимки, запроса )
            currentSettings.setName( fileWithData.getName().split( ".txt" )[ 0 ] );

            InputFileParser.readFileWithDataForGenerateDao( fileWithData, operationSettings, currentSettings );
        } catch ( Throwable e ) {
            System.err.println( ">>>Controller: Ошибка! Файл - " + fileWithData.getName() );
            e.printStackTrace();
        }
    }

    private static void writeFiles(
        OperationSettings operationSettings
    ) throws IOException {

        for ( FunctionSettings settings: settingsList ) {
            if ( checkToNeedOwnInClass( operationSettings, settings ) ) {
                createJavaClassForInputOutputEntities( operationSettings, settings, InOutType.IN );
            }

            if ( settings.getOutputParameterList().size() > 1 ) {
                createJavaClassForInputOutputEntities( operationSettings, settings, InOutType.OUT );
            }

            MyBatis.generateFiles( operationSettings, settings );
        }
    }

    private static void createJavaClassForInputOutputEntities(
            OperationSettings operationSettings,
            FunctionSettings functionSettings,
            InOutType type
    ) throws IOException {

        FileWriter writer = null;
        try {
            InOutClass inOutClass = new InOutClass(
                    operationSettings.getEntityPackage(),
                    type == InOutType.IN ? functionSettings.getInputParameterList(): functionSettings.getOutputParameterList(),
                    Utils.convertNameForClassNaming( functionSettings.getName() ) +
                            ( type == InOutType.IN ? "In" : "Out" )
            );

            File inClassFile = getInOrOutClassFile( operationSettings, inOutClass );
            inClassFile.createNewFile();

            writer = new FileWriter(inClassFile);
            writer.write(inOutClass.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    private static void saveProperties( OperationSettings operationSettings ) {
        Properties properties = new Properties();

        properties.setProperty( SKIP_TESTS          , String.valueOf( operationSettings.skipTesting() ) );

        properties.setProperty( IASK                , String.valueOf( operationSettings.getType() == Type.IASK ) );
        properties.setProperty( DEPO                , String.valueOf( operationSettings.getType() == Type.DEPO ) );

        properties.setProperty( WIDTH               , String.valueOf( ( int ) MainForm.getInstance().getSize().getWidth () ) );
        properties.setProperty( HEIGHT              , String.valueOf( ( int ) MainForm.getInstance().getSize().getHeight() ) );

        properties.setProperty( DEST_DIR            , operationSettings.getOutputPath   () );
        properties.setProperty( ENTITY_PACKAGE      , operationSettings.getEntityPackage() );
        properties.setProperty( INTERFACE_PACKAGE   , operationSettings.getDaoPackage   () );
        properties.setProperty( MAPPING_PACKAGE     , operationSettings.getMapperPackage() );

        PropertiesFileController.saveSpecificProperties( operationSettings.getSourcePath(), properties );

        properties.setProperty( SOURCE_DIR          , operationSettings.getSourcePath() );

        PropertiesFileController.saveCommonProperties( properties );

    }
}