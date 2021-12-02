package at.helpers;

import java.util.Optional;

import at.steps.Hook;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class MyTestWatcher implements TestWatcher {
    @SneakyThrows
    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        HookHelper.makeScreenshot();
        new Hook().afterEach();
    }

    @SneakyThrows
    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        HookHelper.makeScreenshot();
        new Hook().afterEach();

    }

    @SneakyThrows
    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        HookHelper.makeScreenshot();
        new Hook().afterEach();

    }

    @SneakyThrows
    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        new Hook().afterEach();

    }
}
