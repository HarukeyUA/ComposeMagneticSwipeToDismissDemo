# Magnetic Swipe to Dismiss (KMP)

A visually stunning, high-performance Magnetic Swipe to Dismiss interaction for **Compose Multiplatform**. This project showcases fluid, physics-based animations inspired by Material 3's expressive design principles, where list items react dynamically to user interaction.

## Features

- **Magnetic Interaction**: Neighboring items follow the swiped item's movement with varying intensity, creating a magnetic field effect.
- **Dynamic Roundness**: Items dynamically adjust their corner radius as they are pulled away from the list.
- **Neighbor Squish**: When an item is dismissed, neighboring items exhibit a subtle "squish" animation, filling the space organically and responsively.
- **Multiplatform**: Single codebase targeting Android, iOS, Desktop, and Web.

## Platforms

This project targets the following platforms using Kotlin Multiplatform:

- **Android**
- **iOS**
- **Desktop (JVM)**
- **Web (Wasm)**

## Getting Started

### Prerequisites

- **JDK 17** or higher
- **Android Studio**
- **Xcode** (for iOS development, macOS only)

### Build and Run

**Android**
```shell
./gradlew :composeApp:assembleDebug
```

**Desktop**
```shell
./gradlew :composeApp:run
```

**Web (Wasm)**
```shell
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

**iOS**

- Use [Kotlin Multiplatform Intellij Plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform)
  
or

- Open `iosApp/iosApp.xcodeproj` in Xcode and run the application.

## Technologies Used

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Material 3](https://m3.material.io/)

## Demo

[Try it out yourself with the Web Demo](https://harukeyua.github.io/ComposeMagneticSwipeToDismiss/)


```
Copyright 2025 HarukeyUA

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```