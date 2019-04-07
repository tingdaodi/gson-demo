package com.gson.gsondemo.aspect;

import com.google.gson.Gson;
import com.gson.gsondemo.annotation.OperationLog;
import com.gson.gsondemo.model.OperationLogEntity;
import com.gson.gsondemo.model.Users;
import com.gson.gsondemo.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

/**
 * Description TODO
 *
 * @author Roye.L
 * @date 2019/4/7 22:37
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    private static final ThreadLocal<LocalDateTime> BEGIN_TIME_LOCAL = new NamedThreadLocal<>("ThreadLocal beginTime");
    private static final ThreadLocal<OperationLogEntity> OPERATION_LOG_LOCAL = new NamedThreadLocal<>("ThreadLocal operation log");
    private static final ThreadLocal<Users> USER_LOCAL = new NamedThreadLocal<>("ThreadLocal user");

    @Autowired
    HttpServletRequest request;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    ThreadPoolTaskExecutor operationLogExecutor;

    @Pointcut("@annotation(com.gson.gsondemo.annotation.OperationLog)")
    public void operation() {
    }

    @Before("operation()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("Enter operation log aspect the before.");

        LocalDateTime beginTime = LocalDateTime.now();
        BEGIN_TIME_LOCAL.set(beginTime);

        log.info("Begin time: {}  URI: {}", beginTime.toString(), request.getRequestURI());

        Users users = Users.builder().username("roy").age(10).build();

        USER_LOCAL.set(users);
    }

    @After("operation()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("Enter operation log aspect the after.");

        Users users = USER_LOCAL.get();

        if (null != users) {
            String title = "";
            String type = "info";
            String remoteAddr = request.getRemoteAddr();
            String requestUri = request.getRequestURI();
            String method = request.getMethod();
            Map<String, String[]> params = request.getParameterMap();

            try {
                title = getDescription(joinPoint);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long beginTime = BEGIN_TIME_LOCAL.get().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long endTime = System.currentTimeMillis();

            log.info("计时结束：{}  URI: {}  耗时： {} s   最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(endTime),
                    request.getRequestURI(),
                    (endTime - beginTime) / 1000,
                    Runtime.getRuntime().maxMemory() / 1024 / 1024,
                    Runtime.getRuntime().totalMemory() / 1024 / 1024,
                    Runtime.getRuntime().freeMemory() / 1024 / 1024,
                    (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);

            OperationLogEntity operationLogEntity = new OperationLogEntity();
            operationLogEntity.setLogId(UUID.randomUUID().toString());
            operationLogEntity.setTitle(title);
            operationLogEntity.setType(type);
            operationLogEntity.setRemoteAddr(remoteAddr);
            operationLogEntity.setRequestUri(requestUri);
            operationLogEntity.setMethod(method);
            operationLogEntity.setMapToParams(params);
            operationLogEntity.setException("无异常!");
            operationLogEntity.setUserId(users.getUsername());
            operationLogEntity.setOperateDate(BEGIN_TIME_LOCAL.get());
            operationLogEntity.setTimeout(String.valueOf((endTime - beginTime) / 1000));

            operationLogExecutor.execute(new SaveOperationLogThread(operationLogEntity, operationLogService));

            OPERATION_LOG_LOCAL.set(operationLogEntity);
        }
    }

    @AfterReturning(returning = "response", pointcut = "operation()")
    public void doAfterReturning(Object response) {
        Gson gson = new Gson();
        String params = gson.toJson(response);

        // 处理完请求，返回内容
        log.info("==========返回参数日志=========");
        log.info("返回接口响应参数:" + params);

        OperationLogEntity entity = OPERATION_LOG_LOCAL.get();
        if (entity != null) {
            entity.setResultParams(params);
            log.info("==========更新日志参数=========");
            new UpdateOperationLogThread(entity, operationLogService).start();
        }
    }

    /**
     *   异常通知 记录操作报错日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "operation()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info("进入日志切面异常通知!!");
        log.info("异常信息:" + e.getMessage());
        OperationLogEntity entity = OPERATION_LOG_LOCAL.get();
        if (entity != null) {
            entity.setType("error");
            entity.setException(e.toString());
            new UpdateOperationLogThread(entity, operationLogService).start();
        }
    }

    private static String getDescription(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        return operationLog.description();
    }

    private static class SaveOperationLogThread implements Runnable {
        private OperationLogEntity logEntity;
        private OperationLogService operationLogService;

        SaveOperationLogThread(OperationLogEntity logEntity, OperationLogService operationLogService) {
            this.logEntity = logEntity;
            this.operationLogService = operationLogService;
        }

        @Override
        public void run() {
            operationLogService.add(logEntity);
        }
    }

    private static class UpdateOperationLogThread extends Thread {
        private OperationLogEntity logEntity;
        private OperationLogService operationLogService;

        public UpdateOperationLogThread(OperationLogEntity logEntity, OperationLogService operationLogService) {
            super(UpdateOperationLogThread.class.getSimpleName());
            this.logEntity = logEntity;
            this.operationLogService = operationLogService;
        }

        @Override
        public void run() {
            operationLogService.obtain();
        }
    }


}
