package com.kreig133.daogenerator.db.preparators;

import com.kreig133.daogenerator.jaxb.ParameterType;
import junit.framework.Assert;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DoubleQueryPreparatorTest extends DoubleQueryPreparator{

    @Language( "SQL" )
    String query = "SELECT distinct\n" +
            " tm.imembercid, \n" +
            "ltrim(tm.sShortName) as sshortname \n" +
            "FROM dbo.tabBondKind bk with (nolock) \n" +
            "JOIN dbo.tabBondTypes bt with (nolock) ON bk.iBondKindID = bt.iBondKindID \n" +
            "JOIN dbo.tabMembers tm with (nolock) ON bt.iMemberCID = tm.iMemberCID \n" +
            "JOIN ( select ibonvalueid \n" +
            "from dbo.tabsectorbondtypes with (nolock) \n" +
            "where ivalueid = coalesce(cast(:id as integer),0) \n" +
            "union select ivalueid as ibonvalueid \n" +
            "from dbo.tabbondtypes with (nolock) \n" +
            "where coalesce(cast(:id as integer),0) = 0) ts ON bt.ivalueid = ts.ibonvalueid, \n" +
            "( select streecode, ibondkindid \n" +
            "from dbo.tabBondKind with (nolock) \n" +
            "where ibondkindid=coalesce(cast(:ibondid as integer),ibondkindid)) tk \n" +
            "WHERE dbo.SUBSTRING(bk.streecode,1, dbo.DATALENGTH(tk.streecode)) = tk.streecode \n" +
            "UNION ALL\n" +
            "SELECT cast(null as int) as imembercid, \n" +
            "'' as sshortname \n" +
            "FROM dbo.v_dummy \n" +
            "ORDER BY sshortname";

    @Language( "SQL" )
    String fiiledQuery = "SELECT distinct\n" +
            " tm.imembercid, \n" +
            "ltrim(tm.sShortName) as sshortname \n" +
            "FROM dbo.tabBondKind bk with (nolock) \n" +
            "JOIN dbo.tabBondTypes bt with (nolock) ON bk.iBondKindID = bt.iBondKindID \n" +
            "JOIN dbo.tabMembers tm with (nolock) ON bt.iMemberCID = tm.iMemberCID \n" +
            "JOIN ( select ibonvalueid \n" +
            "from dbo.tabsectorbondtypes with (nolock) \n" +
            "where ivalueid = coalesce(cast(123 as integer),0) \n" +
            "union select ivalueid as ibonvalueid \n" +
            "from dbo.tabbondtypes with (nolock) \n" +
            "where coalesce(cast(123 as integer),0) = 0) ts ON bt.ivalueid = ts.ibonvalueid, \n" +
            "( select streecode, ibondkindid \n" +
            "from dbo.tabBondKind with (nolock) \n" +
            "where ibondkindid=coalesce(cast(456 as integer),ibondkindid)) tk \n" +
            "WHERE dbo.SUBSTRING(bk.streecode,1, dbo.DATALENGTH(tk.streecode)) = tk.streecode \n" +
            "UNION ALL\n" +
            "SELECT cast(null as int) as imembercid, \n" +
            "'' as sshortname \n" +
            "FROM dbo.v_dummy \n" +
            "ORDER BY sshortname";

    @Test
    public void testParse(){
        String s = prepareQuery( query, fiiledQuery );
        System.out.println(s);
    }

    @Test
    public void testParseQueryWithName() {
        List<String> queryPiece = new ArrayList<String>();
        List<String> paramNames = new ArrayList<String>();

        parseQueryWithName( queryPiece, paramNames, query );

        Assert.assertEquals( paramNames.size(), 3 );
    }

    @Test
    public void testDetermineSqlTypeByTestValue() {
        testDetermineSqlTypeByTestValue( "'3-22-2000 0:0:0.000'", "datetime" );
        testDetermineSqlTypeByTestValue( "-199239818328939", "int" );
        testDetermineSqlTypeByTestValue( "99239818328939", "int" );
        testDetermineSqlTypeByTestValue( "-99239818328939.000", "numeric" );
        testDetermineSqlTypeByTestValue( "99239818328939.000", "numeric" );
        testDetermineSqlTypeByTestValue( "''", "varchar" );
        testDetermineSqlTypeByTestValue( "''", "varchar" );
    }

    private void testDetermineSqlTypeByTestValue( String testValue, String sqlType ) {
        ParameterType parameterType = new ParameterType();
        parameterType.setTestValue( testValue );
        determineSqlTypeByTestValue( parameterType );

        Assert.assertEquals( parameterType.getSqlType(), sqlType );
    }
}