package aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
public class MonitoringAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringAspect.class);

    @Around("publicMethodsInSportBetting()")
    public Object printMonitoredData(ProceedingJoinPoint pjp) throws Throwable {
        String targetMethodName = pjp.getSignature().getName();
        StringBuilder incomingParams = new StringBuilder();
        Object[] signatureArgs = pjp.getArgs();

        for (Object signatureArg : signatureArgs) {
            incomingParams.append(signatureArg.toString());
        }
        LOGGER.info("Method name: [" + targetMethodName + "], parameter(s):(" + incomingParams + ")");
        long started = System.nanoTime();

        Object result = pjp.proceed();

        long ended = System.nanoTime();

        LOGGER.info("Method name: [" + targetMethodName + "], return value:[" + result + "]");

        LOGGER.info("Execution time = " + (ended - started)/1000 + " μs");

        return result;
    }

    @Pointcut("execution(public * service.SportsBettingService.*(..))")
    public void publicMethodsInSportBetting() {
    }

}
