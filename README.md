# My Places

This is an application which helps me record locations which I want to save and keep track of.

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.varunbarad.myplaces)

You can also find the signed APK [here](https://github.com/VarunBarad/my-places-android/raw/master/release/My%20Places.apk).

## Running the application

To build the debug-variant of application, use the below command

```shell
# On Linux/Unix system
./gradlew assembleDebug

# On Windows system
gradlew.bat assembleDebug
```

You can then find the generated apk file at `<project-dir>/app/build/outputs/apk/debug/app-debug.apk`

## Running the tests

To run the unit-tests, use the below command

```shell
# On Linux/Unix system
./gradlew test

# On Windows system
gradlew.bat test
```

After it finishes, you can find the results of the test in this directory: `<project-dir>/app/build/reports/test/testReleaseUnitTest`

## Screenshots

![Home Screen - No Places](https://raw.githubusercontent.com/VarunBarad/my-places-android/master/screenshots/01%20No%20Saved%20Places.png)

![Home Screen - With Places](https://raw.githubusercontent.com/VarunBarad/my-places-android/master/screenshots/04%20Saved%20Places%20List.png)

![Add Place](https://raw.githubusercontent.com/VarunBarad/my-places-android/master/screenshots/02%20Add%20Place.png)

![Place Details](https://raw.githubusercontent.com/VarunBarad/my-places-android/master/screenshots/03%20Place%20Details.png)
