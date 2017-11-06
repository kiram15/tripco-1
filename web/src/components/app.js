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
            selColumns : [],
            queryResults : [],
            svgResults : null
        }
    };


render() {
    let pairs = this.state.allPairs;
    let ps = pairs.map((pp) => {
        return <Pair {...pp}/>;
    });
    let svg = this.state.svgResults;
    let query = this.state.queryResults;
    return (

        <div className="app-container">
            <Home
                browseFile={this.browseFile.bind(this)}
                selectColumns={this.selectColumns.bind(this)}
                startEndInfo={this.startEndInfo.bind(this)}
                query={query}
                svg={svg}
                onClick={this.onClick.bind(this)}
                pairs={ps}
                totalDist={this.state.total}
                columns = {this.state.setInfo}
                qreturn = {this.state.queryResults}
                fetch={this.fetch.bind(this)}
                queryResults={this.state.queryResults}
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

    var columnNames = this.state.selColumns;
    console.log("set columnNames: ", this.state.selColumns);

    var seCol = JSON.parse(file);

    var info = this.state.setInfo;
    console.log("Info: ", info);

    for (var j = 0; j < (columnNames.length); j++) {

        if ("Airport ID" ==columnNames[j]) {
            finalStr = finalStr + "Airport ID: " + seCol.airports_id + "\n";
        }
        else if("Latitude" ==columnNames[j]) {
            finalStr = finalStr + "Latitude: " + seCol.Latitude+ "\n";
        }
        else if("Longitude" ==columnNames[j]) {
            finalStr = finalStr + "Longitude: " + seCol.Longitude+ "\n";
        }
        else if ("Airport Code" ==columnNames[j]) {
            finalStr = finalStr + "Airport Code: " + seCol.airports_code + "\n";
        }
        else if ("Type" ==columnNames[j]) {
            finalStr = finalStr + "Type: " + seCol.airports_type + "\n";
        }
        else if ("Elevation" ==columnNames[j]) {
            finalStr = finalStr + "Elevation: " + seCol.airports_elevation + "\n";
        }
        else if ("Municipality" ==columnNames[j]) {
            finalStr = finalStr + "Municipality: " + seCol.airports_municipality + "\n";
        }
        else if ("Airport Website" ==columnNames[j]) {
            finalStr = finalStr + "Website: " + seCol.airports_home_link + "\n";
        }
        else if ("Airport Wikipedia" ==columnNames[j]) {
            finalStr = finalStr + "Wikipedia: " + seCol.airports_wikipedia_link + "\n";
        }
        else if ("Country" ==columnNames[j]) {
            finalStr = finalStr + "Country: " + seCol.countries_name + "\n";
        }
        else if ("Continent" ==columnNames[j]) {
            finalStr = finalStr + "Continent: " + seCol.continents_name + "\n";
        }
        else if ("Region" ==columnNames[j]) {
            finalStr = finalStr + "Region: " + seCol.regions_name + "\n";
        }
        else if ("GPS Code" ==columnNames[j]) {
            finalStr = finalStr + "GPS Code: " + seCol.airports_gps_code + "\n";
        }
        else if ("Local Code" ==columnNames[j]) {
            finalStr = finalStr + "Local Code: " + seCol.airports_local_code + "\n";
        }
        else if ("Region Code" ==columnNames[j]) {
            finalStr = finalStr + "Region Code: " + seCol.regions_code + "\n";
        }
        // else{
        //     finalStr = finalStr + "Scheduled Service: " + seCol.scheduled_service + "\n";
        // }
    }
    console.log("FINAL: ", finalStr);
    return finalStr;
}

async selectColumns(file) {
    //console.log("Got File:", file);
    console.log(file[0]);
    console.log(file[0].startID.info);
    var options = ["Airport ID", "Latitude", "Longitude", "Airport Code", "Type",
                    "Elevation", "Municipality", "Airport Website",
                    "Airport Wikipedia", "Country", "Continent", "Region",
                    "GPS Code", "Local Code", "Region Code"]
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
        let start = file[i].startID.name; //get start from file i
        let end = file[i].endID.name; //get end from file i
        let dist = file[i].gcd;
        runTotal = runTotal + dist;

        var updatedStart = JSON.stringify(file[i].startID.info);
        updatedStart = this.startEndInfo(updatedStart);
        var updatedEnd = JSON.stringify(file[i].endID.info);
        updatedEnd = this.startEndInfo(updatedEnd);

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

    // This function sends `input` the server and updates the state with whatever is returned
    async fetch(type, input, setUnit, opt){
        //input.preventDefault();
        //console.log("THIS IS TYPE::: ", type);
        console.log("Fetching... ", input);
        console.log("Using Unit: ", setUnit);
        console.log("Using Optimization: ", opt);
        let request;

        //if text box
        if (type === "query") {
            request = {
                request: "query",
                description: input,
                unit : setUnit,
                optSelection : opt
            };
            console.log("Fetching Query");
        // if the button is clicked:
        } else {
            request = {
                request: "svg",
                description: "",
                unit : setUnit,
                optSelection : opt
            }
            console.log("Fetching SVG");
        }

        try{
            let jsonRet = await fetch(`http://localhost:4567/testing`,
                {
                    method: "POST",
                    body: JSON.stringify(request)
                });
            let ret = await jsonRet.json();
            let parsed = JSON.parse(ret);
            console.log("Got back: ", JSON.parse(ret));
            let parse = JSON.parse(ret);
            //console.log("THIS IS RET RESPONSE: ", parsed.response);

            if (parsed.response === "query") {
                this.setState({
                    queryResults: parsed.trip
                });

                console.log("queryResults", this.state.queryResults);

                //this will actually display it in the table
                this.browseFile(this.state.queryResults);
            // if it's not, we assume the response field is "svg" and contains the an svg image
            } else {

                this.setState({
                    svgResults: parse.contents
                })
            }

        }catch(e) {
            console.error("Error talking to server");
            console.error(e);
        }
    }


}
