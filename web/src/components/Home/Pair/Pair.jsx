import React, {Component} from 'react';

let Pair = ({start, end, dist, total, startInfo, endInfo}) => <tbody
    className="pair">
    <tr>
        <td>
            <h4>{start}</h4>
            <p></p>
            <h7>{startInfo}</h7>
        </td>
        <td>
            <h4>{end}</h4 s>
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