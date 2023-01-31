# WorkmanagerRepo

Its an example of workmanager.

We use both type of work request:
- OneTimeWorkRequest
- PeriodicWorkRequest

Step to implement WorkManager:
- Create a new project and add WorkManager dependency in app/buid.gradle file
- Create a class by extending abstract Worker class
- Create WorkRequest
- Enqueue the request
- Fetch the task status
