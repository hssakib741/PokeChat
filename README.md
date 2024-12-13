PokeChat Application
PokeChat is a real-time chat application designed for seamless communication between users. Built with Java for Android, the app leverages Firebase as the backend for authentication, real-time database, and notifications. Its modern and intuitive interface ensures a smooth user experience, making it an excellent choice for learning and building chat applications.

Key Features
User Authentication:

Phone Number Login (Loginphone.java): Users can log in using their phone number.
OTP Verification (Loginotp.java): Ensures secure login with one-time password authentication.
Username Login (Loginusername.java): Alternative login method for flexibility.
Real-Time Chat:

ChatActivity.java: Facilitates one-on-one messaging with real-time updates.
ChatFragment.java: Displays ongoing conversations and provides a smooth chat experience.
ChatRecyclerAdapter.java: Efficiently renders chat messages in a scrollable format.
Firebase Integration:

FirebaseUtil.java: Handles database operations such as storing messages, user details, and fetching data in real time.
FCM_Notification.java: Sends push notifications for new messages using Firebase Cloud Messaging.
User Management:

SearchUserActivity.java: Allows users to search for other registered users.
ProfileFragment.java: Displays and manages user profile details.
UI/UX Design:

XML layouts (activity_*.xml, fragment_*.xml) for login, chat, and search screens are optimized for a modern and responsive experience.
RecentChatRecyclerAdapter.java: Lists recent chats for easy navigation.
Splash Screen (Splash.java):

Welcomes users with an engaging splash screen during app startup.
Technology Stack
Frontend: Java with Android SDK for a native mobile experience.
Backend: Firebase for real-time database, authentication, and push notifications.
Adapters: Efficient RecyclerAdapters for rendering chat messages, user search results, and recent chats.
Ideal Use Cases
Personal or small-scale chat applications.
Learning project to understand Firebase integration and RecyclerView in Android.
A base template for building advanced chat apps with group chats or multimedia sharing.
