package tequila;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class InstallGitHook extends DefaultTask {

    @TaskAction
    public void installGitHook() throws IOException {
        var gitHookTarget = getProject().getRootDir().toPath()
                .resolve(Path.of(".git", "hooks", "commit-msg"));
        var gitHookSource = getProject().getRootDir().toPath()
                .resolve(Path.of("buildSrc", "src", "main", "resources", "tequila", "hooks", "commit-msg"));
        if (Files.notExists(gitHookTarget)) {
            Files.copy(gitHookSource, gitHookTarget, StandardCopyOption.COPY_ATTRIBUTES);
        }
    }
}
