# goSellSDK-Android
Android SDK to use [goSell API][1].

Install
--------
Add it in your **root** `build.gradle` at the end of repositories:
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```groovy
	dependencies {
	        compile 'com.github.Tap-Payments:goSellSDK-Android:1.0'
	}
```

Basic usage
-------------
**Step 1.** Grab credentials from your dashboard:<br>
1. **`authToken`** - to authorize your requests.
2. **`encryptionKey`** - to protect sensitive card details.

**Step 2.** Obtain an instance of **`GoSellAPI`** class via **`getInstance(authToken)`** method.

**Step 3.** Now you can call API methods with this instance, providing necessary parameters and **`APIRequestCallback<>`** class, instantiated with corresponding response type.

Also see `company.tap.gosellapi.TestActivity` class for usage examples.

Documentation
-------------
Documentation is available at [github-pages][2].<br>
Also documented sources are attached to the library.

[2]:https://tap-payments.github.io/goSellSDK-Android/

[1]:https://www.tap.company/developers/
