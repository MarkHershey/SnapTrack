# Work & Life
50.001 Introduction to Information Systems &amp; Programming - 1D Project

## Design MockUp

- [Figma Edit Link](https://www.figma.com/file/q8oGLAocaAazg4XulfOtYF/50001-SnapTrack?node-id=0%3A1)

## Scope Of Work

- [ ] User sign up and sign in
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
    - **UID**: Primary key, hidden to user
    - **email**: for login
    - **password**: for login
    - **display_name**: User name for display
    

## UserActivity

- UserActivity is the user-defined activity (e.g. "Work out", "Study", "CompStruct", "Entertainment", etc) for time tracking, it is tied to each User's ID in the database. 
- Attributes
    - **AID**: Primary key, hidden to user
    - **activity_name**: requires uniqueness within the user scope 
    - **short_display_name**: requires uniqueness within the user scope 
    - **color**: requires uniqueness within the user scope.
        - system will assign random color as default value.
    - **category**: CID of a Category. 
    - **nfc_tag_id**: UUID to identify NFC tags

## Category

- Attributes
    - **CID**: Primary key, hidden to user
    - **name**: name of category.
    - **color**: requires uniqueness within the user scope.
        - system will assign random color as default value.

## Event

- Event is the object being created when a User start doing a `UserActivity`, either using NFC tapping or manual creation.
- Attributes
    - **EID**: Primary key, hidden to user
    - **User**: UID
    - **UserActivity**: UserActivity ID
    - **time_start**: DateTime Object 
    - **time_end**: DateTime Object 