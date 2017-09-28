import React, {Component} from 'react';
import Dropzone from 'react-dropzone'
import Select from 'react-select'

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selColumns : []
        }
    }
    logChange(val) {
        if (this.state.selColumns.indexOf(val) == -1) {
            this.state.selColumns.push(val);
        }
        console.log("Selected: " + this.state.selColumns);
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
        return <div className="home-container">
            <div className="inner">

      <h2>T02 NEKA</h2>
                <h3>Itinerary</h3>
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
                        onChange={this.logChange.bind(this)}
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
}

export default Home
