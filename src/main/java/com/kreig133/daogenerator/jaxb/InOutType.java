//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.13 at 10:35:51 AM YEKT
//


package com.kreig133.daogenerator.jaxb;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for inOutType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="inOutType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IN"/>
 *     &lt;enumeration value="OUT"/>
 *     &lt;enumeration value="INOUT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "inOutType")
@XmlEnum
public enum InOutType {

    IN,
    OUT,
    INOUT;

    public String value() {
        return name();
    }

    public static InOutType fromValue(String v) {
        return valueOf(v);
    }

    @NotNull
    public static InOutType getByName( @NotNull String name ) {
        for( InOutType inputOrOutputType : InOutType.values() ){
            if( inputOrOutputType.toString().equalsIgnoreCase( name.trim() )){
                return inputOrOutputType;
            }
        }
        throw  new IllegalArgumentException();
    }
}
