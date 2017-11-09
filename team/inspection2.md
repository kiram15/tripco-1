# Team *T02* - Inspection *2*
 
Inspection | Details
----- | -----
Subject | 3-Opt
Meeting | 11/8, 8PM, Morgan Library
Checklist | http://users.csc.calpoly.edu/~jdalbey/301/Forms/CodeReviewChecklistJava.pdf

### Roles
Name | Role | Preparation Time
---- | ---- | ----
 Emerson | End User | 1 hour
 Amber | Maintainer | 1 hour
 Nicole | Tester | 1 hour
 Kira | Moderator | 1 hour

### Log
file:line | defect | h/m/l | github# | who
--- | --- |:---:|:---:| ---
 Hub.java : 632-839 | break swaps into methods to cut down on method size | m | #341
 Hub.java : 276-882 | break up all the optimization methods into another class | m | #342
 Hub.java : 857 | add comments to replaceSegment (helper method to 3opt) | l | #338
 Hub.java : 845 | add comments to optSwap (helper method) | l | #338 | kira | #338
 Hub.java : 650-700 | add comments to explain the 3opt algorithm throughout | m | #338 | kira
 Hub.java : 650-700 | remove comments pertaining to colors on lecture slides | l | #338 | kira
 Hub.java : 834 | assigning value to an unused variable (ogDistance) | l | #343
 Hub.java : 736 | create one delta variable and reassign instead of creating multiple | l | #343
 Hub.java : 700 | take out loops that calculate updated distance (only needed for testing | m | #343
