# Storyboard

Library for building presentations using [Compose Multiplatform][compose].

> ## sto·ry·board
> 
> A sequence of drawings, typically with some directions and dialogue, 
> representing the shots planned for a movie or television production.

## Examples

[Various examples](examples) are available to demonstrate how to build Storyboards of
varying levels of complexity and functionality.

### GitHub Pages

Live versions of the example storyboards are available via GitHub Pages.
To see how publishing is achieved, see the [`pages.yml` workflow](.github/workflows/pages.yml).

* [Basic](https://bnorm.github.io/storyboard/example/basic)
* [Interactive](https://bnorm.github.io/storyboard/example/interactive)

## Getting Started

> [!WARNING]
> Storyboard is under active development and will not be officially released until Kotlin 2.2.0,
> so context parameters may be used in the API design.
> There is also a Compose animation bug that should be fixed in the Compose Multiplatform 1.8.0 release.

### Terminology

This library is all about building _**storyboards**_.
A storyboard is analogous to a slide show in other presentation software.
While other software may have _**slides**_, Storyboard has two different terms it uses.

When building a storyboard, you will create a sequence of _**scenes**_,
and each scene may contain any number of _**states**_.
The states of a scene is just a list of class instances, specified when creating the scene.
This combination of _**scenes**_ and _**states**_ is analogous to _**slides**_ in other presentation software.

For the purpose of rendering, the states of a scene are converted into _**frames**_.
If a scene comes after another scene, a _**start**_ frame will be added as the first frame.
If a scene comes before another scene, an _**end**_ frame will be added as the last frame.

For example, there is a storyboard of three scenes with one, three, and two states respectively.
The frames of the storyboard could be displayed as the following:

```text
1-1, 1-End, 2-Start, 2-1, 2-2, 2-3, 2-End, 3-Start, 3-1, 3-2
```

A storyboard is rendered based on the current frame, but states determine when a storyboard pauses.
Start and end frames are only used for intermediate rendering when transitioning between scenes.
This enables start and end animations for a scene and smooth transitions between scenes.
This also enables the use of scenes which do not have any states to create more complex transitions.

### Libraries

There are two main libraries in Storyboard: Core and Easel.
The Core library contains the building blocks for Storyboards,
while Easel contains UI components which make Storyboard usable on desktop and web.

```kotlin
dependencies {
    implementation("dev.bnorm.storyboard:storyboard-core")
    implementation("dev.bnorm.storyboard:storyboard-easel")
}
```

There are also some experimental libraries which hold common utilities for building storyboards.

* _**storyboard-text**_ - Contains utilities for complex text animations and code rendering.
* _**storyboard-diagram**_ - Todo: utilities for drawing diagrams with shapes and arrows.

## Try It Out!

While there are no releases of Storyboard yet,
there are still ways to try it out and provide feedback:

### 1. Examples

I welcome you to check out the repository and try running one of the examples.
I would also welcome additional examples of complex layouts or animations.
Please experiment and see what's possible!

### 2. Git Submodule

(This is the setup I use for my own presentation development)

* Add the Storyboard repository as a Git Submodule or clone it to a local directory.

```
git submodule add -- https://github.com/bnorm/storyboard.git storyboard
```

* Include the Storyboard submodule in your Gradle build.

```kotlin
includeBuild("storyboard")
```

## References

Want to see Storyboard in action?

* **Kotlin + Power-Assert = Love** _(KotlinConf 2024)_ - [YouTube](https://www.youtube.com/watch?v=N8u-6d0iCiE)
* **Writing Your Third Kotlin Compiler Plugin** _(KotlinConf 2025)_ - [_Coming Soon!_](https://kotlinconf.com/schedule/?session=9df8d3fd-5dc8-5d72-a362-c83079285174)

[//]: # (Links)
[compose]: https://www.jetbrains.com/lp/compose-multiplatform
