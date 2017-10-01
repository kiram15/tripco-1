# Sprint 2 - *T02* - *NEKA*

## Goal

### A shorter trip with a better itinerary.

## Definition of Done

* Ready for the demo.
* Sprint Review and Restrospectives completed.
* Product Increment release created on GitHub with appropriate version number and name, a description based on the template, and a JAR file containing the executables to be used for the demo. 
* Unit tests for all new features and public methods at a minimum.
* Clean continuous integration build/test on master branch.
* No outstanding branches, commits, pull requests.

## Policies

* Master is never broken.  If broken, it is fixed immediately.
* Continuous integration always builds and tests successfully.
* Tests are written before/with code.  
* All changes are built and tested before they are committed.
* Always check for new changes in master to resolve merge conflicts locally before committing them.
* All commits with more than 1 line of change include a task/issue number.
* All pull requests include tests for the affected code.

## Plan 

User stories (epics) in the Sprint Backlog:  6

Total planned tasks / issues in the Sprint Backlog:  18

## Daily Scrums

Date | Tasks done this time | Tasks done next time | Impediments | Confidence
:--- | :--- | :--- | :--- | :---
 9/18 | worked on some tests | Write tests for sprint one, look into svg map | Nicole has recruitment until wednesday, exam studying  | Not stressed, feeling decent
 9/21 | finished changing ID to name, wrote JUnit tests, fixed master| starting on checkbox filters, psuedo code -> code on Shortest trip | midterm coming up | Feeling good so far
 9/25 |Kira/Emerson did a lot of research on the popup/checkboxes but didn't make an progress, added SVG rectangle | redo loops in nearest neighbors, write JUnit tests | Kira is leaving for New Orleans Thursday | feeling a little stressed, but focused
 9/27 |Kira finished the dropdown menu, Nicole got shortest distance done, Emerson refactored, Amber is writing a java class to write SVG for map | Continue working on updating itinerary with user preferences, and get map on webpage, add JUnit tests for backend |Lateness of the direction and unfamiliarity with the technology | Low

## Review

#### Completed user stories (epics) in Sprint Backlog 
* Basic map with itinerary
* Shortest Trip
* Optional Map Background

Completed *24* tasks / issues associated with these user stories.

#### Incomplete user stories / epics in Sprint Backlog 
* Useful information in itinerary
* Optional Fast Response
* Optional Refactor

#### What went well
* Calculating the shortest trip
* Creating/Displaying the map of the trip on the webpage
* Dynmaically reading in the data from the CSV file

#### Problems encountered and resolutions
* Too many loops in shortest path - used a sorted has table to minimize time
* Scaling lat/long to x/y coordinates on the Colorado Map - regularly flushed the print writer
* Technology - documentation posted by professor

## Retrospective

Topic | Teamwork | Process | Tools
:--- | :--- | :--- | :---
What we will changed this time |  |  | 
What we did well | Supporting each other and communicating | Getting a second opinion to fix bugs/improve code design | GitHub procedure, quickly picked up on technologies we've never used before
What we need to work on | Active listening to teammates during meetings, don't just focus on our own work | Get started earlier, figure out scope of work within the first couple of days | Need to do indept react/Javascript tutorials
What we will change next time | Make sure we are checking in with our teammates throughout the sprint to see if they need help | Add an extra weekly meeting on Sundays | Focus more on Test-Driven Development using JUnit test
