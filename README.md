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

Looking for help? Join the discussion in the [KotlinLang Slack](https://kotlinlang.slack.com/archives/C08R5V4EHDH)!

### Terminology

This library is all about building _**storyboards**_.
A storyboard is analogous to a slide show in other presentation software.
While other software may have _**slides**_ with _**advancements**_,
Storyboard uses the terms _**scenes**_ and _**states**_ respectively.
Building on the _**state**_-driven nature of Compose UI,
Storyboard provides a _**state**_-based way to build your presentation,
and requires each scene to provide a list of states.
When a storyboard advances, it simply moves to the next state specified for your scene,
or the first state of the next scene.

For the purpose of rendering, the states of a scene are converted into _**frames**_.
If a scene comes after another scene, a _**start**_ frame will be added as the first frame.
If a scene comes before another scene, an _**end**_ frame will be added as the last frame.
Frames are used to control the transitions between scenes and states.

For example, imagine there is a storyboard with three scenes, each with one, three, and two states respectively.
The frames of this storyboard could be displayed as follows:

```text
1-1, 1-End, 2-Start, 2-1, 2-2, 2-3, 2-End, 3-Start, 3-1, 3-2
```

This sequence of frames is what controls the advancement of a storyboard.
Start and end frames are only used for intermediate rendering when transitioning between scenes.
This enables start and end animations for a scene and smooth transitions between scenes.
This also enables the use of scenes which do not have any states to create more complex transitions.

### Compose

Along with defining a list of states, [a scene must also define a `@Composable` lambda][StoryboardBuilder],
which takes a [`SceneScope`][SceneScope] as a receiver.
The SceneScope providers a number of properties to the scene Composable,
including a [`Transition`][Transition] instance that defines the current and target frames.
This allows animating values between the states of the scene and synchronizing with the storyboard advancement.
And because of the start and end frame, scene start and end animations are also easy to achieve.

An [`AnimatedVisibilityScope`][AnimatedVisibilityScope] and [`SharedTransitionScope`][SharedTransitionScope] are also
provided by via context parameters.
These scopes are provided for animating and sharing elements between scenes.

Along with the Composable lambda and states, a scene is also able to define a enter and exit transition.
These transitions determine how the scene transitions into view
and can be based on the current advancement direction of the storyboard.
For example, these transitions can be used to create a carousel like animation between scenes.

[When building a storyboard][Storyboard], a custom `DpSize` may be provided; otherwise the default size is used.
The `DpSize` of the storyboard determines the _**constant render size**_ of all scenes.
This makes it so the scenes of the storyboard automatically scale according the available space,
while maintaining a constant pixel space for defining the size and spacing of elements.

A [SceneDecorator][SceneDecorator] may also be provided when building a storyboard.
This decorator will be applied around all scenes
and can be used to adjust theming, provide composition locals, or add a shared background.

## Libraries

There are two main libraries in Storyboard: Storyboard and Easel.
The Storyboard library contains the building blocks for Storyboards,
while Easel contains UI components which make Storyboard usable on desktop and web.

```kotlin
dependencies {
    implementation("dev.bnorm.storyboard:storyboard")
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
* **(Re)creating Magic(Move) with Compose** _(droidcon NYC 2025)_ - [_Coming Soon!_](https://nyc.droidcon.com/brian-norman/)

[//]: # (Storyboard Links)

[SceneScope]: /storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/SceneScope.kt
[SceneDecorator]: /storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/SceneDecorator.kt
[Storyboard]: storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/Storyboard.kt
[StoryboardBuilder]: storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/StoryboardBuilder.kt

[//]: # (Compose Links)

[compose]: https://www.jetbrains.com/lp/compose-multiplatform

[AnimatedVisibilityScope]: https://developer.android.com/reference/kotlin/androidx/compose/animation/AnimatedVisibilityScope
[SharedTransitionScope]: https://developer.android.com/reference/kotlin/androidx/compose/animation/SharedTransitionScope
[Transition]: https://developer.android.com/reference/kotlin/androidx/compose/animation/core/Transition
