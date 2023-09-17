# WhatsopWebAndroid
WhatsoWebAndroid is an Android messaging application that takes inspiration from the popular WhatsApp design. It was developed as part of the Advanced Programming 2 course at Bar-Ilan University. This projects contains a NodeJS based server, a web client (based on React) and an Android based application while all types of communication happens during real-time.

## Server
The communication in the application is performed against an API server.<br>
We recommend using our server. However, you can also use your own server as long as it implements all the required API contracts.<br>
By default, the app uses http://10.0.2.2:5000/api/ as the server api address, assuming that the server is running on the same computer as the emulator. However, you can easily change this address in the app's settings.

## Data
The application stores data both on the server and in a local database.<br>
The local database is implemented using SQLite and [Room](https://developer.android.com/training/data-storage/room/), a persistence library provided by Android. When the user enters the app, the local data is loaded first, and then the app requests updated data from the server (using [RetroFit](https://square.github.io/retrofit/)), ensuring synchronization between the local and server data.

## How to Run
Clone this repository:

```bash
git clone https://github.com/noam1222/WhatsopWebAndorid
```

### Server and Web Client
To run the server and the web client, follow these steps:<br><br>
Go to the server directory:

```bash
cd WhatsopWebAndorid/server
```

Install the dependencies:

```bash
npm install
```

Start the development server:

```bash
npm start
```

Open [http://localhost:5000](http://localhost:5000) to view it in the browser.

### App
To run the application, follow these steps:

- Open the cloned project in [Android Studio](https://developer.android.com/studio).
- Configure the necessary dependencies and SDK versions if prompted.
- Build and run the app on an Android emulator or a connected Android device.

## Screens
### Web
![login screen](https://user-images.githubusercontent.com/101872202/236865743-2f2a9c58-2b29-4bd6-a3d4-4be41499c6c8.png)
![signup scrren](https://user-images.githubusercontent.com/101872202/236865619-9ef181a0-7af3-4d38-bbe4-6545af3fc20d.png)
![chat screen](https://imgur.com/dxYz0bu.png)

### App
The application consists of the following screens:

<img src="https://imgur.com/vPIHZ80.png" width="200"  align="left" />
<img src="https://imgur.com/oYUnc4x.png" width="200"  align="left" />
<img src="https://imgur.com/fLmHACN.png" width="200" />
<img src="https://imgur.com/hkbWqMC.png" width="200"  align="left"/>
<img src="https://imgur.com/gYB7zYk.png" width="200"  align="left" />
<img src="https://imgur.com/1q1d6eo.png" width="200" />

### 1. Login
The login screen allows users to authenticate and access their accounts. Users need to enter their username and password to log in.

### 2. Signup
The signup screen enables new users to create an account. To successfully sign up, users must provide a username and display name, both with a minimum of 4 characters. The password must be at least 8 characters long, including 1 or 2 numbers.

### 3. Settings
The settings screen provides options for customizing the application. Users can change the API server address and change the app theme according to their preferences.

### 4. Chats
The chats screen displays all the conversations that the user is currently engaged in. Users can select a chat to view the messages.

### 5. Messages
The messages screen shows all the messages within a specific chat. Users can read and send messages in this screen.

### 6. Add Contact
The add contact screen allows users to add new contacts to their messaging list. Users can search for other users by their username or display name and send them a contact request.<br>
<b>Please note: User addition is limited to existing users only.</b>

<b>After signing in, the app opens directly to the Chats screen, and this screen remains the default open screen until the user logs out, even if the app is closed and reopened.</b>

