# Android Unit Converter App

## Overview
This app is a single-activity Android application that converts between various units of measure (Weight, Volume, Length). It demonstrates two different architectural patterns side-by-side: **MVP (Model-View-Presenter)** and **MVVM (Model-View-ViewModel)** within the same app. The user can navigate to either implementation from a common Home screen.

## Features
- Convert between **Kilograms and Pounds** (weight units).
- Convert between **Litres and Gallons** (volume units).
- Convert between **Yards and Meters** (length units).
- Identical user interface for both MVP and MVVM screens for consistency (input field, unit selection spinners, convert button, and result display box).
- Proper error handling for invalid inputs or incompatible unit conversions.
- Preserves the conversion result on screen during device rotation in the MVVM implementation.

## Architecture
This project follows Clean Architecture principles with clear separation of concerns:
- **Domain Layer:** Contains the core logic, including the `UnitType` definitions and a use-case class (`ConvertUnitsUseCase`) that performs unit conversion. This layer defines an abstract `ConversionRepository` to obtain conversion factors.
- **Data Layer:** Provides the data for conversions. We implement `ConversionRepository` in `ConversionRepositoryImpl`, which defines the conversion ratios for each unit relative to a base unit (e.g., 1 Kg, 1 Litre, 1 Meter as base units).
- **Presentation Layer:** Contains the Android UI and presentation logic. This includes:
  - `HomeFragment` (the main menu with navigation buttons).
  - `MVPFragment` and `UnitConverterPresenter` (MVP architecture: the fragment is the View and the Presenter handles input validation and uses the domain use-case to perform conversions).
  - `MVVMFragment` and `ConverterViewModel` (MVVM architecture: the fragment observes LiveData from the ViewModel, which performs input validation and conversions using the domain use-case).
- **Framework Layer:** Android-specific components and utilities, such as `MainActivity` (host for fragments) and ViewBinding setup for layouts.

The solution adheres to SOLID principles:
- **Single Responsibility:** Each class has a focused responsibility (e.g., the Presenter only handles presentation logic, the ViewModel only handles UI state, the UseCase only performs conversion calculations, etc.).
- **Open/Closed:** The use of interfaces (e.g., `ConversionRepository`) allows easy extension or modification of data sources without changing domain logic.
- **Liskov Substitution:** The Presenter and ViewModel both rely on the same domain logic via the use-case, and the View can switch between MVP or MVVM without altering the domain layer.
- **Interface Segregation:** The MVP view and presenter interfaces (`UnitConverterView` and `UnitConverterPresenter`) are narrowly defined for the unit conversion feature.
- **Dependency Inversion:** The higher-level layers (presentation/domain) depend on abstractions. The domain layer defines `ConversionRepository` and the data layer provides the implementation, which is injected into the use-case.

## UI Implementation
The user interface is designed to match the provided screenshots:
- A **Home screen** (`HomeFragment`) with two buttons ("MVP Converter" and "MVVM Converter") for navigation.
- Both converter screens have the same layout:
  - An input field for the value to convert (with a hint "Enter value").
  - Two spinners for selecting the "From" unit and "To" unit (populated with "Kg", "Pound", "Litre", "Gallon", "Yard", "Meter").
  - A "Convert" button to perform the conversion.
  - A result display box that shows the converted value or an error message. This result TextView uses a background drawable that makes it appear like an output textbox (for example, using the EditText style background for a familiar look).
- The app uses consistent padding and alignment as per the design, and default Android widget styles for a clean appearance.

ViewBinding is enabled and used in all fragments and the activity, eliminating manual `findViewById` calls and making UI code safer and easier to maintain.

## Error Handling
- In the MVP screen, **invalid input** (empty or non-numeric input) or attempts to convert between incompatible units (e.g., weight to length) are communicated via a Toast message. The result field remains unchanged if an error occurs (the previous result stays on screen until a new valid conversion is done).
- In the MVVM screen, errors are shown **inline**: the result text box itself will display the error message (in red text) instead of a numerical result. This provides immediate feedback within the UI without using a Toast.
- Both implementations validate inputs before conversion to prevent crashes or undefined behavior. 

## Orientation Support
The MVVM implementation leverages Android's ViewModel to handle configuration changes. The `ConverterViewModel` is scoped to the activity, so it survives rotations. This means:
- After rotating the device on the MVVM converter screen, the last conversion result or error message will still be displayed (the LiveData retains its value and the new fragment instance picks it up).
- In the MVP implementation, rotating the device will recreate the fragment and its Presenter. The conversion result will reset to an empty/default state, as the MVP architecture does not retain state by default (without additional code). The app will not crash, but the user will need to re-enter the value to convert again on rotation.

## Kotlin Features Used
- **Coroutines:** The MVVM ViewModel uses Kotlin coroutines (`viewModelScope.launch`) to perform the conversion calculation on a background thread (Dispatchers.Default) and then updates the LiveData on the main thread. This ensures that any heavy computation or future data fetching for conversion (if extended) won't block the UI.
- **Kotlin Scope Functions:** Functions like `apply` are used for cleaner initialization. For example, when setting up the spinner adapter, we use `adapter.apply { setDropDownViewResource(...) }` for brevity. Similarly, null-checks for numeric parsing use Kotlin's safe-call and Elvis operator in a clear way.
- **ViewBinding:** Used throughout to bind UI elements, which improves code safety and readability.

## Author
**Rohan Nagpure**
