package com.newland.paas.sbcommon.doc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import com.newland.paas.paasservice.controllerapi.annotation.Doc;
import com.newland.paas.sbcommon.doc.dto.InfDefine;
import com.newland.paas.sbcommon.doc.dto.InfDefineDocument;
import com.newland.paas.sbcommon.doc.dto.ParamDefine;
import com.newland.paas.sbcommon.utils.SpringContextUtil;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * @author laiCheng
 * @date 2018/8/3 15:17
 */
public class DocParser {

    public static final int MAX_TYPE_PARSE = 10;

    public List<InfDefine> paseInfs() {
        // all controller
        Map<String, Object> bs = SpringContextUtil.getContext().getBeansWithAnnotation(RestController.class);
        List<InfDefine> ret = new ArrayList<>();
        for (Object b : bs.values()) {
            try {
                ret.addAll(
                    parseInfDefineClass(Class.forName(b.getClass().getName().replaceAll("(.*?)\\$\\$.*", "$1"))));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        ret.sort(Comparator.comparing(InfDefine::getMappingString));
        return ret;
    }

    public InfDefineDocument parseDetail(InfDefine infDefine) {
        InfDefineDocument ret = new InfDefineDocument();
        ret.setName(getDocMain(infDefine.getMethod()));
        ret.setDesc(getDocDesc(infDefine.getMethod()));
        clearThreadlocal();
        ret.getInputParams().addAll(parseParameters(infDefine.getMethod()));
        String parentName = "content";
        if (infDefine.getMethod().getReturnType().equals(BasicResponseContentVo.class)) {
            parentName = "";
        }
        clearThreadlocal();
        ret.getOutputParams()
            .addAll(parseType("", parentName, parentName, infDefine.getMethod().getGenericReturnType(), false));
        clearThreadlocal();
        ret.setInputSample(
            JSONObject.toJSONStringWithDateFormat(getInputParamsSample(infDefine.getMethod()), "yyyy-MM-dd HH:mm:ss"));
        ret.setOutputSample(JSONObject.toJSONStringWithDateFormat(
            getObjMock(infDefine.getMethod().getGenericReturnType()), "yyyy-MM-dd HH:mm:ss"));
        return ret;
    }

    private ThreadLocal<Map<Type, Integer>> tcountThreadLocal = new ThreadLocal<>();

    private void clearThreadlocal() {
        tcountThreadLocal.remove();
    }

    private Integer getTypeCount(Type t) {
        Map<Type, Integer> m = tcountThreadLocal.get();
        if (m == null) {
            m = new HashMap<>();
            tcountThreadLocal.set(m);
        }
        Integer ret = m.get(t);
        return ret == null ? 0 : ret;
    }

    private void addTypeCount(Type t) {
        Map<Type, Integer> m = tcountThreadLocal.get();
        if (m == null) {
            m = new HashMap<>();
            tcountThreadLocal.set(m);
        }
        Integer tc = m.get(t);
        if (tc == null) {
            tc = 0;
        }
        tc++;
        m.put(t, tc);
    }

    private Object getObjMock(Type output) {
        if (output == Void.class) {
            return null;
        }
        MockConfig mc = new MockConfig().sizeRange(1, 1);
        try {
            return JMockData.mock(new TypeReference<Object>() {
                @Override
                public Type getType() {
                    return output;
                }
            }, mc);
        } catch (Throwable th) {

        }
        return null;
    }

    private Object getInputParamsSample(Method m) {
        MockConfig mc = new MockConfig().sizeRange(1, 1);
        Type[] type = m.getGenericParameterTypes();
        Annotation[][] pannos = m.getParameterAnnotations();
        for (int i = 0; i < pannos.length; i++) {
            List<Annotation> annos = Arrays.asList(pannos[0]);
            List<? extends Class<? extends Annotation>> annoTypes =
                annos.stream().map(e -> e.annotationType()).collect(Collectors.toList());
            if (annos.stream().filter(a -> a instanceof RequestBody).findFirst().isPresent()) {
                int finalI = i;
                try {
                    return JMockData.mock(new TypeReference<Object>() {
                        @Override
                        public Type getType() {
                            return type[finalI];
                        }
                    }, mc);
                } catch (Throwable th) {
                }
                return null;
            }
        }
        return null;
    }

    private <T> TypeReference<T> getTypeRef(Type t) {
        return new TypeReference<T>() {
            @Override
            public Type getType() {
                return t;
            }
        };
    }

    private List<ParamDefine> parseParameters(Method m) {
        Type[] type = m.getGenericParameterTypes();
        Annotation[][] pannos = m.getParameterAnnotations();
        List<ParamDefine> ret = new ArrayList<>();
        for (int i = 0; i < pannos.length; i++) {
            List<Annotation> annos = Arrays.asList(pannos[i]);
            List<? extends Class<? extends Annotation>> annoTypes =
                annos.stream().map(e -> e.annotationType()).collect(Collectors.toList());
            if (annos.stream().filter(a -> a instanceof RequestBody).findFirst().isPresent()) {
                ret.addAll(parseType("", "", "body", type[i], false));
            }
            if (annos.stream().filter(a -> a instanceof PathVariable).findFirst().isPresent()) {
                ret.addAll(parseType(
                    annos.stream().filter(e -> e instanceof Doc).map(e -> ((Doc)e).value()).findFirst().orElse(""),
                    annos.stream().filter(e -> e instanceof PathVariable).map(e -> ((PathVariable)e).value())
                        .findFirst().orElse(""),
                    "url", type[i], false).stream().map(e -> {
                        e.setParentName("url");
                        return e;
                    }).collect(Collectors.toList()));
            }
        }
        return ret;
    }

    private List<ParamDefine> parseType(String name, String code, String parentName, Type t, boolean virtual) {

        List<ParamDefine> ret = new ArrayList<>();
        ParamDefine pd = new ParamDefine();
        pd.setCode(code);
        pd.setName(name);
        pd.setParentName(parentName);
        if (!virtual) {
            ret.add(pd);
        }
        if (t instanceof ParameterizedTypeImpl) {
            // 泛型
            String aTypeName =
                ((ParameterizedTypeImpl)t).getActualTypeArguments()[0].getTypeName().replaceAll(".*\\.(.*)", "$1");
            pd.setCode(aTypeName);
            pd.setType(((ParameterizedTypeImpl)t).getRawType().getSimpleName() + "<" + aTypeName + ">");
            ret.addAll(parseType(null, pd.getCode(), pd.getCode(),
                ((ParameterizedTypeImpl)t).getActualTypeArguments()[0], true));
        } else if (t.getTypeName().startsWith("java.")) {
            // java 内置类型
            pd.setType(t.getTypeName().replaceAll(".*\\.(.*)", "$1"));
        } else {
            // 自定义vo
            if (t instanceof Class) {
                if (getTypeCount(t) > MAX_TYPE_PARSE) {
                    return Collections.EMPTY_LIST;
                }
                addTypeCount(t);
                pd.setType(((Class)t).getSimpleName());
                for (Method m : ((Class)t).getMethods()) {
                    String fieldName = getFieldName(m);
                    if (fieldName == null) {
                        continue;
                    }
                    ret.addAll(parseType(getFieldDoc(m.getDeclaringClass(), fieldName), fieldName, pd.getCode(),
                        m.getGenericReturnType(), false));
                }
            }
        }
        return ret;
    }

    private String getFieldDoc(Class cls, String fieldName) {
        String ret = "";
        try {
            Field f = cls.getDeclaredField(fieldName);
            Doc da = f.getAnnotation(Doc.class);
            if (da != null) {
                ret = da.value();
            }
        } catch (NoSuchFieldException e) {
            // ignore
        }
        return ret;
    }

    private String getFieldName(Method m) {
        if (!m.getName().matches("^get(.+)$") || m.getParameterCount() != 0 || m.getDeclaringClass() == Object.class) {
            return null;
        }
        StringBuilder fieldNamesb = new StringBuilder(m.getName().replaceAll("^get(.+)$", "$1"));
        fieldNamesb.setCharAt(0, Character.toLowerCase(fieldNamesb.charAt(0)));
        return fieldNamesb.toString();
    }

    private List<InfDefine> parseInfDefineClass(Class cls) {
        ArrayList<InfDefine> ret = new ArrayList<>();
        String baseUrl = "/" + getRequestMapping(cls) + "/";
        for (Method m : cls.getMethods()) {
            ret.addAll(getMappingUrl(baseUrl, m).stream().map(f -> {
                InfDefine t = new InfDefine();
                t.setMappingString(f);
                t.setName(getDocMain(m));
                if (StringUtils.isEmpty(t.getName())) {
                    t.setName(m.getName());
                }
                t.setMethod(m);
                return t;
            }).collect(Collectors.toList()));
        }
        return ret;
    }

    private String normalizeUrl(String url) {
        return url.replace("\\", "/").replaceAll("/{2,}", "/");
    }

    private String getRequestMapping(Class cls) {
        RequestMapping anno = (RequestMapping)cls.getAnnotation(RequestMapping.class);
        if (anno != null && anno.value().length > 0) {
            return anno.value()[0];
        }
        return "";
    }

    private String getDocMain(Method method) {
        Doc anno = method.getAnnotation(Doc.class);
        if (anno != null) {
            return anno.value();
        }
        return "";
    }

    private String getDocDesc(Method method) {
        Doc anno = method.getAnnotation(Doc.class);
        if (anno != null && StringUtils.isNotBlank(anno.desc())) {
            return anno.desc();
        }
        return "";
    }

    private List<String> getMappingUrl(String baseUrl, Method method) {
        List<String> ret = new ArrayList<>();

        GetMapping get = method.getAnnotation(GetMapping.class);
        if (get != null) {
            ret.addAll(Arrays.asList(get.value()).stream().map(f -> "GET: " + normalizeUrl(baseUrl + f))
                .collect(Collectors.toList()));
        }
        PostMapping post = method.getAnnotation(PostMapping.class);
        if (post != null) {
            ret.addAll(Arrays.asList(post.value()).stream().map(f -> "POST: " + normalizeUrl(baseUrl + f))
                .collect(Collectors.toList()));
        }
        PutMapping put = method.getAnnotation(PutMapping.class);
        if (put != null) {
            ret.addAll(Arrays.asList(put.value()).stream().map(f -> "PUT: " + normalizeUrl(baseUrl + f))
                .collect(Collectors.toList()));
        }
        DeleteMapping delete = method.getAnnotation(DeleteMapping.class);
        if (delete != null) {
            ret.addAll(Arrays.asList(delete.value()).stream().map(f -> "DELETE: " + normalizeUrl(baseUrl + f))
                .collect(Collectors.toList()));
        }
        RequestMapping req = method.getAnnotation(RequestMapping.class);
        if (req != null) {
            Arrays.asList(req.method()).stream().forEach(methodname -> {
                ret.addAll(Arrays.asList(req.value()).stream().map(f -> methodname + ": " + normalizeUrl(baseUrl + f))
                    .collect(Collectors.toList()));
            });
        }
        return ret;
    }
}
