import React, {Component} from 'react'
import Dropzone from 'react-dropzone';

class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
          svgImage: ''
        };
      }

    render() {
        let total = this.props.totalDist; //update the total here
        let displaySVG = null;
            if(this.state.svgImage){
                displaySVG = (<div className="svgImage"><img src={this.state.svgImage} width="70%"/></div>);
            }

        return <div className="home-container">
            <div className="inner">
		<h2>T02 NEKA</h2>
                <h3>Itinerary</h3>
                <h4>Select Preferences</h4>
                <div>
                  <input type="checkbox" id="IDCheck" name="preference" value="ID"></input>
                  <label for="IDCheck">ID</label>
                </div>
                <div>
                  <input type="checkbox" id="LatCheck" name="preference" value="Lat"></input>
                  <label for="LatCheck">Latitude</label>
                 </div>
                 <div>
                  <input type="checkbox" id="LonCheck" name="preference" value="Lon"></input>
                  <label for="LonCheck">Longitude</label>
                </div>
                <div>
                  <button type="submit">Submit</button>
                </div>
                <p></p>
                <Dropzone className="dropzone-style" onDrop={this.drop.bind(this)}>
                    <button>Open JSON File</button>
                </Dropzone>
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
                <Dropzone className="dropzone" onDrop={this.dropSVG.bind(this)}>
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
