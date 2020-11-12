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



## UserActivity

- UserActivity is user-customised activities for time tracking, it is tied to each User's database. 
- Attributes
    - **activity_name**: requires uniqueness within the user scope 
    - **short_display_name**: requires uniqueness within the user scope 
    - **color**: requires uniqueness within the user scope.
        - system will assign random color as default value.
    - **labels**: a list of tag object. 
        - should contain at least one tag. 
        - System will pre-define two tags: work & life. 
    - **nfc_tag_id**: UUID to identify NFC tags



## Label

- Label is a string object used to label UserActivity, it provides the user the flexibilty to view data analystics grouped by custom tags.
- Attributes
    - **tag_name**: a unique String
    - **color**: requires uniqueness within the user scope for all tags
        - system will assign random color as default value.


## Event (the tracking object)

- Attributes
    - `UserActivity`
    - time_start
    - time_end