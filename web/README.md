## Getting Started

To use this project first make sure you have `npm` or `yarn` installed. These are dependency managers that will download everything you need for the project.
You will also need `mvn` which is another dependency manager. You can download this as a standalone program and setup following the instructions found [here](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html). Otherwise, you have the option of installing [IntelliJ](https://www.jetbrains.com/idea/) and selecting the `mvn` plugin on configuration.
- Note: a `pom.xml` has already been added to the repo and does not need to be generated.

### Prerequisites

You must have `mvn` installed to build, package, and run your tests for your java server.
`npm` or `yarn` must be installed on the machine you wish to test your web app on.

You can easily install `npm` through [Homebrew](https://brew.sh/) or by downloading [Node.js](https://nodejs.org/en/).
- Note: `npm` is already installed on the CS 120 lab machines.

JsonRunner is built with:
- React
- Webpack
- React-hot-loader
- SASS

### Installing

If you have not already, clone the repo and enter it:
```bash
$ git clone git@github.com:csu2017fa314/staff.git
$ cd staff
```

Install all `mvn` dependencies:
```bash
$ mvn package
```
This will download everything referenced in the `pom.xml`
- Note: the `pom.xml` file has already been generated for your convenience. 

Move into the JsonRunner directory:
```bash
$ cd JsonRunner
```

Now run the following in the `JsonRunner` directory:
```bash
$ npm install
//or
$ yarn install
```
This will download all dependencies needed to run the app.
- Note: If you encounter a `EINTEGRITY` error, run `npm cache verify`. This will verify npm's cache. If this does not resolve the problem, run `rm -rf node_modules`. This will clear the node modules.

To run:
```
$ npm run dev
//or
$ yarn run dev
```

This will open the default web browser and navigate to `localhost:8080`.

From here, you can update the `.jsx` files and `localhost` will automagically reload your changes.

## Contact

For questions please contact [cs314@cs.colostate.edu](mailto:cs314@cs.colostate.edu).