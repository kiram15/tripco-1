# Sprint 4 - *T02* - *NEKA*

## Goal

### Worldwide trips!

## Definition of Done

* Ready for the demo.
* Sprint Review and Restrospectives completed.
* Product Increment release `v4.0` created on GitHub with appropriate version number and name, a description based on the template, and an executable JAR file for the demo.
* Version in pom.xml should be `<version>4.0.0</version>`.
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

## Plan 

Epics in the Sprint Backlog: *worldwide trips, website transition, trip destination selection, optimization and selection*.  

Total planned tasks in the Sprint Backlog: *45* 

Total story points in the Sprint Backlog: *122*

## Daily Scrums

Date | Tasks done this time | Tasks done next time | Impediments | Confidence
:--- | :--- | :--- | :--- | :---
*date* | *issue numbers only* | *issue numbers only* | *None* | *High*
10/25 | #201 #218 #222 #219 #252 | #223 #216 #240 #221 | Midterm/Halloweekend | Moderate (large project/workload) 
10/26 | #74 #221 #223 #241 #242 #258 #263 #266 | #243 #240 #262 | Midterm/Halloweekend | Moderately positive
10/29 | #226 #247 #272 #262 #248 | #274 #216 #240 #246 #249 | Fixing 2-opt distances | Good
11/1 | #274 #283 #286 #287 #289 #290 | #243 #255 #242 #216 #250| CS370 Project (Emerson), Math Midterm (Emerson+Amber), Interview Prep (Kira) | Low 
11/2 | #242 #246 #298 | #243 #255 #240 #297 | Same as last daily scrum | Low
11/3 | #75 #240 #243 #249 #297 | #255 #276 #216 #244 #250 | Same as last daily scrum | Medium
11/5 | #255 #285 #216 #268 #307 #245 #313 | #276 #244 #259 #250 #311 | Tests, 320 program (Amber, Kira, Nicole), stress | Medium high
11/8 | #244 #245 #250 #251 #256 #259 #320 #324 #325 #328 #330 #331 #336| #338 #343 #217| 320 program and exam (Amber, Kira, Nicole) | Medium
 

## Review

#### Completed user stories (epics) in Sprint Backlog 
* *75*:   *Worldwide*
* *232*:  *Trip Destination Selection*
* *233*:  *Optimization and Selection*
* *234*:  *Select a starting Location*
* *239*:  *Website Transition*

Completed *55* issues associated with these user stories.

#### Incomplete user stories / epics in Sprint Backlog 
*

#### What went well
* *Website transition, worldwide application*
*

#### Problems encountered and resolutions
* *Issue #250 (save itinerary) was shown to have bugs in the demo, and therefore needs fixing before completed*
* *All optimizations were reveled to have a small bug at the end of the itinierary which adds to overall distance*

## Retrospective

Topic | Teamwork | Process | Tools
:--- | :--- | :--- | :---
What we will change this time | We definetly started work earlier | We improved and planned a ton of the issues upfront | Code coverage is up to aroun 85% with our JUnit tests
What we did well |  We collaborated well, asked for help, and met 3 times a week |  We started really early and consistenly worked | We are more comfortable with the technology and new languages, and were able to add the url stuff
What we need to work on | Communicating and being familiar with all sides, so we can all help | Doing the same amount of work every week | Code Climate and saving state on javascript
What we will change next time | Get more familiar with all parts of the system to allow for team collaboration | Try to get big issues finished before the last minute | Continue on Code Climate and JSX, react, CSS, and SQL
