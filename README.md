# Android SDK
​
## Android SDK Introduction
​
With GlamAR Android SDK, you can apply different SKUs such as  lipstick, blush, eyeliner and eyeshadow.

Note :- 

- You can only apply SKUs that you’ve already configured in the Admin Panel.

- With the GlamAR SDK, you can take snapshots of the preview, change the makeup opacity and even capture a video recording.

- Because the GlamAR SDK can apply makeup in real-time, you can use it in applications such as displaying your shop’s cataolgue.

### Note:

* Before running or building the app or project you need to enter the API key, you can access one from the admin panel dashboard. The API key has to be placed inside strings.xml file 

```xml
    <string name="GLAM_API_KEY">GlamAR Activation Key</string>
```

## Integrating GlamAR SDK into an Android Project

### Download the GlamAR Android SDK

Before integrating the GlamAR SDK into your app, you must first download the [GlamAR Android SDK](https://cdn.pixelbin.io/v2/glamar-fynd-835885/yzNPP9/original/glamar_android_sdk.zip) module from the attached link. Once you have downloaded the SDK, follow the steps provided in the documentation to begin the integration process.

### **Requirements**

Before proceeding with the integration, ensure your development environment meets the following prerequisites:

* Android SDK version 24 (Android 7.0 Nougat) or higher.

### Integrating into a New Project or Existing Project

1. Create a `libs` folder inside your app folder and copy all the files from the SDK into it.
	![Screenshot1](https://cdn.pixelbin.io/v2/glamar-fynd-835885/flpU0e/original/documents/glamar_android_6.png)
	
2. In your `build.gradle` (Module:app) file, add the following dependencies:

```gradle
implementation fileTree(dir: 'libs', include: ['*.jar'])
implementation fileTree(dir: 'libs', include: ['*.aar'])
constraints {
    implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0'
    implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0'
}
```

Sync your project after adding the dependencies. Once the Gradle sync is successful, you’re all set to use the GlamAR SDK. 

### Modify Android Manifest

Here’s a more precise and concise version of the content as per the standards of SDK documentation:

1. Add the following permissions to your `AndroidManifest.xml` file to allow the SDK to access network resources and the camera:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

2. Add the reference of UnityPlayerActivity to your `AndroidMainfest.xml`, this allows the project to avoid the merger error of manifests while building the apk.

```xml
<activity android:name="com.unity3d.player.UnityPlayerActivity" android:exported="true" />
```

1. In your `strings.xml` file, add a new string resource called `game_view_content_description` to help UnityPlayer access the view:

```xml
<string name="game_view_content_description"></string>
```

This will ensure that the SDK has the necessary permissions and resources to function properly. 

## Setting Up Activity

1. Set up your activity by importing the `unity3d player` package and extending your `Activity` class with the necessary interfaces:
```java
    import com.unity3d.player.IUnityPlayerLifecycleEvents;
    import com.unity3d.player.UnityPlayer;
        
    public class YourActivity extends AppCompatActivity implements
    IUnityPlayerLifecycleEvents, IGlamARCommunication {...}
```
2. Create members for `UnityPlayer` and `GlamARCommunication` in your activity:
```java
    protected UnityPlayer mUnityPlayer;
    protected GlamARCommunication glamARCommunication;
```

## GlamAR SDK Calls

1. Initialize `UnityPlayer` when the activity is created, for example in the `onCreate()` method:
```java
    mUnityPlayer = new UnityPlayer(this, this, true);
    this.frameLayoutForUnity = (FrameLayout) findViewById(R.id.unity_frame);
    this.frameLayoutForUnity.addView(mUnityPlayer.getView(), FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    
    mUnityPlayer.requestFocus();
    mUnityPlayer.windowFocusChanged(true);
```

2. Initialize the `GlamAR Communication` calls using the `InitGlamAR()` method:
```java
    glamARCommunication.InitGlamAR(YOUR_API_KEY, "<YourPackageName>.<YourActivityClassName>", IGlamARCommunication);
    glamARCommunication.setCameraActivity(Activity);
```

### GlamAR SDK Methods

|Method	|Parameter	|Details	|
|---	|---	|---	|
|glamARCommunication.fetchParticularSKUId(String);	|1. SKU ID as a string	|Verifies the SKU ID sent by the user.	|
|glamARCommunication.launchModuleByName(String category, String currentColorVal)	|1. Category name as a string 2. Color hex value as a string 	|Launches the module according to the category sent and color value applied.	|
|glamARCommunication.setColorIntensity(Int currentIntensityVal)	|1. Opaqueness percentage as an integer. Minimum value: 1 (completely transparent). Maximum value: 100 (completely opaque)	|Set the opaqueness of the color value by passing the percentage of opaqueness desired.	|
|glamARCommunication.setStyleId(String styleId)	|1. Style ID as a string	|Set the styling for the lipstick module (0 for matte effect, 1 for glitter effect, 2 for glossy effect).	|
|glamARCommunication.applyMakeupConfig(String makeupJsonString)	|1. Makeup JSON object as a string 	|Applies the makeup according to the JSON object sent.	|
|glamARCommunication.handlePauseOrPlayPlayer(boolean isPlaying)	| Handle the Pause and Play of GlamAR Player. This method could be useful when you're using the GlamAR Player inside your fragment.	|

## API References

1. Get all SKU items: Use the following endpoint to fetch all SKUs from the admin panel that you have created:

```
GET https://api.glamar.io/frieza/api/v1/core/sdk/sku-list
```

**Parameters**:

* `Authorization` (string): Required. Your API key.
* `Content-type` (string): Required. `application/json`.
* `Source` (string): Required. `android`.

1. Get SKU item: Use the following endpoint to fetch a specific SKU item by its ID:

```
GET https://api.glamar.io/frieza/api/v1/core/sdk/sku/${id}
```

**Parameters**:

* `Authorization` (string): Required. Your API key.
* `Content-type` (string): Required. `application/json`.
* `Source` (string): Required. `android`.

Make sure to include the required parameters in your API requests to successfully retrieve the desired data.


## Events

### IGlamARCommunication (Interface):

|Callback	|Description	|
|---	|---	|
|onSuccessResponseModuleLaunch(String var1)	|Called when the verification of the passed `skuId` value is successful. Returns the result in `Object` format (a JSON string response).	|
|---	|---	|
|onFaceTrackingLost(boolean var1)	|Called when the tracking of the face is lost. Returns the result in Boolean format 	|
|onErrorMessage(MessageType var1, String var2)	|Called when an error occurs when calling any method of the `GlamARCommunication` class. Gives the MessageType as enum and returns a string message.	|
|onSuccessMessage(MessageType var1, String var2)	|Called when a method of the `GlamARCommunication` class is successfully executed. Gives the MessageType as enum and returns a string message.	|
|onDownloadCompletedSuccessfully() |Called when the component downloading part is completed.|
|onDownloadProgress(int progress) |Called whenever the progress of download is updated.|
|onDownloadStatus(DownloadStatus status) |Called when the download is started,interupted or completed. The values returned would be STARTED, FAILED, or COMPLETED|

**Expected Error Messages:**

|Message Type	|Description	|
|---	|---	|
|CONFIG_FAILED	|When the makeup config is passed with invalid JSON 
|GLAM_FAILED	|When wrong category is sent while launching the module.	|
|CAMERA_FAILED	|When camera permission is not set	|
|COLOR_INTENSITY_FAILED	|When a wrong value is passed while setting the color intensity.	|
|*SKU_FAILED*	|When the SKU ID is failed to verified.	|
|COLOR_FAILED	|When the wrong hex color code is sent.	|
|STYLE_FAILED	|When the style ID is set wrong.	|
|IMAGE_PICKER_ENDED	|When the Image Picker couldn't find the permission or permission isn't granted or some interuption has occured.	|
|VALIDATION_FAILED	|When API key is not set or when activity class name is not provided or when camera class reference is not instantiated.	|

**Expected Success Messages:**

|Message Type	|Description	|
|---	|---	|
|*SKU_APPLIED*	|When SKU ID is verified successfully.	|
|*CONFIG_APPLIED*	|When makeup config. JSON is verified successfully.	|
|*GLAM_LOADED*	|When the module is launched successfully.	|
|*GLAM_LOADING*	|When specified module is loading.	|
|*CAMERA_OPENED*	|When the camera permission is accessed successfully.	|
|*COLOR_INTENSITY_APPLIED*	|When the color intensity value is provided and applied successfully.	|
|*COLOR_APPLIED*	|When the color value is applied successfully.	|
|*STYLE_APPLIED*	|When the style ID is set properly.	|
|*IMAGE_PICKER_OPENED*	|When the Image Picker from SDK is opened.	|

These callbacks provide useful information for error handling and tracking the success of calls to the `GlamARCommunication` class.

---

## Sample Android SDK app

See the [Glamar SDK quickstart sample](https://github.com/gofynd/glamar-android-demo) on Gitlab for an example of this SDK in use.

---
