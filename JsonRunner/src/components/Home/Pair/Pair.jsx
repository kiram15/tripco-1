import React, {Component} from 'react';

let Pair = ({start, end, dist}) => <tbody
    className="pair">
    <tr>
        <td>
            <h5>{start}</h5>
        </td>
        <td>
            <h5>{end}</h5>
        </td>
        <td>
            <h5>{dist}</h5>
        </td>
    </tr>
</tbody>;

export default Pair;