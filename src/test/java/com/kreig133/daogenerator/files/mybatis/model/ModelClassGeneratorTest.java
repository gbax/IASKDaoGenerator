package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.files.NamingUtils;
import com.kreig133.daogenerator.jaxb.ParameterType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ModelClassGeneratorTest extends ModelClassGenerator{

    @Override
    protected List<ParameterType> filter( List<ParameterType> parameter ) {
        return null;
    }

    public ModelClassGeneratorTest() {
        super( null );
    }

    @Test
    public void testConvertForEnum() throws Exception {
        Assert.assertEquals( NamingUtils.convertNameForEnum( "userGroupId" ), "USER_GROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "GroupId" ), "GROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "UGroupId" ), "UGROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "UGroupIdS" ), "UGROUP_ID_S" );
    }
}
