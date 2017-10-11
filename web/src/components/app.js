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
            setInfo : [],
            selColumns : []
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
                    startEndInfo={this.startEndInfo.bind(this)}

                    onClick={this.onClick.bind(this)}
                    pairs={ps}
                    totalDist={this.state.total}
                    columns = {this.state.setInfo}
                />
            </div>
        )
    }

    onClick(val) {
        if (this.state.selColumns.indexOf(val) == -1) {
            this.state.selColumns.push(val);
            console.log("Selected: ", this.state.selColumns);
            this.browseFile(this.state.sysFile);
        }
        else {
            var inVal = this.state.selColumns.indexOf(val);
            this.state.selColumns.splice(inVal, 1);
            console.log("DeSelected: ", val);
            console.log("Selections now: ", this.state.selColumns);
            this.browseFile(this.state.sysFile);
        }

    }


    startEndInfo(file) {
        var finalStr = "";
        console.log("In startEndInfo");
        file = file.replace(/["{}]/g, "")
        var columnNames = this.state.selColumns;
        //var columnNames =
        var info = file.split(',');
        for (var i = 0; i < info.length; i++) {
            info[i] = info[i].trim();
        }
        //console.log(selectedColumns);

//        for (var i = 0; i < info.length; i++) {
//            for (var j = 0; j < (selectedColumns.length); j++) {
//                var colName = info[i].substring(0, info[i].indexOf(":"));
//                if (colName.equals(selectedColumns[j])) {
//                    finalStr += info[i] + "\n";
//                }
//            }
//        }
        for (var i = 0; i < info.length; i++) {
            for (var j = 0; j < (columnNames.length); j++) {
                var colName = info[i].substring(0, info[i].indexOf(":"));
                if (colName ==columnNames[j]) {
                    finalStr += info[i] + "\n";
                }
            }
        }

        return finalStr;
    }

    async selectColumns(file) {
        console.log("Got File:", file);
        console.log(file[0].startInfo);
        var options = Object.keys(file[0].startInfo);
        console.log("Options: ", options);
        this.setState({
            setInfo: options
        });
    }

    async browseFile(file) {
        console.log("Got file:", file);
        //For loop that goes through all pairs,
        let pairs = [];
        let runTotal = 0;
        this.selectColumns(file);
        for (let i = 0; i < Object.values (file).length; i++) {
            let start = file[i].start; //get start from file i
            let end = file[i].end; //get end from file i
            let dist = file[i].distance;
            runTotal = runTotal + dist;

            var updatedStart = JSON.stringify(file[i].startInfo);
            updatedStart = String(this.startEndInfo(updatedStart));
            var updatedEnd = JSON.stringify(file[i].endInfo);
            updatedEnd = String(this.startEndInfo(updatedEnd));

            let p = { //create object with start, end, and dist variable
                start: start,
                end: end,
                dist: dist,
                total : runTotal,
                startInfo : updatedStart,
                endInfo : updatedEnd
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
