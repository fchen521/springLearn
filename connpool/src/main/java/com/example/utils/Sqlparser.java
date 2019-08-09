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
                "'OP'                  as    OPorIP,          --门诊/住院\n" +
                "nvl(mdm.name2,'其他')                as    SecondDeptnam  ,    --二级挂号科室名称\n" +
                "nvl(mdm.name3,'其他')                as    ThirdDeptname  ,    --三级挂号科室名称\n" +
                "rtrim(f1.ksdm)              as    deptcode  ,        --科室代码\n" +
                "rtrim(f1.ksmc)              as    deptname  ,        --科室名称\n" +
                "rtrim(a.YSDM)              as    doctorcode  ,      --医生代码\n" +
                "rtrim(g.YGXM)              as    doctorname  ,      --医生姓名\n" +
                "cast('其他' as string)          as    wardcode  ,        --病区代码\n" +
                "cast('其他' as string)          as    wardname  ,        --病区名称\n" +
                "case when a.thbz='0' then \n" +
                " substring(rtrim(a.ghsj),1,7)\n" +
                "when a.thbz='1' then\n" +
                " substring(rtrim(th.THRQ),1,7) end    as    chargedate   ,      --收费日期\n" +
                "nvl(mdm2.str1,'其他')                as    itemcode  ,        --项目编码\n" +
                "nvl(mdm2.str2,'其他')                as    itemname  ,        --项目名称\n" +
                "nvl(mdm2.chargecategoryparentcode,'其他')      as    chargecategorycode  ,  --收费项目类型编码...\n" +
                "nvl(mdm2.chargecategoryparentname,'其他')      as    chargecategoryname  ,  --收费项目类型\n" +
                "'10'                        as    itemtypecode  ,      --项目类型\n" +
                "mdm2.chargecategoryname                as    itemtypename  ,      --项目类型名称\n" +
                "\n" +
                "sum(case when a.thbz='0' then (cast(a.ghje as decimal(20,4))+\n" +
                "cast(a.zlje as decimal(20,4)))\n" +
                "when a.thbz='1' then -(cast(a.ghje as decimal(20,4))+\n" +
                "cast(a.zlje as decimal(20,4)))  end  )  as    totalmoney  ,    --金额（实收金额）\n" +
                "cast( 0 as int)              as      PatientCount      --人次\n" +
                " from        his_repl.ms_ghmx a\n" +
                "    left join his_repl.ms_brda b on a.brid=b.brid\n" +
                "    left join his_repl.gy_brxz c on a.brxz=c.brxz\n" +
                "    left join his_repl.ms_thmx th on a.sbxh=th.sbxh \n" +
                "    left join his_repl.MS_GHKS f on a.KSDM=f.KSDM  ----挂号科室\n" +
                "    left join his_repl.GY_KSDM f1 on f1.KSDM=f.MZKS  ----对应的科室\n" +
                "    left join his_repl.mdept_vs_hisdept_1 mdm on f1.ksdm=mdm.ksdm  ---MDM科室\n" +
                "    left join his_repl.mdm_chargeitemcatalog mdm2 on mdm2.chargecategorycode='10'\n" +
                "    left join his_repl.gy_ygdm g on a.YSDM=g.YGDM\n" +
                "    left join his_repl.gy_ygdm g1 on a.czgh=g1.YGDM\n" +
                "group by 'OP',mdm.name2,mdm.name3,rtrim(a.YSDM),rtrim(g.YGXM),rtrim(f1.ksdm),rtrim(f1.ksmc),\n" +
                "case when a.thbz='0' then \n" +
                " substring(rtrim(a.ghsj),1,7)\n" +
                "when a.thbz='1' then\n" +
                " substring(rtrim(th.THRQ),1,7) end,mdm2.str1,mdm2.str2,mdm2.chargecategoryparentcode,mdm2.chargecategoryparentname,\n" +
                " '10',mdm2.chargecategoryname");*/

        Sqlparser.sql("select NULL as checkresult,t1.emp_age as currentage,NULL as date1,NULL as\n" +
                "date2,t3.det_deptcode as  deptcode,t3.det_deptname as deptname,NULL as doctcode,NULL as\n" +
                "doctname,t1.emp_code as healthexamno,'佛山市第一人民医院' as  hospitalname,'4406000001' as hospitalno,NULL\n" +
                "as imagepath,0 as isdeleted,t3.det_com_code as itemcode,t4.com_name as  itemname,now() as\n" +
                "lastimportdttm,t1.lastupdatedttm as lastupdatedttm,NULL as meno,NULL as num1,NULL as\n" +
                " num2,t1.emp_date as operationdate,t1.emp_cardno as patientid,t1.emp_name as\n" +
                "patientname,t1.emp_phone as  phone,'tj_employee' as resourcetable,'emp_code' as\n" +
                "resourcetablekey,t1.emp_code as resourcetablekeyvalue,t2.pat_sex  as sexid,CASE t2.pat_sex WHEN\n" +
                "'1' THEN '男' WHEN '2' THEN '女' ELSE '未知' END as sexname,NULL as str1,NULL as  str2,NULL as\n" +
                "str3,NULL as str4,NULL as str5,NULL as str6,NULL as subscribedate,t1.emp_cardno as vistnumber from\n" +
                " pes_repl.tj_employee   t1 LEFT JOIN pes_repl.tj_mid_patient   t2 on t1.emp_cardno = t2.pat_cardno\n" +
                "LEFT JOIN  pes_repl.tj_employee_detail   t3 on t1.emp_code = t3.det_emp_code LEFT JOIN\n" +
                "pes_repl.tj_dict_combine   t4 on  t3.det_com_code = t4.com_code");
    }
}
