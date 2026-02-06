# Storyboard

A [Compose Multiplatform][compose] library for building presentations.
Inspired by [reveal.js](https://github.com/hakimel/reveal.js),
[Spectacle](https://github.com/FormidableLabs/spectacle),
and many others!

> ## sto·ry·board
> 
> A sequence of drawings, typically with some directions and dialogue, 
> representing the shots planned for a movie or television production.

## Usage

As a Compose Multiplatform library, Storyboard requires the [JetBrains Compose plugin][compose-compiler].
With the Compose plugin present, Storyboard can be used just like any other library.

```kotlin
dependencies {
    implementation("dev.bnorm.storyboard:storyboard:0.1.0-alpha03")
    implementation("dev.bnorm.storyboard:storyboard-easel:0.1.0-alpha03")
    implementation("dev.bnorm.storyboard:storyboard-text:0.1.0-alpha03")
}
```

<details>
<summary>Snapshots of the latest version are available in the Central Portal Snapshots repository.</summary>
<p>

```kotlin
repository {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
}

dependencies {
    implementation("dev.bnorm.storyboard:storyboard:0.1.0-SNAPSHOT")
    implementation("dev.bnorm.storyboard:storyboard-easel:0.1.0-SNAPSHOT")
    implementation("dev.bnorm.storyboard:storyboard-text:0.1.0-SNAPSHOT")
}
```

Snapshot documentation is available at [bnorm.github.io/storyboard/docs/api/latest/](https://bnorm.github.io/storyboard/docs/api/latest/).

</p>
</details>

## Examples

[Various examples](examples) are available to demonstrate how to build Storyboards of
varying levels of complexity and functionality.

## Getting Started

Looking for help? Join the discussion in the [KotlinLang Slack](https://kotlinlang.slack.com/archives/C08R5V4EHDH)!

### Terminology

This library is all about building _**storyboards**_.
A storyboard is analogous to a slide show in other presentation software.
While other software may have _**slides**_ with _**advancements**_,
Storyboard uses the terms _**scenes**_ and _**states**_ respectively.
Building on the _**state**_-driven nature of Compose UI,
Storyboard provides a _**state**_-based way to build your presentation
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

Along with the Composable lambda and states, a scene is also able to define enter and exit transitions.
These transitions determine how the scene transitions into view
and can be based on the current advancement direction of the storyboard.
For example, these transitions can be used to create a carousel like animation between scenes.

[When building a storyboard][Storyboard], a custom [SceneFormat][SceneFormat] may be provided;
otherwise the default format is used.
The scene format is a combination of `DpSize` and `Density`
which determines the _**constant render size**_ of all scenes.
This makes it so the scenes of the storyboard automatically scale according to the available space,
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

There are also some experimental libraries that hold common utilities for building storyboards.

* _**storyboard-text**_ - Contains utilities for complex text animations and code rendering.
* _**storyboard-diagram**_ - Todo: utilities for drawing diagrams with shapes and arrows.

## References

Want to see Storyboard in action?

* **Kotlin + Power-Assert = Love** _(KotlinConf 2024)_ - 
  [Storyboard](https://deck.bnorm.dev/kotlinconf2024),
  [Code](https://github.com/bnorm/deck.bnorm.dev/tree/kotlinconf2024/kotlinconf2024),
  [Recording](https://www.youtube.com/watch?v=N8u-6d0iCiE)
* **Writing Your Third Kotlin Compiler Plugin** _(KotlinConf 2025)_ - 
  [Storyboard](https://deck.bnorm.dev/kc25),
  [Code](https://github.com/bnorm/deck.bnorm.dev/tree/kc25/kotlinconf2025/story),
  [Recording](https://www.youtube.com/watch?v=9P7qUGi5_gc)
* **(Re)creating Magic(Move) with Compose** _(droidcon NYC 2025)_ -
  [Storyboard](https://deck.bnorm.dev/dcnyc25),
  [Code](https://github.com/bnorm/deck.bnorm.dev/tree/dcnyc25/dcnyc25/story),
  [Recording](https://www.youtube.com/watch?v=PgzBWebeJsk)

[//]: # (Storyboard Links)

[Storyboard]: storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/Storyboard.kt
[StoryboardBuilder]: storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/StoryboardBuilder.kt
[SceneDecorator]: /storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/SceneDecorator.kt
[SceneFormat]: /storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/SceneFormat.kt
[SceneScope]: /storyboard/src/commonMain/kotlin/dev/bnorm/storyboard/SceneScope.kt

[//]: # (Compose Links)

[compose]: https://www.jetbrains.com/lp/compose-multiplatform
[compose-compiler]: https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-compiler.html

[AnimatedVisibilityScope]: https://developer.android.com/reference/kotlin/androidx/compose/animation/AnimatedVisibilityScope
[SharedTransitionScope]: https://developer.android.com/reference/kotlin/androidx/compose/animation/SharedTransitionScope
[Transition]: https://developer.android.com/reference/kotlin/androidx/compose/animation/core/Transition
