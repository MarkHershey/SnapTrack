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
DataUtils.fetchActivities(new Listener<Map<String, UserActivityInfo>>(){
    void update(Map<String, UserActivityInfo> activities){
        if (activities != null){
            // do Something
        }
    }
});
```

## Category

- Attributes
    - `string` **name**: Primary key, name of category.
    - `int` **color**: requires uniqueness within the user scope.
        - system will assign random color as default value.

```java
DataUtils.fetchCategories(new Listener<Map<String, CategoryInfo>>(){
    void update(Map<String, CategoryInfo> categories){
        if (categories != null){
            // do Something
        }
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
DataUtils.fetchEvents(new Listener<List<EventInfo>>(){
    void update(List<EventInfo> events){
        if (events != null){
            // do Something
        }
    }
});
```
