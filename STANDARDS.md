# VoxelEngine3 Contribution Standards

This document outlines our standards and expectations when it comes to contributions.

> This is separate from our [Contributing] document, which specifies _how_ to contribute to the project.

## Git Commit Standards

Below is a list of conventions we use for git commit messages. We'd appreciate contributors using the same.

```terminaloutput
git commit -m "<type>(optional scope): <description>" \
    -m "<optional body>" \
    -m "<optional footer>"
```

### Examples

```terminaloutput
fix(gui): color transparency no longer divided by 255
```

```terminaloutput
git commit -m "fix(gui): color transparency no longer divided by 255" \
    -m "refers to (Leokayasen/VoxelEngine3#16)"
```

### Prefixes

* `fix:`
    * A commit that patches a bug in the codebase
    * In a release that includes this commit, the release version will bump the `PATCH` number
* `feat:`
    * A commit that introduces a new feature to the codebase
        * In a release that includes this commit, the release version will bump the `MINOR` number
* `breaking:`
    * A commit that involves a large portion of the codebase changing
        * In a release that includes this commit, the release version will bump the `MAJOR` number
            * In most cases, this overrules and resets the counter for `MINOR` and `PATCH` numbers
* `chore:`
    * A commit that represent tasks like:
        * initial commit, updating `.gitignore`, etc
* `ci/cd:`
    * A commit that affects CI/CD pipelines, backups, monitoring or recovery procedures
* `build:`
    * A commit that affect build tools, scripts dependencies, project version, etc
* `docs:`
    * A commit that exclusively affects documentation
        * This includes both external and internal docs (e.g. Javadoc in a class, matching documentation page on a new
          feature)
* `test:`
    * A commit that adds missing tests or corrects existing ones

### Scope

The `scope` provides additional, optional context to the commit.

* Allowed scopes vary and are typically defined by the branch you are working with
* **Do not** use issue identifiers as scopes

### Breaking Changes

If the commit introduces a breaking change, such as a large refactor of a package or class,
the changes **must** be indicated by a `!` like this: `feat(core)!: remove EngineStatus Enum`

### Description

The `description` contains a concise description of the change.

* Description is **mandatory**
* **Do not** capitalise first letter
* **Do not** end the description with `,` or `.`

### Body

The `body` should include the motivation for the change.

* This can be a reference to a PR or Issue that is open.
* Since we use Codeberg as our `origin` repository, PRs and Issue references are rendered like this `(#issue-number)`.

### Footer

* `footer` is optional for most commits
    * Only a `breaking:` commit requires a footer with the following:
        * `BREAKING CHANGE: <description>`

---

## Versioning

As per the [Semantic Versioning] standard, we follow the `MAJOR.MINOR.PATCH` process.

* A release containing a **breaking** change in the codebase, bumps `MAJOR`
* A release containing relevant changes, like `feat:`, bumps `MINOR`
* A release containing bug fixes, typos and any or small fixes, bumps `PATCH`

On top of this, as defined by [Semantic Versioning], `PATCH` only increments if the changes
are backward compatible with existing releases - otherwise `MAJOR` increments and `MINOR` and `PATCH` reset to 0.

### Other SemVar considerations

We also follow the SemVar spec for additional metadata being included in version labels.

* Pre-release
  * Non-final releases, such as `release-candidate` types, use `-` at the end of a version number before indicating release state
  * For example: `4.0.0-alpha`, `4.0.0-rc-1`
* Build metadata
  * Occasionally, we may also use `+` in the version number
  * This is if a patch or pre-release version has additional fixes
  * Precedence voids use of build metadata in the version number
  * For example: `4.0.0-alpha+001`
* Precedence
  * This refers to how versions are compared to each other when ordered.
  * We follow the SemVar spec of ordering from left to right
  * For example: `2.0.0 < 3.0.0 < 3.9.0 < 4.0.0`

---

[Contributing]: CONTRIBUTING.md
[Semantic Versioning]: https://semver.org
