
# Glam AR Android SDK

## Introduction
You can utilise numerous SKUs, such as lipstick, blush, eyeliner, and eyeshadow, using the GlamAR Android SDK. The SKUs that you have already set up in the Admin Panel are the only ones that you can apply. You can tweak the makeup's opacity. Further, in next release you will be able to take pictures of the preview, and even record videos using the GlamAR SDK. You can use the GlamAR SDK in applications like showing your shop's catalogue because it can apply cosmetics in real-time.

## Getting started

Before you start running the demo app, you need to download the [Android SDK](https://cdn.pixelbin.io/v2/glamar-fynd-835885/Fn3s_P/original/glamar_android_sdk.zip) from here.

Once you've downloaded the SDK follow the below steps to start integrating the SDK into your app.

> :bulb: **Note** 
>
> Before running or building the app or project you need to enter the API key, you can access one from the admin panel dashboard. The GlamAR Activation key has to be placed inside strings.xml file 

```xml
    <string name="GLAM_API_KEY">YOUR_GLAMAR_ACTIVATION_KEY</string>
```

    
### 1. Create the `libs` folder inside you app folder and copy `glamarcommunication-debug.aar` file into it before starting integration.

![Screenshot1](https://cdn.pixelbin.io/v2/glamar-fynd-835885/YG-lVE/original/documents/glamar_android_1.png)

### 2. Now, create a folder in your root directory called `glamarsdk` and copy the rest of the SDK module into it.

![Screenshot1](https://cdn.pixelbin.io/v2/glamar-fynd-835885/YG-lVE/original/documents/glamar_android_2.png)

Once you've created the folder go to your `settings.gradle` folder and do the following changes:

``` gradle
    include ':glamarsdk'
```

Now, go to your build.gradle (app) and make following changes in dependencies section:

``` gradle
implementation fileTree(dir: 'libs', include: ['*.aar'])
api project(':glamarsdk')
```

After adding the above dependecies sync the project.

Once the gradle is synced successfully, go to File -> Project Structure and configure the app dependency as follows

![Screenshot1](https://cdn.pixelbin.io/v2/glamar-fynd-835885/YG-lVE/original/documents/glamar_android_3.png)

![Screenshot1](https://cdn.pixelbin.io/v2/glamar-fynd-835885/YG-lVE/original/documents/glamar_android_4.png)

Once completed, sync the project.

### 3. You can now goto UnityPlayerActivity and see the implementation of SDK. Also, you can now try running the app (Remember you will need physical device to run the app).

![Screenshot1](https://cdn.pixelbin.io/v2/glamar-fynd-835885/YG-lVE/original/documents/glamar_android_5.png)



