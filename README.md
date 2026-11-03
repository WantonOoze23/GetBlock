# GetBlock

GetBlock is an Android application for exploring live Solana blockchain data through the GetBlock JSON-RPC service. It presents current epoch and SOL supply information alongside recent blocks, and lets users inspect block details or look up a slot within the current epoch.

## Screenshots

<!-- Replace these placeholders with captured application screenshots. -->
<table>
  <tr>
    <td><img src="/docs/images/screen1.png" alt="GetBlock home screen" width="240"></td>
    <td><img src="/docs/images/screen2.png" alt="GetBlock block details screen" width="240"></td>
    <td><img src="/docs/images/screen3.png" alt="GetBlock block list" width="240"></td>
  </tr>
</table>

## Architecture

GetBlock is a **single-module Android app with an MVVM-style presentation flow**. `MainActivity` creates a `GetBlockViewModel`; the Compose screens observe its `StateFlow<UiStack>`, while the ViewModel uses `RpcRepository` to retrieve and map RPC results into UI state. The code is organized into UI, ViewModel, repository, and model packages, but these are packages inside `:app` rather than independently buildable Clean Architecture modules. There is no separate domain module or dependency-injection framework in the current project.

### Modules and package boundaries

| Module / package | Responsibility | Dependencies and provides |
| --- | --- | --- |
| `:app` | The only Gradle module; Android application configuration, manifest, resources, and application entry point. | Builds the `com.tyshko.getblock` app. It contains all runtime code and depends on AndroidX/Compose, Ktor, and Kotlin serialization libraries. |
| `com.tyshko.getblock` | `MainActivity` and Compose navigation between the main and block-detail destinations. | Creates the ViewModel and connects the navigation host to the UI. |
| `com.tyshko.getblock.view` | `GetBlockViewModel` owns polling, RPC-result mapping, and screen state exposed as `StateFlow<UiStack>`. | Uses `RpcRepository`; provides state and actions consumed by Compose UI. |
| `com.tyshko.getblock.data` | `RpcRepository` performs JSON-RPC requests; `HTTPRouts` holds the service URL and API-key configuration. | Uses Ktor HTTP, content negotiation, logging, and serialization; returns typed RPC responses. |
| `com.tyshko.getblock.models` | Serializable RPC response and domain-shaped data classes, plus `Block` and `UiStack` presentation state. | Shared by the data, ViewModel, and UI code. |
| `com.tyshko.getblock.ui` | Compose screens, reusable components, and theme. | Observes ViewModel state and forwards user actions; should not own network transport. |

**Dependency direction in the current implementation:** Compose UI → `GetBlockViewModel` → `RpcRepository` → GetBlock JSON-RPC endpoint. Models are shared across these packages. These boundaries are organizational rather than enforced by separate Gradle modules; the ViewModel currently constructs the repository directly.

![Architecture Diagram](docs/images/architecture_diagram.png)

## Tech Stack

Versions are managed in `gradle/libs.versions.toml`.

| Category | Technologies |
| --- | --- |
| **Language and build** | Kotlin 2.0.0, Kotlin DSL, Gradle Wrapper 8.11.1, Android Gradle Plugin 8.9.0 |
| **UI** | Jetpack Compose, Compose Material 3, AndroidX Activity Compose, Compose Navigation 2.8.9 |
| **Architecture** | AndroidX Lifecycle Runtime KTX 2.8.7, `ViewModel`, `viewModelScope`, Kotlin `StateFlow` |
| **Dependency injection** | No DI library is configured; `GetBlockViewModel` creates `RpcRepository` directly. |
| **Remote data** | Ktor Client 3.1.0 (Core, CIO engine, Logging, Content Negotiation), Kotlinx Serialization JSON 1.6.3; GetBlock Solana JSON-RPC API |
| **Local data** | No database or local persistence library is configured. |
| **Security** | The app has the Android `INTERNET` permission. No dedicated security or secret-management library is configured. The RPC URL/API key are currently defined in `data/HTTPRouts.kt`; avoid committing production credentials and rotate any credential that has been exposed. |
| **Asynchrony** | Kotlin coroutines through lifecycle `viewModelScope`, with `StateFlow` for observable state. |
| **Testing** | JUnit 4.13.2, AndroidX Test JUnit 1.2.1, Espresso 3.6.1, and Compose UI testing dependencies. |

## Setup & Build Instructions

### Requirements

- **JDK 17** (required by Android Gradle Plugin 8.9.0).
- Android Studio with support for **AGP 8.9.0** and Android SDK Platform **35**. Install the Android SDK Build-Tools/platform components requested by Android Studio or Gradle.
- An Android device or emulator running **API 28 or newer** (`minSdk = 28`) to install and run the app.
- Network access for the app's remote JSON-RPC requests.

### Clone and build

```bash
git clone https://github.com/WantonOoze23/GetBlock.git
cd GetBlock
./gradlew assembleDebug
```

On Windows, use `gradlew.bat` instead of `./gradlew`. The wrapper downloads the configured Gradle 8.11.1 distribution when needed.

### Run

Start an API 28+ emulator or connect a device with USB debugging enabled, then install and launch the debug build from Android Studio. To install from the command line:

```bash
./gradlew installDebug
```

Open the `GetBlock` project in Android Studio and select the `app` run configuration to launch it interactively. The app uses its configured GetBlock RPC endpoint for blockchain data; ensure valid credentials are configured before making requests.

## Testing

The repository includes standard sample local and instrumented tests (`ExampleUnitTest` and `ExampleInstrumentedTest`). Run them with:

```bash
# Local JVM unit tests
./gradlew testDebugUnitTest

# Instrumented tests (requires a running emulator or connected device)
./gradlew connectedDebugAndroidTest
```

The checked-in tests currently provide basic template coverage; add feature-specific tests as application behavior grows.
