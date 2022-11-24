plugins {
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

tasks {
    register<tequila.ValidateCommitsGitTask>("validateCommits")
    register<tequila.ValidateCommitMessageGitTask>("validateCommitMessageGit")
    register<tequila.InstallGitHook>("installGitHook")

    register("setup") {
        dependsOn("installGitHook")
    }

    assemble {
        dependsOn("setup") // auto install git hook when project is built
    }
}
