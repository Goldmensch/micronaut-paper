# Tequila "git" workflow java gradle template

This repository template contains the definition of the tequila workflow, 
some useful java (gradle) workflows to enforce this workflow and guidelines to configure your
GitHub repository.

## The tequila "git" workflow
The keywords "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED",  "MAY", and "OPTIONAL" in this document are to be interpreted as described in RFC 2119.

### About

The Tequila git workflow tries to be a modern alternative to gitflow. 
The Tequila workflow makes use of things like releases or tags which are common features in nearly every familiar git based development platform such as GitHub or GitLab. 
The goals of this workflow are to make it easier to understand the history of a project, validate versions and changes and to build efficient ci/cd.

The Tequila workflow uses a decentralized “truth” repo like the gitflow workflow and has some other commonalities. 
Also, this workflow is based on Semantic Versioning.

### Branches

This workflow has knowledge of 4 different types of branches:

- main branch -- the branch name MUST be "main"
- feat/fix branches -- the branch name MUST follow the pattern `[feat/fix]/<branchname>` if directly created from main or `[feat/fix]/<sub-main-branch>/<branchnam>` if created from a sub-main branch
- sub-main branches -- any branch name except "main" and it MUST NOT include "feat" or "fix"
- version branches -- only the major version number followed by ".x", eg "1.x"; "5.x"
  
Replace `<...>` with your own value, eg. `<branchname>` with "add user managment".

Replace `[.../...]` with one of the values inside, eg. `[feat/fix]` with "feat".

#### The feat/fix branch
A feat/fix branch has nearly the same usages as the gitflow’s feature branch, except that it is merged directly in the main, a version or a sub-main branch and no development branch is used.

A feat or fix branch is created for each new feature, fix or other enhancement. 
Such a branch SHOULD always be responsible for only one task and the goals of this enhancement SHOULD be clear before the development begins. 
A feat/fix branch SHOULD be rebased on top of the main, a version or a sub-main branch. 
When this branch is finished, it’s merged in the main, a version or a sub-main branch.

#### The main branch
The main branch adopts the development branch of the gitflow workflow, but makes several changes to its usage. 
Firstly no direct pushes of changes are allowed in this branch, so all changes MUST be made by a pull/merge request. 
Secondly a main branch MUST always be compilable and all tests MUST pass. 
The main branch serves as a collector of all changes for the next major version and is therefore allowed to contain breaking changes compared to the current version branch.

#### The sub-main branch
A sub-main branch has the same usage and rules as the main branch, but it’s used to enable the development of bigger features to be split in several smaller feat/fix branches, these branches are first merged in the corresponding sub-main.
The latest changes from the main branch SHOULD be merged in the sub-main branch. 
When the feature is then completed, the sub-main branch is merged into the main or a version branch.

#### The version branch
A version branch is created from the main branch the same time a new major release is released.
All preparation for this version MUST be made in the main branch.
All development for a version is made in this version branch, so all feat/fix/sub-main branches corresponding to the same version MUST be merged into this branch instead of the main branch.
After each change in the latest version branch these changes SHOULD be merged in the main branch.
After each change that is applicable and useful in a newer version branch the older version branch SHOULD be merged into the newer one, until the current version branch is reached.
Development for the next major version MUST be made in the main branch. Like in the main branch all changes MUST be made by a pull/merge request.
A version branch SHOULD only exist as long as the version is supported or maintained.

### Releases
Versions are released with the help of tags and the releases function of the specific development platform such as GitHub or Gitlab.
For each major/minor/patch version an own git tag and release MUST be created.
For the version SemVer MUST be used.
Unsupported/no longer maintained versions/releases MUST be explicitly marked as that.

### Pull/merge requests
Pull/merge requests SHOULD NOT be squashed.
Also, a pull/merge request MUST always state breaking changes and added features in its description.

### Commits
All commits MUST follow the Conventional Commits specification defined here
https://www.conventionalcommits.org

However, this template is using an extended version described [here](https://gist.github.com/qoomon/5dfcdf8eec66a051ecd85625518cfd13).

## Repository settings suggestions

- [x] -> set; left out settings are to be understood as unset

### Branches

- default branch: "main"

#### Branch protection rules
Pattern: "*" // applies to main, sub-main and version branches

##### Settings

- [x] Require a pull request before merging
- [x] Require status checks to pass before merging
  - [x] Require branches to be up to date before merging
  - Status checks that are required:
    - build-and-test (source: Github Actions)
    - validate-commit (source: Github Actions)
- [x] Require conversation resolution before merging
- [x] Do not allow bypassing the above settings
- [x] Allow deletion // depends on the team organization

### Pull Requests

- [x] Allow merge commits
  - Default: "Default to pull request title and description"
- [x] Always suggest updating pull request branches
- [x] Allow auto-merge
- [x] Automatically delete head branches



