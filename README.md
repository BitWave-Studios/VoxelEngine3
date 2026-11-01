# VoxelEngine3

---
> All previous iterations of VoxelEngine are archived and unavailable as of 27/08/2026.
---

## Overview
### What is VoxelEngine3?
VoxelEngine is a (hint in the name) game engine for blocky, voxel based games.

Initially a fun experiment to make a Minecraft-like game, that has since evolved into 
an attempt at a fully fledged game engine. There have been 2 other iterations of the project so far, dating as
far back as mid-2024, though none of the older versions are available as stated at the top of this readme.

VoxelEngine3, or VE3, is a culmination of the lessons learned from previous iterations and is built to be a
better, more-complete version of the engine.

### What are the requirements to use VE3?
v1 of VE3 will support Windows x64 and x86 first. This is priority due to my own hardware using this architecture.

Eventually, the intention is to reach support for the following:

| OS      | Natives       | Arch  |
|---------|---------------|-------|
| Windows | Windows x64   | x64   |
| Windows | Windows x86   | x86   |
| Windows | Windows arm64 | arm64 |
| Linux   | Linux x64     | x64   |
| Linux   | Linux x86     | x86   |
| Linux   | Linux arm64   | arm64 |
| Linux   | Linux arm32   | arm32 |
| macOS   | macOS x64     | x64   |
| macOS   | macOS arm64   | arm64 |

---

## Engine Specs
- LWJGL 3.4.3
- JOML v1.10.9
- JOML Primitives v1.10.0
- FMOD
- FreeType
- GLFW 
- meshoptimizer
- Nuklear
- OpenAL
- OpenCL
- OpenGL / Vulkan (the final decision on graphics renderer hasn't been made yet)
- stb

---

## License
This project, along with any connected sub-projects like the website, docs or any public APIs developed, are
subject to the MIT license.

```
Copyright © 2026 Kai Leopold

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
