package com.oax.admin.service.aop;

import com.oax.admin.util.UserUtils;
import com.oax.common.IPHelper;
import com.oax.entity.admin.AdminOperationLog;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * 后台用户操作记录
 */
//@Aspect
@Component
@Slf4j
public class WebLogAspect {

    private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

//    @Autowired
//    private AdminOperationLogService service;

//    @Pointcut("execution(public * com.oax.admin.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            recordAdminOperationLog(joinPoint);
        } catch (Exception e) {
            logger.error("添加admin用户操作记录失败.", e);
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
//        logger.info("RESPONSE : " + ret);
    }

    /**
     * 保存用户操作记录
     */
    private void recordAdminOperationLog(JoinPoint joinPoint) throws ClassNotFoundException, NotFoundException {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取类名
        String classType = joinPoint.getTarget().getClass().getName();
        //创建类的class
        Class<?> clazz = Class.forName(classType);
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
//        Method targetMethod = methodSignature.getMethod();
//        String apiOperation = null;
//        if (targetMethod.isAnnotationPresent(ApiOperation.class)) {
//            ApiOperation apiAnnotation = targetMethod.getAnnotation(ApiOperation.class);
//            if (apiAnnotation != null) {
//                apiOperation = apiAnnotation.value();
//            }
//        }
        //具体类名
        String clazzName = clazz.getName();
        //请求的方法名
        String methodName = joinPoint.getSignature().getName();
        //得到方法参数的名称
        String[] paramNames = getFieldsName(this.getClass(), clazzName, methodName);
        String params = writeLogInfo(paramNames, joinPoint);
        // 记录下请求内容
        AdminOperationLog operationLog = new AdminOperationLog();
        operationLog.setCreateDate(new Date());
        operationLog.setIp(IPHelper.getIpAddress(request));
        operationLog.setMethodName(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        operationLog.setApiOperation(apiOperation);
        operationLog.setParams(params);
        operationLog.setRequestUrl(request.getRequestURL().toString());
        operationLog.setSessionUser(UserUtils.getShiroUser().getUsername());
//        service.saveOne(operationLog);
        log.info(operationLog.toString());

    }

    private static String writeLogInfo(String[] paramNames, JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for(int k=0; k<args.length; k++){
            if (paramNames[k].equals("request")){
                continue;
            }
            Object arg = args[k];
            sb.append(paramNames[k]+" - ");
            // 获取对象类型
            sb.append(getFieldsValue(arg));
            sb.append("  ");
        }
        return sb.toString();
    }
    /**
     * 得到方法参数的名称
     * @param cls
     * @param clazzName
     * @param methodName
     * @return
     * @throws NotFoundException
     */
    private static String[] getFieldsName(Class cls, String clazzName, String methodName) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // advice
        }
        String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++){
            paramNames[i] = attr.variableName(i + pos);	//paramNames即参数名
        }
        return paramNames;
    }
    /**
     * 得到参数的值
     * @param obj
     */
    public static String getFieldsValue(Object obj) {
        if (obj == null) {
            return "";
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if (f.getType().isEnum() && f.get(obj) != null){
                    sb.append(f.getName() + "=" + ((Enum)f.get(obj)).ordinal()+", ");
                }else if (f.get(obj) != null){//空的不返回
                    sb.append(f.getName() + "=" + f.get(obj)+", ");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
