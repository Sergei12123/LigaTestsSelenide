package at.helpers;

import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class MyTestWatcher implements TestWatcher {
    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        HookHelper.makeScreenshot(false);
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        HookHelper.makeScreenshot(false);
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        HookHelper.makeScreenshot(false);
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        // do something
    }
}
