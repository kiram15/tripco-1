import React, {Component} from 'react'
import Dropzone from 'react-dropzone'
import Select from 'react-select';
import InlineSVG from 'svg-inline-react';


class Home extends React.Component {
constructor(props) {
   super(props);
   this.state = {
       svgImage : [],
       input : [],
       unit : "miles",
       optimization : "None",
       selectedLocations: [],
       pastQuery: "",
       qRLength: 0,
       saveIDList : [],
       saveLats : [],
       saveLons : []
   };


}

render() {

    var options = [];
    for (var i = 0; i < (this.props.columns.length); i++) {
        var ob = new Object();
        ob.value=this.props.columns[i];
        ob.label=this.props.columns[i];
        options.push(ob);
    }

    var dragula = require('react-dragula');
    dragula([document.getElementById("checkedLocations")]);

    //different query if there is a different search string
    if(this.state.pastQuery !== this.props.currentQuery){
        var myDiv = document.getElementById("searchResult");
        for (var i = 0; i < (this.props.queryResults.length); i++) {
            var checkBox = document.createElement("input");
            var label = document.createElement("label");
            var br = document.createElement("br");
            checkBox.type = "checkbox";
            checkBox.value = this.props.queryResults[i].name;
            myDiv.appendChild(checkBox);
            myDiv.appendChild(label);
            label.appendChild(document.createTextNode(this.props.queryResults[i].name));
            this.state.saveIDList.push({name : this.props.queryResults[i].name, id : this.props.queryResults[i].info.airports_code});
            this.state.saveLats.push({name : this.props.queryResults[i].name, lat : this.props.queryResults[i].lat});
            this.state.saveLons.push({name : this.props.queryResults[i].name, lon : this.props.queryResults[i].lon});
            myDiv.appendChild(br);
        }
        console.log("Save list: ", this.state.saveIDList);
        this.state.pastQuery = this.props.currentQuery;
    }

    let total = this.props.totalDist; //update the total here
    let svg = this.props.svg;
    let txtSearch;
    let displaySVG;
    let renderedSvg;
    if(this.props.svg){
        displaySVG = <InlineSVG src={svg}></InlineSVG>;

    }

    return <div className="home-container">

<div className="inner">
    <div id="background"></div>

  <p className="w3-myFont"><h2>T02 NEKA</h2></p>

  <div className="app-container">
    <form onSubmit={this.handleSubmit.bind(this)}>
        <input id="searchTB" size="35" className="search-button" type="text"
        onKeyUp={this.keyUp.bind(this)} placeholder="What are you searching for?" autoFocus/>
        <input id="subButton" type="submit" value="Submit" />
    </form>
  </div>
  <p></p>

  <p className="w3-myFont">Search Results</p>
  <div id="searchResult">
  search to see results
  </div>
  <p></p>

  <button type="button" onClick={this.updateSelectedLocations.bind(this)}>Select</button>
  <button type="button" onClick={this.selectAll.bind(this)}>Select All</button>
  <button type="button" onClick={this.clearAll.bind(this)}>Clear All</button>
  <p></p>

  <p className="w3-myFont">Selected Locations</p>
  <div id="checkedLocations">
  </div>
  <p></p>

    <div className="optimization">
          Select your optimizations:<p></p>
          <button type="button" onClick={this.milesClicked.bind(this)}>Miles</button>
          <button type="button" onClick={this.kiloClicked.bind(this)}>Kilometers</button>
          <p></p>
          <button type="button" onClick={this.NoneClicked.bind(this)}>None</button>
          <button type="button" onClick={this.NNClicked.bind(this)}>Nearest Neighbor</button>
          <button type="button" onClick={this.TwoOptClicked.bind(this)}>2-opt</button>
          <button type="button" onClick={this.ThreeOptClicked.bind(this)}>3-opt</button>
    </div>
    <p></p>

    <Dropzone className="dropzone-style" onDrop={this.uploadButtonClicked.bind(this)}>
        <button type="button" > Upload a location file</button>
    </Dropzone>

    <button type="button" onClick={this.saveButtonClicked.bind(this)}>Save Trip</button>
    <button type="button" onClick={this.saveKMLClicked.bind(this)}>Save Map File</button>
    <button type="button" onClick={this.planTrip.bind(this)}>Plan</button>
    <p></p>

  <button type="button" onClick={this.buttonClicked.bind(this)}>Click here for an SVG</button>
  <p></p>
  {displaySVG}
  <p></p>

  <div className="Itinerary">
    <div id="Itin" >
        <p className="w3-myFont"><h3>Itinerary</h3></p>
        <p></p>
        <table className="pair-table">
            <tr>
                <td><h8><b><p className="w3-myFont"> Start Name </p></b></h8></td>
                <td><h8><b><p className="w3-myFont"> End Name </p></b></h8></td>
                <td><h8><b><p className="w3-myFont"> Distance </p></b></h8></td>
                <td><h8><b><p className="w3-myFont"> Total Distance </p></b></h8></td>
            </tr>
            {this.props.pairs}
            <tbody>
                 <tr>
                    <td colSpan="4"><p className="w3-myFont">Total:</p></td>
                    <td>{total}</td>
                 </tr>
            </tbody>
        </table>
    </div>

    <div id="Prefs" >
         <p className="w3-myFont"><h3>Choose Preferences</h3></p>
         <div className = "select-value">
         <Select
              name="form-field-name"
              options={options}
              onChange={this.props.onClick}
              simpleValue = {true}
              closeOnSelect = {false}
              multi={true}
              searchable = {false}
              backspaceToRemoveMethod=""
         />
    </div>
  </div>
</div>
</div>
</div>
}

keyUp(event) {
    if (event.which === 13) { // Waiting for enter to be pressed. Enter is key 13: https://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
        this.props.fetch("query", this.state.input, this.state.unit, this.state.optimization); // Call fetch and pass whatever text is in the input box

    } else {
        this.setState({
            input: event.target.value
        });

    }
}

handleSubmit(event) {
    document.getElementById("searchResult").innerHTML = "";
    this.props.fetch("query", this.state.input, this.state.unit, this.state.optimization);

    event.preventDefault();
}

planTrip(event){
    var reorderedSL = [];
    var dragulaDivs = document.getElementById("checkedLocations").children;
    for(var i = 0; i < dragulaDivs.length; i++){
        reorderedSL[i] = dragulaDivs[i].innerHTML;
    }
    console.log("reorderedSL", reorderedSL);
    this.props.fetch("plan", reorderedSL, this.state.unit, this.state.optimization);
}

buttonClicked(event) {
    this.props.fetch("svg", event.target.value, this.state.unit, this.state.optimization);
}

milesClicked(event){
    this.setState({
        unit : "miles"
    });
    console.log("Units are miles");
}

kiloClicked(event){
    this.setState({
        unit : "km"
    });
    console.log("Units are km");
}

NoneClicked(event){
    this.setState({
        optimization : "None"
    });
    console.log("Opt is none");
}

NNClicked(event){
    this.setState({
        optimization : "NearestNeighbor"
    });
    console.log("Opt is NearestNeighbor");
}

TwoOptClicked(event){
    this.setState({
        optimization : "TwoOpt"
    });
    console.log("Opt is TwoOpt");
}

ThreeOptClicked(event){
    this.setState({
        optimization : "ThreeOpt"
    });
    console.log("Opt is ThreeOpt");
}

saveButtonClicked(event){
    var airportIDs = [];
    for(var i = 0; i < this.state.saveIDList.length; i++){
        for(var j = 0; j < this.state.selectedLocations.length; j++){
            if(this.state.saveIDList[i].name === this.state.selectedLocations[j]){
                airportIDs.push(this.state.saveIDList[i].id);
            }
        }
    }
    this.props.getFile(airportIDs, "json", this.state.selectedLocations);
}

saveKMLClicked(event){
    var lats = [];
    var lons = [];
    for(var i = 0; i < this.state.saveLats.length; i++){
        for(var j = 0; j < this.state.selectedLocations.length; j++){
            if(this.state.saveLats[i].name === this.state.selectedLocations[j]){
                lats.push(this.state.saveLats[i].lat);
            }
        }
    }
    for(var i = 0; i < this.state.saveLons.length; i++){
        for(var j = 0; j < this.state.selectedLocations.length; j++){
            if(this.state.saveLons[i].name === this.state.selectedLocations[j]){
                lons.push(this.state.saveLons[i].lon);
            }
        }
    }
    var both = []
    both.push(lats);
    both.push(lons);
    // console.log("Latitudes: ", lats);
    // console.log("longitudes: ", lons);
    // console.log("both ", both);
    this.props.getFile(both, "kml", this.state.selectedLocations);
    //for(var i = 0; i < this.state.)
}

//File reading is almost identical how you did it in Sprint 1
uploadButtonClicked(acceptedFiles) {
    document.getElementById("searchResult").innerHTML = "";
    console.log("Accepting drop");
    acceptedFiles.forEach(file => {
        console.log("Filename:", file.name, "File:", file);
        console.log(JSON.stringify(file));
        let fr = new FileReader();
        fr.onload = (function () {
            return function (e) {
                let JsonObj = JSON.parse(e.target.result);
                console.log(JsonObj);
                // Do something with the file:
                this.props.fetch("upload", JsonObj, this.state.unit, this.state.optimization);
                //this.props.browseFile(JsonObj);
            };
        })(file).bind(this);

        fr.readAsText(file);
    });
}

updateSelectedLocations(event) {
    var parentDiv = document.getElementById("searchResult");
    var locations = parentDiv.getElementsByTagName("input");
    var tempSLIndex = this.state.selectedLocations.length;
    for (var i = 0; i < locations.length; i++) {
        //do something with the checked location - add to selected locations array??
        if (locations[i].checked && (this.state.selectedLocations.includes(locations[i].value) === false)) {
            this.state.selectedLocations[tempSLIndex] = locations[i].value;
            tempSLIndex++;
        }
    }
    console.log("selectedLocations:", this.state.selectedLocations);
    document.getElementById("checkedLocations").innerHTML = "";
    var wrapper = document.getElementById("checkedLocations");
        for (var i = 0; i < (this.state.selectedLocations.length); i++) {
	    var selected = document.createElement("div");
            var textNode = document.createTextNode(this.state.selectedLocations[i]);
	    selected.appendChild(textNode);
            wrapper.appendChild(selected);
        }
}

selectAll(source) {
  var parentDiv = document.getElementById("searchResult");
  var checkboxes = parentDiv.getElementsByTagName("input");
  for(var i=0, n=checkboxes.length;i<n;i++) {
    checkboxes[i].checked = true;
  }
  this.updateSelectedLocations(this);
}

clearAll(source) {
  var parentDiv = document.getElementById("searchResult");
  var checkboxes = parentDiv.getElementsByTagName("input");
  for(var i=0, n=checkboxes.length;i<n;i++) {
    checkboxes[i].checked = false;
  }
    this.state.selectedLocations = [];
    this.updateSelectedLocations(this);
}

}

export default Home
