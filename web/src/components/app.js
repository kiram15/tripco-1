import React from 'react';
import Home from './Home/Home.jsx';
import Pair from './Home/Pair/Pair.jsx';


export default class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            allPairs: [],
            sysFile: [],
            total : 0
        }
    };

    render() {
        let pairs = this.state.allPairs;
        let ps = pairs.map((pp) => {
            return <Pair {...pp}/>;
        });
        return (
            <div className="app-container">
                <Home
                    browseFile={this.browseFile.bind(this)}
                    pairs={ps}
                    totalDist={this.state.total}
                />
            </div>
        )
    }

    async browseFile(file) {
        console.log("Got file:", file);
        //For loop that goes through all pairs,
        let pairs = [];
        let runTotal = 0;
        for (let i = 0; i < Object.values(file).length; i++) {
            let start = file[i].start; //get start from file i
            let end = file[i].end; //get end from file i
            let dist = file[i].distance;
            runTotal = runTotal + dist;
            //let test = file[i].startInfo.elevation; <-- this accesses the nested info
            let p = { //create object with start, end, and dist variable
                start: start,
                end: end,
                dist: dist,
                total : runTotal,
                //test : test
            };
            pairs.push(p); //add object to pairs array
            console.log("Pushing pair: ", p); //log to console
        }

        //Here we will update the state of app.
        // Anything component (i.e. pairs) referencing it will be re-rendered
        this.setState({
            allPairs: pairs,
            sysFile: file,
            total : runTotal
        });
    }
}
