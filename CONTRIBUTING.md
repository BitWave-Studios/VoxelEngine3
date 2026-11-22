# VoxelEngine3 Contributor Guide

This document describes the development guidelines for VoxelEngine3.

> [!IMPORTANT]
> This may be amended at any time.
> You are expected to refer to the development guidelines when working on any contributions.

First of all, thank you for wanting to support this project and taking the time to read this document.
Whether you're reporting a bug, suggesting a new feature, or just looking through the repository,
we appreciate it.


Following these guidelines should ensure your contributions to the project are quick to review, consistent
with other code in the repository and well thought out. This document should not be seen as a strict
set of rules, but instead the thought process that a VE3 team member would consider during the design,
implementation and review of contributions to the project.

Old code or parts thereof might not yet be up to the standards defined by these guidelines. When working
with old code, try to adhere to these guidelines, but don't bulk update legacy code to match them.
The team will handle updating older code to match the newer standards when appropriate.

With that said, let's dive into how *you* could contribute to the project.

---

## Ways to help
* **Reporting a bug**
  * Open an [Issue] on the repository and describe it plainly:
    * What you did
    * What you expected
    * What actually happened instead
  * It would also help to include a snippet of the code that failed, or a crash log
* **Flag a problem with our website**
  * There may be occasions where we miss a typo, broken link or other issues with our website
  * Letting us know, helps us keep track and correct it quickly.
* **Suggesting an idea or improvement**
  * Open an [Issue] and explain your idea or suggestion
  * If a similar enough Issue already exists, yours may be closed and forwarded to the existing one.
    * This is to prevent Issue spamming on Codeberg.
* **Any questions**
  * You can contact us via the [Discord] and either message our ModMail bot or post in the `questions` channel.
* **Prefer using English**
  * As much as we appreciate that not everyone has English as their first language, it makes things a lot easier for us.
  * As per our [AI Policy], we allow the use of Assistive tools to translate your message.
* **Report a security problem**
  * Please, do not open a public issue for this.
  * We prefer security risks and issues being handled via our ModMail bot on Discord.
  * This ensures the security risk is kept at a minimum and can be resolved quickly.

## Pull Requests

> [!TIP]
> For PRs that are beyond a small fix, we urge you to open an [Issue] first.
> 
> _This is so we save you the hassle of reporting something that may have been an intentional design._

Upon opening a PR, you should expect a comment or reply from a developer in the team within a day.
> You can send a message via ModMail if your PR has been forgotten or missed for more than 3 days.

If the changes in the PR are important and worth doing:
* One change in the codebase, is one pull request.
  * That means we expect one feature change (add or remove) per PR, regardless of how many files are affected.
* Keep the changes minimal.
  * A PR is read and merged quicker if the changes are simple, related to the label used and is easy to locate.
    * You can make it easier by linking the exact commit message of your changes.
* Add a code comment to explain _why_ you made the change, not what you did.
  * Since we wrote the original code, we know what it does. We want to know _why_ you want to change it.
* Use conventional commit prefixes: `feat`, `fix`, `chore`, `ci`, `docs`, etc.
  * If you're unsure, you can read our [Standards and Design] spec.

## Using AI in your Contribution
As per our [AI Policy], we allow the use of AI to assist you with writing or understanding the code.

To put simply:
* **PRs require an AI usage declaration**
  * When you open a PR with changes made with the help of an AI assistant, we expect you to use the correct label.
* **In your own words**
  * When you write the title, description or any answers to questions we may have, we expect you to answer in your own words.
* **Understand and Test your code**
  * While we understand if Java, GLSL or another language used in the VE3 Platform may not be your strongest programming language,
    * we expect you to be able to understand and test the code, to ensure the quality fits the codebase before publishing.

## Patience is very much appreciated
The VE3 Platform is a large project spanning multiple repositories, majority of which is maintained by one or 
two developers at any point. We may not always be able to respond to a PR, Issue or a ModMail message as 
soon as it is sent.

If something is urgent, it may help to put something like `urgent` in the title - abusing this to distract a developer
from another task is strictly prohibited.

Otherwise, we appreciate your patience.

---

[Standards and Design]: #
[AI Policy]: https://www.voxelengine3.com/policies
[Issue]: https://codeberg.org/Leokayasen/VoxelEngine3/issues
[Discord]: https://discord.gg/6EZx8VaHWJ
