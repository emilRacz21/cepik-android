# cepik-android

`cepik-android` is an Android application that integrates with the **Central Vehicle Register (CEPiK)** API to provide users with information about registered vehicles in Poland. The app allows users to search for vehicle details by car ID or apply filters like brand, model, year, fuel type, and registration dates. The application retrieves and displays data about registered vehicles, helping users easily access vehicle-related information.

## Features

- **Vehicle Search by ID**: Search for a specific vehicle using its unique ID.
- **Vehicle Search by Filters**: Filter vehicles by brand, model, year of manufacture, fuel type, and registration dates.
- **Display Vehicle Information**: View detailed information such as the brand, model, fuel type, registration details, and more.
- **Copy Vehicle ID**: Copy the vehicle ID to the clipboard for easy use.

## Technologies Used

- **Android SDK**: The app is built using the Android SDK, utilizing Java for development.
- **Volley**: Used for handling HTTP requests and interacting with the CEPiK API.
- **CEPik API**: Data related to registered vehicles in Poland is fetched from the official CEPiK API.

## Setup and Installation

To run the app locally, follow these steps:

1. Clone the repository to your local machine:
    ```bash
    git clone https://github.com/emilRacz21/cepik-android
    ```

2. Open the project in **Android Studio**.

3. Ensure your project is configured to use the appropriate version of the Android SDK.

4. Build and run the application on your emulator or physical device.

## API Endpoints Used

The application integrates with the following CEPiK API endpoints:

- **Vehicles by ID**:  
  `https://api.cepik.gov.pl/pojazdy/{idCar}`  
  Fetch details about a specific vehicle using its ID.

- **Search Vehicles**:  
  `https://api.cepik.gov.pl/pojazdy`  
  Retrieve a list of vehicles based on provided filters like registration date, region (województwo), fuel type, etc.

## Application Structure

### MainActivity
 - The main activity hosts the initial form for inputting vehicle search filters and navigating to other screens.

### FormFragment
 - This fragment displays the form to input search filters like vehicle brand, model, year, fuel type, etc., and interacts with the CEPiK API.

### IdCarFragment
 - This fragment allows the user to search for a vehicle by its unique ID.

### ResultFragment
 - This fragment displays the results of the vehicle search, either based on filters or a specific vehicle ID, and allows users to copy vehicle IDs to the clipboard.

### CustomListAdapter
 - A custom adapter for populating the list view with vehicle data retrieved from the CEPiK API.

## Contributing

We welcome contributions to this project! If you'd like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new pull request.

## Design

![cepik](https://github.com/user-attachments/assets/fd675a78-7866-4153-adb7-5331e8f9f9bb)


## License

This project is open-source and available under the [MIT License](LICENSE).

