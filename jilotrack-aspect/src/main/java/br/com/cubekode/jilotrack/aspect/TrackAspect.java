package br.com.cubekode.jilotrack.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.SourceLocation;

import br.com.cubekode.jilotrack.tracker.Tracker;

@Aspect
public class TrackAspect {

    @Pointcut("execution(* *(..)) && !within(br.com.cubekode.jilotrack..*)")
    public void track() {
    }

    @Before("track()")
    public void beforeTrack(JoinPoint joinPoint) throws Throwable {
        Tracker.begin(signature(joinPoint));
    }

    @AfterReturning("track()")
    public void afterTrackReturning(JoinPoint joinPoint) throws Throwable {
        Tracker.get().end(signature(joinPoint));
    }

    @AfterThrowing(pointcut = "track()", throwing = "e")
    public void afterTrackThrowing(JoinPoint joinPoint, Throwable e) throws Throwable {
        Tracker.get().end(signature(joinPoint), e);
    }

    protected String signature(JoinPoint joinPoint) {

        StringBuilder str = new StringBuilder();

        str.append("class :");
        str.append(joinPoint.getSignature().getDeclaringType().getCanonicalName());

        str.append(", method: ");
        str.append(joinPoint.getSignature());

        SourceLocation sourceLocation = joinPoint.getSourceLocation();
        if (sourceLocation != null) {
            str.append(", source: ").append(sourceLocation);
        }

        return str.toString();
    }
}
