//package com.newland.paas.ee;
//
//import com.newland.paas.ee.installer.AssetAppDeployStepBuilder;
//import com.newland.paas.ee.vo.asset.AstUnitDependencyRsp;
//import com.newland.paas.ee.vo.asset.AstUnitRsp;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class SortAstUnitRspsTestNg {
//    void addAstUnitRsp( List<AstUnitRsp> listAstUnitRsp, String name, String[] dependNames) {
//        AstUnitRsp astUnit = new AstUnitRsp();
//        astUnit.setUnitName(name);
//
//        List<AstUnitDependencyRsp> unitDepends = new LinkedList<>();
//        for ( String dependName : dependNames) {
//            AstUnitDependencyRsp astUnitDependencyRsp = new AstUnitDependencyRsp();
//            astUnitDependencyRsp.setTargetUnitName(dependName);
//            unitDepends.add(astUnitDependencyRsp);
//        }
//        astUnit.setUnitDepends(unitDepends);
//        listAstUnitRsp.add(astUnit);
//    }
//
//    @Test
//    public void testOneDepend() throws  Exception{
//        //1 dpend 3, 2 no depend
//        List<AstUnitRsp> result;
//        {
//
//            List<AstUnitRsp> listAstUnitRsp = new LinkedList<>();
//            addAstUnitRsp(listAstUnitRsp, "1", new String[]{"3"});
//            addAstUnitRsp(listAstUnitRsp, "2", new String[]{});
//            addAstUnitRsp(listAstUnitRsp, "3", new String[]{});
//            result = AssetAppDeployStepBuilder.sortAstUnitRsps(listAstUnitRsp);
//        }
//        {
//            AstUnitRsp astUnitRspFirst = result.get(0);
//            Assert.assertEquals(astUnitRspFirst.getUnitName(), "2");
//            AstUnitRsp astUnitRspSecond = result.get(1);
//            Assert.assertEquals(astUnitRspSecond.getUnitName(), "3");
//            AstUnitRsp astUnitRspThird = result.get(2);
//            Assert.assertEquals(astUnitRspThird.getUnitName(), "1");
//        }
//    }
//
//    @Test
//    public void testNoDepend() throws  Exception{
//        List<AstUnitRsp> result;
//        {
//            List<AstUnitRsp> listAstUnitRsp = new LinkedList<>();
//            addAstUnitRsp(listAstUnitRsp, "1", new String[]{});
//            addAstUnitRsp(listAstUnitRsp, "2", new String[]{});
//            addAstUnitRsp(listAstUnitRsp, "3", new String[]{});
//            result = AssetAppDeployStepBuilder.sortAstUnitRsps(listAstUnitRsp);
//        }
//        {
//            AstUnitRsp astUnitRspFirst = result.get(0);
//            Assert.assertEquals(astUnitRspFirst.getUnitName(), "1");
//            AstUnitRsp astUnitRspSecond = result.get(1);
//            Assert.assertEquals(astUnitRspSecond.getUnitName(), "2");
//            AstUnitRsp astUnitRspThird = result.get(2);
//            Assert.assertEquals(astUnitRspThird.getUnitName(), "3");
//        }
//    }
//
//    @Test
//    public void testComplexDepend() throws  Exception{
//        //1 depend 3,4    2 dpend 3,  3 depend 4
//        List<AstUnitRsp> result;
//        {
//            List<AstUnitRsp> listAstUnitRsp = new LinkedList<>();
//            addAstUnitRsp(listAstUnitRsp, "1", new String[]{"3","4"});
//            addAstUnitRsp(listAstUnitRsp, "2", new String[]{"3"});
//            addAstUnitRsp(listAstUnitRsp, "3", new String[]{"4"});
//            addAstUnitRsp(listAstUnitRsp, "4", new String[]{});
//            result = AssetAppDeployStepBuilder.sortAstUnitRsps(listAstUnitRsp);
//        }
//        {
//            AstUnitRsp astUnitRspFirst = result.get(0);
//            Assert.assertEquals(astUnitRspFirst.getUnitName(), "4");
//            AstUnitRsp astUnitRspSecond = result.get(1);
//            Assert.assertEquals(astUnitRspSecond.getUnitName(), "3");
//            AstUnitRsp astUnitRspThird = result.get(2);
//            Assert.assertEquals(astUnitRspThird.getUnitName(), "1");
//            AstUnitRsp astUnitRspFourth = result.get(3);
//            Assert.assertEquals(astUnitRspFourth.getUnitName(), "2");
//        }
//    }
//
//    @Test(expectedExceptions = Exception.class)
//    public void testLoopDepend() throws  Exception{
//        //1 dpend 2, 2 no depend 1
//            List<AstUnitRsp> listAstUnitRsp = new LinkedList<>();
//            addAstUnitRsp(listAstUnitRsp, "1", new String[]{"2"});
//            addAstUnitRsp(listAstUnitRsp, "2", new String[]{"1"});
//            List<AstUnitRsp> result = AssetAppDeployStepBuilder.sortAstUnitRsps(listAstUnitRsp);
//    }
//}
