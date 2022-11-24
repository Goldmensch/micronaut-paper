package tequila;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ValidateCommitsGitTask extends ValidateCommitMessageGitTask {
    public ValidateCommitsGitTask() throws Exception {
    }

    @Override
    public void task() throws Exception {
        var validateCommitsBefore = isTemplateRepository(repository)
                ? "da671f4db3f28d18b140cc2aac91bd836215f45e"
                : (String) getProject().property("commit.ignoreCommitsBefore");

        System.out.println(repository.getConfig().getString("remote", "origin", "url"));

        var rootBranch = rootBranch(repository.getBranch());
        commits(git, log -> {
            if (validateCommitsBefore != null && !validateCommitsBefore.isBlank()) {
                var ignoreBeforeCommit = repository.resolve(validateCommitsBefore);
                if (ignoreBeforeCommit == null) {
                    throw new IllegalArgumentException("Commit mentioned in 'ignoreBeforeCommit.ignoreCommitsBefore' wasn't found: %s".formatted(
                            validateCommitsBefore
                    ));
                }
                log.not(ignoreBeforeCommit);
            }
            if (rootBranch != null) {
                var rootRef = repository.resolve("origin/%s".formatted(rootBranch(repository.getBranch())));
                var latestRootCommit = commits(git, logL -> logL.setMaxCount(1).add(rootRef)).findFirst().orElse(null);
                if (latestRootCommit != null) log.not(latestRootCommit);
            }
        })
                .filter(commit -> commit.getParents().length == 1) // ignore merge commits
                .map(this::validate)
                .filter(Objects::nonNull)
                .forEach(this::addErr);
    }

    // assume validated branch naming
    public String rootBranch(String branch) {
        var branches = branch.split("/");
        return switch (branches.length) {
            case 1 -> null;
            case 2 -> "main";
            case 3 -> branches[1];
            default -> throw new IllegalArgumentException("Branch doens't follow branch naming conventions.");
        };
    }

    private String validate(RevCommit commit) {
        getLogger().info("Validating commit: {}", commitDisplayHeader(commit));
        var errors = Stream.concat(validateHeader(commit.getShortMessage()).stream(),
                        validateFooter(commit.getFullMessage()).stream())
                .collect(Collectors.joining("\n"));

        return errors.isEmpty()
                ? null
                : "Errors for commit: %s \n %s".formatted(commitDisplayHeader(commit),
                errors);
    }

    private String commitDisplayHeader(RevCommit commit) {
        return "%s @s %s".formatted(commit.getShortMessage(), commit.getId().abbreviate(7).name());
    }

    private Stream<RevCommit> commits(Git git, ThrowingConsumer<LogCommand> builder) throws Exception {
        var log = git.log();
        builder.apply(log);
        return StreamSupport.stream(log.call().spliterator(), false);
    }

    @FunctionalInterface
    private interface ThrowingConsumer<T> {
        void apply(T some) throws Exception;
    }
}
