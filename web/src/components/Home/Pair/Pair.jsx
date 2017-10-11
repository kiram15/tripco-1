import React, {Component} from 'react';

let Pair = ({start, end, dist, total, startInfo, endInfo}) => <tbody
    className="pair">
    <tr>
        <td>
            <h5>{start}</h5>
            <p></p>
            <h7>{startInfo}</h7>
        </td>
        <td>
            <h5>{end}</h5>
            <p></p>
            <h7>{endInfo}</h7>
        </td>
        <td>
            <h7>{dist}</h7>
        </td>
        <td>
            <h7>{total}</h7>
        </td>
    </tr>
</tbody>;

export default Pair;