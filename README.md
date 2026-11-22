# VoxelEngine 3

A game engine for blocky, voxel based games.

---

## Overview
### What is VoxelEngine3?
VoxelEngine is a (hint in the name) game engine for voxel based games.
It's built in Java and uses LWJGL for OpenGL rendering, OpenAL for audio, and GLFW for window management.

We're also using JOML for math and eventually adding OpenGL ES for mobile support.

**NOTE:** The GUI system used in this project, is entirely custom-built - this is separate to the application that will
be released later in development.

> You can find the full list of dependencies in the [Build Script](build.gradle.kts).

### What are the requirements to use VE3?

We recommend using any Java SDK that supports 21 or higher, as this is the minimum version required to run VE3.

---

## Getting Started - Contributing
You can fetch the repository through our main worktree on Codeberg, or through our GitHub mirror.

Codeberg:
```shell
git clone https://codeberg.org/Leokayasen/VoxelEngine3.git
```

GitHub:
```shell
git clone https://github.com/BitWave-Studios/VoxelEngine3.git
```

1. Read through the code, get yourself familiarised with the basic functionality.
2. Make a new branch with the prefix `feat/` - you can see more prefix suggestions in [Contributing]
3. Add your new feature, fix or rework of the engine.
   * If it's a complete overhaul or refactor, you should fork the repository instead.
4. When it's ready and you have read the checklist in the [Contributing] doc, you can open a PR.

You are welcome to fork and use the code provided as a reference or as part of your learning.
As far as the limits of the license mentioned below, which is included in this project and all related in the VE3 Platform,
we cannot be liable for any claim, damages or other liability of any kind.

> [!IMPORTANT]
> If you choose to use AI to aid you with developing a new feature, to which you intend to open a
> Pull Request to be reviewed, we expect you to understand how the code works, what it affects,
> what it adds or changes.
>
> We do not condone the use of AI to 100% vibecode contributions.

---

## Contact & Support
Our [Discord] is open to anyone who wants to follow the development, receive updates and 
chat with the team @ BitWave Studios.


> [!IMPORTANT]
> Found a security issue? Do not open a public issue, see [Security] for how to report it privately.

---
This project would not have been possible without the endless documentation, tools and plugins provided
by and with these tools:

#### Language
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

#### Framework
![OpenGL](https://img.shields.io/badge/opengl-%235586A4.svg?style=for-the-badge&logo=opengl&logoColor=white)

---

## License
This project, along with any connected subprojects like the website, docs or any public APIs developed, are
subject to the MIT license.

```
Copyright © 2026 BitWave Studios

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without 
limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of 
the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS IN THE SOFTWARE.

```

---
[Contributing]: CONTRIBUTING.md
[License]: https://www.voxelengine3.com/license
[Security]: SECURITY.md
[Discord]: https://discord.gg/6EZx8VaHWJ
