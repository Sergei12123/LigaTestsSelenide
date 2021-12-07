package at.helpers;

import at.utils.allure.AllureHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ScreenshotsAspect {
    @After("@annotation(io.qameta.allure.Step) && execution(* pages..*(..))")
    public void takeScreenshotAfterStep(final JoinPoint joinPoint) {
        AllureHelper.screenshot();
    }
}
