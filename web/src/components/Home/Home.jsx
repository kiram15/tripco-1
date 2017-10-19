import React, {Component} from 'react'
import Dropzone from 'react-dropzone'
import Select from 'react-select';

class Home extends React.Component {
constructor(props) {
   super(props);
   this.state = {
       svgImage : [],
       input : []
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


    let total = this.props.totalDist; //update the total here
    let svg = this.props.svg;
    let txtSearch;
    let displaySVG;
    if(this.state.svgImage){
        displaySVG = (<div className="svgImage"><img src={this.state.svgImage} width="70%"/></div>);
    }

    return <div className="home-container">
        <div className="inner">

  <h2>T02 NEKA</h2>
            <h3>Itinerary</h3>
            <p></p>
            <div className="app-container">

                <form onSubmit={this.handleSubmit.bind(this)}>
                    <input size="35" className="search-button" type="text"
                        onKeyUp={this.keyUp.bind(this)} placeholder="What are you searching for?" autoFocus/>
                    <input type="submit" value="Submit" />
                </form>

            </div>
            <br />
            <br />
            <button type="button" onClick={this.buttonClicked.bind(this)}>Click here for an SVG</button>
            <p></p>
            <Dropzone className="dropzone-style" onDrop={this.drop.bind(this)}>
                <button>Open JSON File</button>
            </Dropzone>
            <p></p>

            <h3 className="section-heading">Choose Preferences</h3>
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

            <p></p>
            <table className="pair-table">
                <tr>
                    <td><h8><b> Start Name </b></h8></td>
                    <td><h8><b> End Name </b></h8></td>
                    <td><h8><b> Distance (mi) </b></h8></td>
                    <td><h8><b> Total Distance (mi)</b></h8></td>
                </tr>
                {this.props.pairs}
                <tbody>
                    <tr>
                        <td colSpan="4">Total:</td>
                        <td>{total}</td>
                    </tr>
                </tbody>
            </table>
            <p></p>
             <Dropzone className="dropzone-style" onDrop={this.dropSVG.bind(this)}>
                <button>Open SVG Image</button>
             </Dropzone>
             {svg}
        </div>
    </div>
}

drop(acceptedFiles) {
    console.log("Accepting drop");
    acceptedFiles.forEach(file => {
        console.log("Filename:", file.name, "File:", file);
        console.log(JSON.stringify(file));
        let fr = new FileReader();
        fr.onload = (function () {
            return function (e) {
                let JsonObj = JSON.parse(e.target.result);
                console.log(JsonObj);
                this.props.browseFile(JsonObj);
            };
        })(file).bind(this);

        fr.readAsText(file);
    });
}

dropSVG(acceptedFiles) {
    console.log("Accepting SVG drop");
    acceptedFiles.forEach(file => {
        console.log("Filename:", file.name, "File:", file);
        let fr = new FileReader();
        fr.onload = () => this.setState({ svgImage: fr.result })
        (file).bind(this);
        fr.readAsDataURL(file);
    });
}

keyUp(event) {
    if (event.which === 13) { // Waiting for enter to be pressed. Enter is key 13: https://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
        this.props.fetch("query", this.state.input); // Call fetch and pass whatever text is in the input box
    } else {
        this.setState({
            input: event.target.value
        });
    }
}

handleSubmit(event) {
    this.props.fetch("query", this.state.input);
    event.preventDefault();
}

buttonClicked(event) {
    this.props.fetch("svg", event.target.value);
}



}

export default Home