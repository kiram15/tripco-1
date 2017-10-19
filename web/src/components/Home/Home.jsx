import React, {Component} from 'react'
import Dropzone from 'react-dropzone'
import Select from 'react-select';

class Home extends React.Component {
constructor(props) {
   super(props);
   this.state = {
       svgImage: ''
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
    let displaySVG = null;
    if(this.state.svgImage){
        displaySVG = (
            <div id="svgImage">
                <img src={this.state.svgImage} width="45%"/>
            </div>
        );
    }

    return <div className="home-container">
        <div className="inner">

  <p className="w3-myFont"><h2>T02 NEKA</h2></p>

            <p></p>
            <div className="app-container">
                <form id="SearchBar" onSubmit={this.props.fetch}>
                     <input className="search-button"
                            type="text"
                            id="txtSearch"
                            placeholder="What are you searching for?"
                     />
                     <button type="submit">Submit</button>
                </form>
            </div>
            <p></p>

            <Dropzone className="dropzone-style" onDrop={this.drop.bind(this)}>
                             <button>Open JSON File</button>
                        </Dropzone>
            <p></p>

            <h3 className="section-heading"><p className="w3-myFont">Choose Preferences</p></h3>
            

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


            <p className="w3-myFont"><h3>Itinerary</h3></p>
            <p></p>
            <table className="pair-table">
                <tr>
                    <td><h8><b><p className="w3-myFont"> Start Name </p></b></h8></td>
                    <td><h8><b><p className="w3-myFont"> End Name </p></b></h8></td>
                    <td><h8><b><p className="w3-myFont"> Distance (mi) </p></b></h8></td>
                    <td><h8><b><p className="w3-myFont"> Total Distance (mi)</p></b></h8></td>
                </tr>
                {this.props.pairs}
                <tbody>
                    <tr>
                        <td colSpan="4"><p className="w3-myFont">Total:</p></td>
                        <td>{total}</td>
                    </tr>
                </tbody>
            </table>
            <p></p>
             <Dropzone className="dropzone-style" onDrop={this.dropSVG.bind(this)}>
                <button>Open SVG Image</button>
             </Dropzone>
             {displaySVG}
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
}





export default Home
