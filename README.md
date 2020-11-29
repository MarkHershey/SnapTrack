# SnapTrack
50.001 Introduction to Information Systems &amp; Programming - 1D Project

## Design MockUp

- [Figma Edit Link](https://www.figma.com/file/q8oGLAocaAazg4XulfOtYF/50001-SnapTrack?node-id=0%3A1)

## Scope Of Work

- [x] User sign up and sign in
- [ ] User create / edit UserActivity
- [ ] User create / edit Label
- [ ] User link / unlink NFC tag to UserActivity
- [ ] User manually start / end Event
- [ ] NFC tag start / end Event
- [ ] daily / weekly / monthly Overview
    - [ ] Group by Activity / Label (Dropdown Selection)
- [ ] other data analytics

## User

- Attributes
    - `string` **authID**: generated and maintained by Firebase Authentication
    - **email**: maintained by Firebase Authentication
    - **password**: maintained by Firebase Authentication
    - `string` **userName**: User name for display
    - `string` **userID**: App generated 16-char UserID one-to-one maps to Firebase-generated AuthID. UserID will be written to NFC tags for user identification.


## UserActivity

- UserActivity is the user-defined activity (e.g. "Work out", "Study", "CompStruct", "Entertainment", etc) for time tracking, it is tied to each User's ID in the database.
- Attributes
    - `string` **AID**: App generated 16-char ID, hidden to user.
    - `string` **activity_name**: requires uniqueness within the user scope
    - `int` **color**: requires uniqueness within the user scope. Represented as int
        - system will assign random color as default value.
    - `List<String>` **categories**: Names of categories.

To fetch:

```java
string uid = DataUtils.getAuthID();
FirebaseDatabase db = FirebaseDatabase.getInstance();
DatabaseReference dbRef = db.getReference();
dbRef = dbRef.child("users").child(uid).child("activities");
dbRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        GenericTypeIndicator<Map<String, UserActivityInfo>> t = new GenericTypeIndicator<Map<String, UserActivityInfo>>() {};
        Map<String, UserActivityInfo> activities = snapshot.getValue(t);
        if(activities != null){
            // do something
            // key of the Map is the AID
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        // maybe return an error message idk
    }
});
```

## Category

- Attributes
    - `string` **name**: Primary key, name of category.
    - `int` **color**: requires uniqueness within the user scope.
        - system will assign random color as default value.

```java
string uid = DataUtils.getAuthID();
FirebaseDatabase db = FirebaseDatabase.getInstance();
DatabaseReference dbRef = db.getReference();
dbRef = dbRef.child("users").child(uid).child("categories");
dbRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        GenericTypeIndicator<Map<String, CategoryInfo>> t = new GenericTypeIndicator<Map<String, CategoryInfo>>() {};
        Map<String, CategoryInfo> categories = snapshot.getValue(t);
        if(categories != null){
            doSomething(categories);
            // key of the Map is the category name
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        // maybe return an error message idk
    }
});
```

## Event

- Event is the object being created when a User start doing a `UserActivity`, either using NFC tapping or manual creation.
- Attributes
    - **EID**: Primary key, hidden to user
    - **UserActivity**: UserActivity ID
    - **time_start**: DateTime Object
      - **time_end**: DateTime Object

```java
string uid = DataUtils.getAuthID();
FirebaseDatabase db = FirebaseDatabase.getInstance();
DatabaseReference dbRef = db.getReference();
dbRef = dbRef.child("users").child(uid).child("events");
// to get a range,
dbRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        GenericTypeIndicator<List<EventInfo>> t = new GenericTypeIndicator<List<EventInfo>>() {};
        List<EventInfo> events = snapshot.getValue(t);
        if(events != null){
            doSomething(events);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        // maybe return an error message idk
    }
});
```
