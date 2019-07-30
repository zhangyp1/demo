package com.newland.paas.prefabsql.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
//import com.newland.csf.common.log.mgr.LogFactory;
//import com.newland.csf.common.log.mgr.Log;
//import org.apache.log4j.Logger;

/**
 * 获取和设置某种对象的字段和方法信息
 */
public class ClassMsg{
	//protected static com.newland.csf.common.log.mgr.Log mLogger = LogFactory.getLogger(ClassMsg.class);
	/**
	 * 设置对象oSrc的字段sName(忽略大小写)值为value
	 * @param sName
	 * @param oSrc
	 * @param value
	 * @return
	 */
	public static boolean setValueByName(String sName,Object oSrc,Object value){
		if(sName==null||oSrc==null){
			return false;
		}
		return setValueByName(sName,oSrc.getClass(),oSrc,value);
	}
	/**
	 * 设置对象oSrc的字段sName(忽略大小写)值为value
	 * @param sName
	 * @param oSrc
	 * @param value
	 * @return
	 */
	public static boolean setValueByName(String sName,Class co,Object oSrc,Object value){
		try{
			int flag = 0;
			if(sName==null||(co==null&&oSrc==null)){
				return false;
			}
			java.lang.reflect.Field[] aF = null;
			if(co==null){
				co = oSrc.getClass();
			}
			if(oSrc==null){
				flag = 1;
				oSrc = co;
			}
			aF = co.getDeclaredFields();
			if(aF!=null){
				// java.lang.reflect.AccessibleObject.setAccessible(aF, true);
				for(int i = 0;i<aF.length;i++){
					if(aF[i].getName().equalsIgnoreCase(sName)){
						// --对于不是公有字段,必须设置成可以修改的权限以便修改-------------------------------
						if(aF[i].getModifiers()!=java.lang.reflect.Modifier.PUBLIC){
							try{
								aF[i].setAccessible(true);
							}
							catch(SecurityException ex1){
								ex1.printStackTrace();
							}
							// ----------------------------------------------------------------------------------
						}
						if((aF[i].getType().getName().equals("int")||aF[i].getType().getName().equals("float")||aF[i].getType().getName().equals("boolean")||aF[i].getType().getName().equals("double"))&&value==null){
							return true;
						}
						else{
							aF[i].set(oSrc,value);
						}
						return true;
					}
				}
			}
			if(co!=null){
				co = co.getSuperclass();
				if(co!=null&&!Object.class.equals(co)){
					if(flag==1){
						oSrc = null;
					}
					return setValueByName(sName,co,oSrc,value);
				}
			}
			return false;
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg setValueByName() Error:",ex);
			return false;
		}
	}
	/**
	 * 获取对象oSrc的字段sName(忽略大小写)值
	 * @param sName
	 * @param oSrc
	 * @return
	 */
	public static Object getValueByName(String sName,Object oSrc){
		if(sName==null||oSrc==null){
			return null;
		}
		return getValueByName(sName,oSrc.getClass(),oSrc);
	}
	public static Object getValueByName(String sName,Class co,Object oSrc){
		try{
			if(sName==null||co==null){
				return null;
			}
			if(oSrc==null){
				oSrc = co;
			}
			java.lang.reflect.Field[] aF = null;
			aF = co.getDeclaredFields();
			if(aF!=null){
				// java.lang.reflect.AccessibleObject.setAccessible(aF, true);
				for(int i = 0;i<aF.length;i++){
					if(aF[i].getName().equalsIgnoreCase(sName)){
						// --对于不是公有字段,必须设置成可以访问的权限以便访问-------------------------------
						if(aF[i].getModifiers()!=java.lang.reflect.Modifier.PUBLIC){
							try{
								aF[i].setAccessible(true);
							}
							catch(SecurityException ex1){
								ex1.printStackTrace();
							}
							// ----------------------------------------------------------------------------------
						}
						return aF[i].get(oSrc);
					}
				}
			}
			if(co!=null){
				co = co.getSuperclass();
				if(co!=null&&!Object.class.equals(co)){
					return getValueByName(sName,co,oSrc);
				}
			}
			return null;
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg getValueByName() Error:",ex);
			return null;
		}
	}
	/**
	 * 获取指定(sName,区分大小写)的方法
	 * @param cl
	 * @param sName
	 * @param aClass
	 * @return
	 */
	public static java.lang.reflect.Method getMethod(Class cl,String sName,Class[] aClass){
		try{
			java.lang.reflect.Method[] aM = cl.getDeclaredMethods();
			java.lang.reflect.Method resultMethod = null;
			if(aM!=null){
				for(int i = 0;i<aM.length;i++){
					String sMethodName = aM[i].getName(); // 获取该方法名称
					if(sMethodName.equals(sName)){
						Class[] aCl = aM[i].getParameterTypes(); // 获取该方法输入参数类型
						boolean bEqual = false;
						if((aCl!=null)&&(aClass!=null)){
							if(aCl.length==aClass.length){
								bEqual = true;
								for(int j = 0;j<aClass.length;j++){
									// System.out.println(aCl[j].getName()+"=?="+aClass[j].getName());
//									if(!aCl[j].getName().equals(aClass[j].getName())){
//										if((aCl[j].getName().equals("int"))&&(aClass[j].getName().equals("java.lang.Integer"))){
//											continue;
//										}
//										bEqual = false;
//										break;
//									}
									//edit by 2017-9-22
									if(!aCl[j].getName().equals(aClass[j].getName()) && (aCl[j].getName().equals("int"))&&(aClass[j].getName().equals("java.lang.Integer"))){
										continue;
									} else {
										bEqual = false;
										break;
									}
								}
							}
							if(!bEqual){
								continue;
							}
						}
						if(((aCl==null)||(aCl.length==0))&&((aClass==null)||(aClass.length==0))){
							bEqual = true;
						}
						if(bEqual){
							resultMethod = aM[i];
							break;
						}
					}
				}
			}
			try{
				if(resultMethod!=null){
					resultMethod.setAccessible(true);
				}
			}
			catch(SecurityException ex1){
				ex1.printStackTrace();
			}
			return resultMethod;
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg getMethod() Error:",ex);
			return null;
		}
	}
	/**
	 * 获取指定的类cl的所有字段描述信息
	 * @param cl
	 * @param obj
	 * @param flag flag=1取全部的字段,0:只取公有的
	 * @return Object[x][0]:字段修饰符(如public,private等)
	 *         Object[x][1]:字段类型名称(如int,java.lang.String等) Object[x][2]:该字段名
	 *         Object[x][3]:对应的值 (如果obj不为空,就取obj中其对应的字段值,否则为空)
	 */
	public static Object[][] getFieldMsgs(Class cl,Object obj,int flag){
		try{
			java.lang.reflect.Field[] aF = null;
			if(flag==0){
				aF = cl.getFields();
			}
			else{
				aF = cl.getDeclaredFields();
			}
			if(aF!=null){
				Object[][] oResult = new Object[aF.length][4];
				if(obj!=null){
					if(flag==1){
						java.lang.reflect.AccessibleObject.setAccessible(aF,true);
					}
				}
				for(int i = 0;i<aF.length;i++){
					oResult[i][0] = java.lang.reflect.Modifier.toString(aF[i].getModifiers()); // 获取该字段的修饰符,public
																														// ,private
					oResult[i][1] = aF[i].getType().getName(); // 获取该字段类型名
					oResult[i][2] = aF[i].getName(); // 获取该字段名称
					if(obj!=null){
						try{
							oResult[i][3] = aF[i].get(obj); // 获取该字段值
						}
						catch(Exception ex2){
							oResult[i][3] = null;
						}
					}
				}
				return oResult;
			}
			else{
				return null;
			}
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg getFieldMsgs() Error:",ex);
			return null;
		}
	}
	/**
	 * 获取指定的类cl的所有方法的描述信息
	 * @param cl
	 * @param obj
	 * @return Object[x][0]:方法修饰符(如public,private等)
	 *         Object[x][1]:方法返回值名称(如int,java.lang.String等) Object[x][2]:该方法名
	 *         Object[x][3]:该方法输入参数描述
	 */
	public static Object[][] getMethodMsgs(Class cl){
		try{
			java.lang.reflect.Method[] aM = cl.getDeclaredMethods();
			if(aM!=null){
				Object[][] oResult = new Object[aM.length][4];
				for(int i = 0;i<aM.length;i++){
					try{
						aM[i].setAccessible(true);
					}
					catch(SecurityException ex1){
						ex1.printStackTrace();
					}
					oResult[i][0] = java.lang.reflect.Modifier.toString(aM[i].getModifiers()); // 获取该方法的修饰符,public
																														// ,private
					oResult[i][1] = aM[i].getReturnType().getName(); // 获取该方法返回值的类型名
					oResult[i][2] = aM[i].getName(); // 获取该方法名称
					Class[] aCl = aM[i].getParameterTypes(); // 获取改方法输入参数类型
					if(aCl!=null){
						oResult[i][3] = "";
						for(int j = 0;j<aCl.length;j++){
							oResult[i][3] = oResult[i][3]+aCl[j].getName();
							if(j<aCl.length-1){
								oResult[i][3] = oResult[i][3]+",";
							}
						}
					}
					else{
						oResult[i][3] = "";
					}
				}
				return oResult;
			}
			else{
				return null;
			}
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg getMethodMsgs() Error:",ex);
			return null;
		}
	}
	/**
	 * 获取指定的类cl的所有接口的描述信息
	 * @param cl
	 * @param obj
	 * @return Object[x][0]:该接口名称
	 */
	public static Object[][] getInterfaceMsgs(Class cl){
		try{
			Class[] aInterFace = cl.getInterfaces();
			if(aInterFace!=null){
				Object[][] oResult = new Object[aInterFace.length][1];
				for(int i = 0;i<aInterFace.length;i++){
					oResult[i][0] = aInterFace[i].getName();
				}
				return oResult;
			}
			else{
				return null;
			}
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg getInterfaceMsgs() Error:",ex);
			return null;
		}
	}
	/**
	 * 获取obj的字段名数组
	 * @param obj
	 * @param flag 0:取共有字段 1：取全部字段
	 * @return
	 */
	public static String[] getFieldNames(Object obj,int flag){
		try{
			String[] aFieldsNames = null;
			java.lang.reflect.Field[] aF = null;
			Class co = obj.getClass();
			if(flag==0){
				aF = co.getFields();
			}
			else{
				aF = co.getDeclaredFields();
			}
			aFieldsNames = new String[aF.length];
			for(int i = 0;i<aF.length;i++){
				aFieldsNames[i] = aF[i].getName();
			}
			return aFieldsNames;
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg getFieldNames() Error:",ex);
			return null;
		}
	}
	/**
	 * 将类型cl生成的字段信息输出成字符串信息
	 * @param cl
	 * @param obj
	 * @return
	 */
	public static String debug(Class cl,Object obj){
		try{
			StringBuffer sbResult = new StringBuffer();
			Object[][] objdesc = ClassMsg.getFieldMsgs(cl,obj,1);
			if(objdesc!=null){
				for(int i = 0;i<objdesc.length;i++){
					sbResult.append(objdesc[i][0]);
					sbResult.append(" ");
					sbResult.append(objdesc[i][1]);
					sbResult.append(" ");
					sbResult.append(objdesc[i][2]);
					sbResult.append("=");
					sbResult.append(objdesc[i][3]);
					sbResult.append("\r\n");
				}
			}
			objdesc = getMethodMsgs(cl);
			if(objdesc!=null){
				for(int i = 0;i<objdesc.length;i++){
					sbResult.append(objdesc[i][0]);
					sbResult.append(" ");
					sbResult.append(objdesc[i][1]);
					sbResult.append(" ");
					sbResult.append(objdesc[i][2]);
					sbResult.append("(");
					sbResult.append(objdesc[i][3]);
					sbResult.append(")\r\n");
				}
			}
			objdesc = getInterfaceMsgs(cl);
			if(objdesc!=null){
				for(int i = 0;i<objdesc.length;i++){
					sbResult.append("接口:");
					sbResult.append(objdesc[i][0]);
					sbResult.append(" ");
					sbResult.append(")\r\n");
				}
			}
			return new String(sbResult);
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg debug() Error:",ex);
			return "";
		}
	}
	/**
	 * clone复制某个对象
	 * @param obj Object
	 * @return Object
	 */
	public static Object clone(Object obj){
		try{
			java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream oo = new java.io.ObjectOutputStream(bo);
			oo.writeObject(obj);
			java.io.ByteArrayInputStream bi = new java.io.ByteArrayInputStream(bo.toByteArray());
			java.io.ObjectInputStream oi = new java.io.ObjectInputStream(bi);
			return oi.readObject();
		}
		catch(Exception ex){
			// mLogger.error("ClassMsg clone() Error:", ex);
			try{
				Object newObj = null;
				try{
					newObj = obj.getClass().newInstance();
				}
				catch(Exception ex1){
					//newObj = ((ICopy)obj).copySelf();
				}
				Object[][] aaTemp = getFieldMsgs(obj.getClass(),obj,1);
				for(int i = 0;i<aaTemp.length;i++){
					setValueByName((String)aaTemp[i][2],newObj,aaTemp[i][3]);
				}
				return newObj;
			}
			catch(Exception ex2){
				//mLogger.error("ClassMsg clone() Error:",ex2);
			}
			return null;
		}
	}
	/**
	 * 获取某个类classObj的二进制表达
	 * @param classObj Class
	 * @return byte[]
	 */
	public static byte[] getClassByteMsg(Class classObj){
		try{
			java.io.InputStream myread = null;
			String sClassName = ExtString.replace(classObj.getName(),".","/")+".class";
			java.security.ProtectionDomain protectDomain = classObj.getProtectionDomain();
			java.net.URL context = protectDomain.getCodeSource().getLocation();
			// java.net.URL context =classObj.getResource(classObj.getName());
			String sFileName = "";
			String sJarName = "";
			sJarName = context.toString();
			//mLogger.info("==========protectDomain.getCodeSource().getLocation()="+sJarName+"==========");
			if(context.toString().indexOf(sClassName)<0){ // 不包含要获取的类名
				if(context.toString().indexOf(".jar")>=0){ // 是被包含在一个JAR包中
					sClassName = "!/"+sClassName;
					if(context.toString().indexOf("jar:")<0){ // 协议没有添加jar，则添加
						sFileName = "jar:";
					}
				}
				else{ // 不在jar包内
					sClassName = "/"+sClassName;
				}
			}
			else{ // 已经包含要获取的类名
				sClassName = "";
				if(context.toString().indexOf(".jar")>=0){ // 是被包含在一个JAR包中
					if(context.toString().indexOf("jar:")<0){ // 协议没有添加jar，则添加
						sFileName = "jar:";
					}
				}
			}
			sFileName = sFileName+context.toString()+sClassName;
			//mLogger.info("loading Class File:["+sFileName+"]..........");
			java.net.URL url = new java.net.URL(sFileName);
			try{
				myread = url.openStream();
			}
			catch(IOException ex1){
				//mLogger.error("try to open file["+sFileName+"] fail:"+ex1.toString(),ex1);
				//mLogger.warn("now try to other way to get msg....");
				sJarName = ExtString.replace(sJarName,"jar:","");
				sJarName = ExtString.replace(sJarName,"file:","");
				//mLogger.info("copying jar File:["+URLDecoder.decode(sJarName,"utf-8")+"]..........");
				File src_file = new File(URLDecoder.decode(sJarName,"utf-8"));
				sJarName = ExtString.replace(sJarName,".jar","_bak.jar");
				//mLogger.info("copying to jar File:["+URLDecoder.decode(sJarName,"utf-8")+"]..........");
				File bak_file = new File(URLDecoder.decode(sJarName,"utf-8"));
				FileInputStream src_in = new FileInputStream(src_file);
				FileOutputStream dest_out = new FileOutputStream(bak_file);
				int aReadNum = src_in.available();
				byte[] aData = new byte[aReadNum];
				src_in.read(aData);
				src_in.close();
				dest_out.write(aData);
				dest_out.close();
				sJarName = "jar:file:"+sJarName+"!/"+ExtString.replace(classObj.getName(),".","/")+".class";
				//mLogger.info("loading Class File:["+sJarName+"]..........");
				url = new java.net.URL(sJarName);
				myread = url.openStream();
				bak_file.delete();
			}
			if(myread==null){
				//mLogger.error("load Class File:["+sFileName+"] fail");
			}
			else{
				//mLogger.info("get Class File:["+sFileName+"] success,file bytes="+myread.available());
			}
			int iLen = myread.available();
			byte[] aRead = new byte[iLen];
			myread.read(aRead);
			myread.close();
			return aRead;
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg getClassByteMsg() Error:",ex);
			return null;
		}
	}
	/**
	 * 根据输入的二进制流加载类
	 * @param sysLoad ClassLoader
	 * @param aRead byte[]
	 * @param sClassName String
	 * @return Class
	 */
	public static Class loadClass(ClassLoader sysLoad,byte[] aRead,String sClassName){
		return loadClass(sysLoad,null,aRead,sClassName);
	}
	/**
	 * 使用sysLoad加载指定的类clLoaded，如果sysLoad为空则使用当前线程的getContextClassLoader替代
	 * @param sysLoad ClassLoader
	 * @param clLoaded Class
	 * @return boolean
	 */
	public static boolean loadClass(ClassLoader sysLoad,Class clLoaded){
		return !(loadClass(sysLoad,clLoaded,null,null)==null);
	}
	/**
	 * 调用对象obj的指定方法sMethodName
	 * @param sMethodName String
	 * @param aParam Object[]
	 * @param obj Object
	 * @return Object
	 */
	public static Object invoke(String sMethodName,Object[] aParam,Object obj){
		try{
			Class[] aInputClass = null;
			if(aParam!=null){
				aInputClass = new Class[aParam.length];
				for(int i = 0;i<aInputClass.length;i++){
					if(aParam[i]!=null){
						aInputClass[i] = aParam[i].getClass();
					}
				}
			}
			return invoke(sMethodName,aInputClass,aParam,obj);
		}
		catch(Exception ex){
			//mLogger.warn("",ex);
			return null;
		}
	}
	/**
	 * 调用对象obj的指定方法sMethodName
	 * @param sMethodName String
	 * @param aParamType Class[]
	 * @param aParam Object[]
	 * @param obj Object
	 * @return Object
	 */
	public static Object invoke(String sMethodName,Class[] aParamType,Object[] aParam,Object obj){
		try{
			if(aParamType!=null&&aParam!=null){
				if(aParamType.length!=aParam.length){
					return null;
				}
			}
			if(aParamType==null&&aParam!=null){
				return null;
			}
			if(aParamType!=null&&aParam==null){
				return null;
			}
			if(obj==null){
				return null;
			}
			boolean isClass = false;
			if(Class.class.isInstance(obj)){
				isClass = true;
			}
			java.lang.reflect.Method invokeMethod = null;
			if(isClass){
				invokeMethod = getMethod((Class)obj,sMethodName,aParamType); // 取此对象的静态方法
			}
			else{
				invokeMethod = getMethod(obj.getClass(),sMethodName,aParamType);
			}
			if(invokeMethod==null){
				Class superClass = null;
				if(isClass){
					superClass = ((Class)obj).getSuperclass();
				}
				else{
					superClass = obj.getClass().getSuperclass();
				}
				while(invokeMethod==null&&!Object.class.equals(superClass)){
					invokeMethod = getMethod(superClass,sMethodName,aParamType);
					superClass = superClass.getSuperclass();
				}
			}
			if(invokeMethod==null){
				return null;
			}
			return invokeMethod.invoke(obj,aParam);
		}
		catch(Exception ex){
			//mLogger.warn("",ex);
			return null;
		}
	}
	/**
	 * 调用对象obj的指定的构造方法g
	 * @param aParamType Class[]
	 * @param aParam Object[]
	 * @param cl Class
	 * @return Object
	 */
	public static Object invokeConstruct(Class[] aParamType,Object[] aParam,Class cl){
		try{
			java.lang.reflect.Constructor invokeMethod = getConstruct(aParamType,aParam,cl);
			if(invokeMethod==null){
				return null;
			}
			return invokeMethod.newInstance(aParam);
		}
		catch(Exception ex){
			//mLogger.warn("",ex);
			return null;
		}
	}
	/**
	 * 获取指定类的构造方法
	 * @param aParamType Class[]
	 * @param aParam Object[]
	 * @param cl Class
	 * @return Constructor
	 */
	public static java.lang.reflect.Constructor getConstruct(Class[] aParamType,Object[] aParam,Class cl){
		try{
			if(aParamType!=null&&aParam!=null){
				if(aParamType.length!=aParam.length){
					return null;
				}
			}
			if(aParamType==null&&aParam!=null){
				return null;
			}
			if(aParamType!=null&&aParam==null){
				return null;
			}
			if(cl==null){
				return null;
			}
			java.lang.reflect.Constructor[] constructors = cl.getDeclaredConstructors();
			if(constructors==null||constructors.length==0){
				return null;
			}
			java.lang.reflect.Constructor invokeMethod = null;
			java.util.HashMap map = new java.util.HashMap();
			if(aParamType!=null){
				for(int i = 0;i<aParamType.length;i++){
					map.put(aParamType[i],"");
				}
			}
			for(int i = 0;i<constructors.length;i++){
				Class[] paramType = constructors[i].getParameterTypes();
				if((aParamType==null||aParamType.length==0)&&(paramType==null||paramType.length==0)){
					invokeMethod = constructors[i];
					invokeMethod.setAccessible(true);
					break;
				}
				else if(paramType!=null&&aParamType!=null&&paramType.length==aParamType.length){
					boolean isEqual = true;
					for(int j = 0;j<paramType.length;j++){
						if(map.get(paramType[j])==null){
							isEqual = false;
							break;
						}
					}
					if(isEqual){
						invokeMethod = constructors[i];
						invokeMethod.setAccessible(true);
						break;
					}
				}
			}
			if(invokeMethod==null){
				return null;
			}
			return invokeMethod;
		}
		catch(Exception ex){
			//mLogger.warn("",ex);
			return null;
		}
	}
	/**
	 * 使用sysLoad加载指定的类clLoaded，如果sysLoad为空则使用当前线程的getContextClassLoader替代
	 * @param sysLoad ClassLoader
	 * @param clLoaded Class
	 * @param aRead byte[]
	 * @param sClassName String
	 * @return Class
	 */
	public static Class loadClass(ClassLoader sysLoad,Class clLoaded,byte[] aRead,String sClassName){
		try{
			if(sysLoad==null){
				sysLoad = Thread.currentThread().getContextClassLoader();
			}
			boolean bLoaded = true;
			try{
				if(clLoaded!=null){
					sClassName = clLoaded.getName();
				}
				sysLoad.loadClass(sClassName);
			}
			catch(Exception ex1){
				bLoaded = false;
			}
			if(bLoaded){
				return Class.forName(sClassName,true,sysLoad);
			}
			int iOff = 0;
			if(aRead==null){
				aRead = getClassByteMsg(clLoaded);
			}
			int iLen = aRead.length;
			java.security.ProtectionDomain protectDomain = null;
			if(clLoaded!=null){
				protectDomain = clLoaded.getProtectionDomain();
			}
			else{
				protectDomain = sysLoad.getClass().getProtectionDomain();
			}
			Object[] aInput = new Object[5];
			aInput[0] = sClassName;
			aInput[1] = aRead;
			aInput[2] = new Integer(iOff);
			aInput[3] = new Integer(iLen);
			aInput[4] = protectDomain;
			Class[] aInputClass = {
					String.class,byte[].class,int.class,int.class,java.security.ProtectionDomain.class
			};
			java.lang.reflect.Method defineClass = getMethod(ClassLoader.class,"defineClass",aInputClass);
			clLoaded = (Class)defineClass.invoke(sysLoad,aInput); // 调用defineClass方法
			aInput = new Object[1];
			aInput[0] = clLoaded;
			aInputClass = new Class[1];
			aInputClass[0] = Class.forName("java.lang.Class");
			java.lang.reflect.Method resolveClass = getMethod(ClassLoader.class,"resolveClass",aInputClass);
			resolveClass.invoke(sysLoad,aInput); // 调用resolveClass方法
			Class.forName(clLoaded.getName(),true,sysLoad);
			//mLogger.info("load Class:["+clLoaded.getName()+"] success");
			return clLoaded;
		}
		catch(Exception ex){
			//mLogger.error("ClassMsg loadClass() Error:",ex);
			//mLogger.error("", ex.getMessage(), ex);
			return null;
		}
	}
	
//	public static void main(String[] args){
//		Class cl = ClassMsg.class;
//		System.out.println(cl.getSuperclass());
//	}
}
