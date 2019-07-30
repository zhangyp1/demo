package com.newland.paas.common.util;

/**
 * excel 导出工具
 * 
 * @author SongDi
 * @date 2018/07/12
 */
public class ExcelUtils {

    /*public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        Context context = PoiTransformer.createInitialContext();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        // 获得配置
        JexlExpressionEvaluator evaluator =
            (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        // 设置静默模式，不报警告
        // evaluator.getJexlEngine().setSilent(true);
        // 函数强制，自定义功能
        Map<String, Object> funcs = new HashMap<String, Object>();
        // 添加自定义功能
        funcs.put("utils", new ExcelUtils());
        evaluator.getJexlEngine().setFunctions(funcs);
        // 设置公式处理引擎。必须要这个，否者多Sheet也函数统计会错乱
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
    }
    
    public static void exportExcel(File xls, File out, Map<String, Object> model)
        throws FileNotFoundException, IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }
    
    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws Exception {
        File template = getTemplate(templatePath);
        if (template != null) {
            exportExcel(new FileInputStream(template), os, model);
        } else {
            throw new Exception("Excel 模板未找到。");
        }
    }
    
    // 获取jxls模版文件
    public static File getTemplate(String path) {
        File template = new File(path);
        if (template.exists()) {
            return template;
        }
        return null;
    }
    
    // 日期格式化
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    */
}
