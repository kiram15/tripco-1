import React from 'react';
import Home from './Home/Home.jsx';
import Pair from './Home/Pair/Pair.jsx';

export default class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            allPairs: [],
            sysFile: [],
            total : 0,
            setInfo : []
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
                    selectColumns={this.selectColumns.bind(this)}
                    pairs={ps}
                    totalDist={this.state.total}
                    columns = {this.state.setInfo}
                />
            </div>
        )
    }
    async selectColumns(file) {
        console.log("Got File:", file);
        console.log(file[0].startInfo);
        var options = Object.keys(file[0].startInfo);
        console.log("Options: ", options);
        this.setState({
            setInfo: options
        })
    }

    async browseFile(file) {
        console.log("Got file:", file);
        //For loop that goes through all pairs,
        let pairs = [];
        let runTotal = 0;
        this.selectColumns(file);
        for (let i = 0; i < Object.values(file).length; i++) {
            let start = file[i].start; //get start from file i
            let end = file[i].end; //get end from file i
            let dist = file[i].distance;
            runTotal = runTotal + dist;
            let p = { //create object with start, end, and dist variable
                start: start,
                end: end,
                dist: dist,
                total : runTotal
            };
            pairs.push(p); //add object to pairs array
            console.log("Pushing pair: ", p); //log to console
        }
        //Here we will update the state of app.
        this.setState({
            allPairs: pairs,
            sysFile: file,
            total : runTotal,
        });
    }

}
