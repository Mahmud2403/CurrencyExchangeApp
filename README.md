# Currency Exchange Android App

This is an Android app that allows users to exchange currencies, check their balances, and view available accounts. It utilizes Kotlin, Jetpack Compose, and various Android libraries for a smooth and efficient user experience.

## Features

- **Currency Conversion**: Convert between different currencies using real-time exchange rates.
- **Transaction History**: View available accounts and balances after each transaction.
- **User-Friendly UI**: Simple and interactive user interface built using Jetpack Compose.
- **Error Handling**: Show appropriate error messages when funds are insufficient.

## Tech Stack

- **Kotlin**: The main programming language.
- **ЧXML**: For building the UI.
- **Dagger 2**: For dependency injection.
- **Room**: For local data storage.
- **Retrofit**: For making network requests.
- **RxJava**: For reactive programming.
- **ViewBinding**: For binding UI elements.
- **ViewPager2**: For showing currency options in a swipeable view.

## Architecture

The app follows the **MVI (Model-View-Intent)** and **MVVM** architectural patterns for clean separation of concerns.

- **Model**: Holds the app data and business logic (view models, repositories, etc.).
- **View**: UI components built using Jetpack Compose, handling user interactions.
- **Intent**: User actions that are interpreted and processed by the app.

## Setup

### Prerequisites

- **Android Studio**: Make sure you have the latest version of Android Studio.
- **Kotlin**: This project uses Kotlin 1.5 or newer.
- **Gradle**: Ensure you're using the correct Gradle version for building the project.

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/currency-exchange-android.git
   ```

2. **Open in Android Studio**:
   - Open Android Studio and choose "Open an existing project".
   - Select the directory where the project was cloned.

3. **Sync Gradle**:
   - After opening the project, Android Studio will prompt you to sync Gradle files. Accept the prompt to download the necessary dependencies.

4. **Build and Run**:
   - Click **Build** > **Make Project**.
   - Select your emulator or a physical device and run the project.

## How to Use

1. **Currency Selection**: Swipe between source and target currencies using the `ViewPager2`.
2. **Amount Input**: Enter the amount you wish to exchange.
3. **Exchange**: Tap the "Exchange" button to initiate the currency conversion.
4. **View Balance**: After a successful exchange, the available balance in the target currency is shown in a dialog.

## Dependencies

- **XML**: For building modern UIs.
- **Room**: For local data storage.
- **Dagger 2**: For dependency injection.
- **Retrofit**: For making network requests to fetch exchange rates.
- **OkHttp**: For networking.
- **RxJava**: For handling asynchronous operations.
- **ViewBinding**: For binding views without using `findViewById`.

## Error Handling

- **Insufficient Funds**: If the user doesn't have enough funds in their account, a snackbar message will be shown.
- **Network Errors**: If there are issues with the network, appropriate error messages will be displayed to the user.

## Contributing

1. **Fork the repository**.
2. **Create a new branch**: `git checkout -b feature-branch`.
3. **Commit your changes**: `git commit -am 'Add new feature'`.
4. **Push to your branch**: `git push origin feature-branch`.
5. **Create a new pull request**.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
