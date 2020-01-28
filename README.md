# My Places

This is an application which helps me record locations which I want to save and keep track of.

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
