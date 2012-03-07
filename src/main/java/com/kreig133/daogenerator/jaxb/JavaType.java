//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.06 at 03:26:45 PM YEKT 
//


package com.kreig133.daogenerator.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.*;

/**
 * <p>Java class for javaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="javaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Long"/>
 *     &lt;enumeration value="String"/>
 *     &lt;enumeration value="Date"/>
 *     &lt;enumeration value="Double"/>
 *     &lt;enumeration value="Byte"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "javaType")
@XmlEnum
public enum JavaType {

    @XmlEnumValue("Long")
    LONG("Long"),
    @XmlEnumValue("String")
    STRING("String"),
    @XmlEnumValue("Date")
    DATE("Date"),
    @XmlEnumValue("Double")
    DOUBLE("Double"),
    @XmlEnumValue("Byte")
    BYTE("Byte");
    private final String value;

    JavaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static JavaType fromValue(String v) {
        for (JavaType c: JavaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


    private static final Map< JavaType, List<String> > map = new HashMap<JavaType, List<String>>( 6 );

    static {
        linkJavaTypeWithAliases( LONG   , "int", "byte", "long", "smallint", "tinyint", "bigint" );
        linkJavaTypeWithAliases( BYTE   , "bit" );
        linkJavaTypeWithAliases( DATE   , "datetime" );
        linkJavaTypeWithAliases( STRING , "string", "varchar", "char", "nvarchar", "text" );
        linkJavaTypeWithAliases( DOUBLE , "decimal", "numeric" );
    }

    public static JavaType getBySqlType( String type ) {

        for( JavaType javaType: JavaType.values() ){
            for( java.lang.String typeAllias : map.get( javaType ) ){
                if( type.startsWith( typeAllias ) ){
                    return javaType;
                }
            }
        }

        throw new RuntimeException( "Упс! Нашелся баг: я не шмогла преобразовать входной тип в java-тип ( "
                +type
                +" ). Ай-я-я-яй"
        );
    }

    private static void linkJavaTypeWithAliases(JavaType type, String...aliases){
        List<String> listOfStrings = new ArrayList<String>( aliases.length );
        Collections.addAll( listOfStrings, aliases );
        map.put( type, listOfStrings );
    }

}
