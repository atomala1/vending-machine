# Vending Machine

A simple vending machine application.

## Getting Started

Build the application using the gradle wrapper

```
./gradlew clean build
```

## Running Tests

Run the tests by running the tests with the gradle wrapper

```
./gradlew clean test
```

## Jacoco Test Report

```
./gradlew test jacocoTestReport
```

## Notes

If the user tries to do anything out of the ordinary, the application will throw a named RuntimeException.