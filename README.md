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
- [ ] other data analytics

## User

- Attributes
    - email
    - password
    - Display Name
    - UID


## UserActivity

- UserActivity is the user-defined activity (e.g. "Work out", "Study", "CompStruct", "Entertainment", etc) for time tracking, it is tied to each User's ID in the database. 
- Attributes
    - **AID**: Primary key, hidden to user
    - **activity_name**: requires uniqueness within the user scope 
    - **short_display_name**: requires uniqueness within the user scope 
    - **color**: requires uniqueness within the user scope.
        - system will assign random color as default value.
    - **labels**: a list of tag object. 
        - should contain at least one tag. 
        - System will pre-define two tags: work & life. 
    - **nfc_tag_id**: UUID to identify NFC tags



## Label

- Label is a string object used to label UserActivity, it provides the user the flexibility to view data analytics grouped by custom tags.
- Attributes
    - **LID**: Primary key, hidden to user
    - **tag_name**: a unique String
    - **color**: requires uniqueness within the user scope for all tags
        - system will assign random color as default value.


## Event

- Event is the object being created when a User start doing a `UserActivity`, either using NFC tapping or manual creation.
- Attributes
    - **EID**: Primary key, hidden to user
    - **User**: UID
    - **UserActivity**: UserActivity ID
    - **time_start**: DateTime Object 
    - **time_end**: DateTime Object 