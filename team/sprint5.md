# Sprint 5 - *T02* - *NEKA*

## Goal

### Reliable first release with clean code!

## Definition of Done

* Ready for demo / customer release.
* Sprint Review and Restrospectives completed.
* Product Increment release `v5.0` created on GitHub with appropriate version number and name, a description based on the template, and an executable JAR file for the demo.
* Version in pom.xml should be `<version>5.0.0</version>`.
* Unit tests for all new features and public methods at a minimum.
* Coverage at least 50% overall and for each class.
* Clean continuous integration build/test on master branch.

## Policies

* Tests and Javadoc are written before/with code.  
* All pull requests include tests for the added or modified code.
* Master is never broken.  If broken, it is fixed immediately.
* Always check for new changes in master to resolve merge conflicts locally before committing them.
* All changes are built and tested before they are committed.
* Continuous integration always builds and tests successfully.
* All commits with more than 1 line of change include a task/issue number.
* All Java dependencies in pom.xml.


## Metrics

Statistic | Planned | Completed
--- | ---: | ---:
Tasks |  11  | *value* 
Story Points |  5  | *value* 


Statistic | Start | End
--- | ---: | ---:
Overall Test Coverage | 88% | *value* 
Smells | 41 | *value* 
Duplication | 40 | *value* 
Technical Debt Ratio | 33.5 | *value* 

## Plan

Epics planned for this release.

* #363 Clean Code
* #362 Test Coverage
* #235 Online map with zoom and pan
* #72 Fast Response
* #236 Mobile User Experience

## Daily Scrums

Date | Tasks done now | Tasks done next | Impediments | Coverage | Smells | Duplication | Technical Debt Ratio | Confidence
:--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :---
11/13 | #355 #356 #358 | #353 #250 #349 | CS320 Exam Wednesday, GRE | *70* | *40* | *40* | *33.5* | Feeling great
11/29 | #378 #353 #368 #374 #250 #349 #254 #217 | #342 #370 #375 #191 | Nicole is a busy bee | *72* | *33* | *6* | *18.3* | Feeling good
11/30 | #375 #342 #382 | #191 #372 #370 #369 | Nicole's sorority stuff, studying for finals | *72* | *20* | *2* | *9.1* | Feeling lit
12/4 | #235 #372 #369 #191 #72 #276 | #390 #370 #371 #392 | final projects/exams | *70* | *20* | *0* | *6.8* | Good
12/6 | #273 #390 #96 #399 #401 | #397 #370 #402 #371 | final projects/exams | *68* | *11* | *0* | *7.3* | Good
12/7 | #397 #402 #415 #411 #410 #362 #363 #371 | None! | finals | *68* | *3* | *0* | *0.7* | Good 

## Review

#### Completed user stories (epics) in Sprint Backlog 
* *user story*:  *comments*
* Test Coverage; We upgraded our test coverage
* Clean Code; We improved our tech debt ratio
* Fast Response; We added threads to our optimization functionality
* Online map with pan/zoom; We added a map that had the zoom and pan functionality

#### Incomplete user stories / epics in Sprint Backlog 
* *user story*: *Explanation...*
* Mobile Experience; We did not have time to get to this Epic
* Continuous Delivery; The Product Owner never gave us specificationss

#### What went well
* *something*
* We planned well
* Never felt stressed
* Spread work out well
* Didn't have carried over tech debt
* Fixded small details
* Utilized other groups

#### Problems encountered and resolutions
* *something*
* 3-opt being slow - added threads, took out extraneous methods
* Busy weeks - the team picked up slack

## Retrospective

Topic | Teamwork | Process | Tools
:--- | :--- | :--- | :---
What we will change this time | Get more familiar with all code | finish large issues quickly | improve code climate, SQL
What we did well | got more familiar with all of our code | finished larger issues quicker | KILLED code climate
What we need to work on | Asking if someone needs help | More logicically organize backlog to minimize merge conflicts | React.js, Code Climate
What we will change next time | try to ask for help earlier | organize backlog better | more research on react
