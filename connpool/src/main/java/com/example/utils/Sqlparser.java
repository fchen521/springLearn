package com.example.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;

/**
 * sql解析工具类Sqlparser 和druid 都可以用来解析sql
 */
public class Sqlparser {
    public static void sql(String sql) throws JSQLParserException {
        Statement stmt = CCJSqlParserUtil.parse(sql);
        List<String> list = new TablesNamesFinder().getTableList(stmt);
        System.out.println(list.size());

    }

    public static void main(String[] args) throws JSQLParserException {
        /*Sqlparser.sql("select  \n" +
                "'OP'                  as    OPorIP,          \n" +
                "nvl(mdm.name2,'其他')                as    SecondDeptnam  ,    \n" +
                "nvl(mdm.name3,'其他')                as    ThirdDeptname  ,    \n" +
                "rtrim(f1.ksdm)              as    deptcode  ,        \n" +
                "rtrim(f1.ksmc)              as    deptname  ,        \n" +
                "rtrim(a.YSDM)              as    doctorcode  ,      \n" +
                "rtrim(g.YGXM)              as    doctorname  ,      \n" +
                "cast('其他' as string)          as    wardcode  ,        \n" +
                "cast('其他' as string)          as    wardname  ,        \n" +
                "case when a.thbz='0' then \n" +
                " substring(rtrim(a.ghsj),1,7)\n" +
                "when a.thbz='1' then\n" +
                " substring(rtrim(th.THRQ),1,7) end    as    chargedate   ,      \n" +
                "nvl(mdm2.str1,'其他')                as    itemcode  ,        \n" +
                "nvl(mdm2.str2,'其他')                as    itemname  ,        \n" +
                "nvl(mdm2.chargecategoryparentcode,'其他')      as    chargecategorycode  , \n" +
                "nvl(mdm2.chargecategoryparentname,'其他')      as    chargecategoryname  , \n" +
                "'10'                        as    itemtypecode  ,      \n" +
                "mdm2.chargecategoryname                as    itemtypename  ,      \n" +
                "\n" +
                "sum(case when a.thbz='0' then (cast(a.ghje as decimal(20,4))+\n" +
                "cast(a.zlje as decimal(20,4)))\n" +
                "when a.thbz='1' then -(cast(a.ghje as decimal(20,4))+\n" +
                "cast(a.zlje as decimal(20,4)))  end  )  as    totalmoney  ,   \n" +
                "cast( 0 as int)              as      PatientCount   \n" +
                " from        his_repl.ms_ghmx a\n" +
                "    left join his_repl.ms_brda b on a.brid=b.brid\n" +
                "    left join his_repl.gy_brxz c on a.brxz=c.brxz\n" +
                "    left join his_repl.ms_thmx th on a.sbxh=th.sbxh \n" +
                "    left join his_repl.MS_GHKS f on a.KSDM=f.KSDM  \n" +
                "    left join his_repl.GY_KSDM f1 on f1.KSDM=f.MZKS  \n" +
                "    left join his_repl.mdept_vs_hisdept_1 mdm on f1.ksdm=mdm.ksdm \n" +
                "    left join his_repl.mdm_chargeitemcatalog mdm2 on mdm2.chargecategorycode='10'\n" +
                "    left join his_repl.gy_ygdm g on a.YSDM=g.YGDM\n" +
                "    left join his_repl.gy_ygdm g1 on a.czgh=g1.YGDM\n" +
                "group by 'OP',mdm.name2,mdm.name3,rtrim(a.YSDM),rtrim(g.YGXM),rtrim(f1.ksdm),rtrim(f1.ksmc),\n" +
                "case when a.thbz='0' then \n" +
                " substring(rtrim(a.ghsj),1,7)\n" +
                "when a.thbz='1' then\n" +
                " substring(rtrim(th.THRQ),1,7) end,mdm2.str1,mdm2.str2,mdm2.chargecategoryparentcode,mdm2.chargecategoryparentname,\n" +
                " '10',mdm2.chargecategoryname");*/
    }
}
