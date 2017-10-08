import React, {Component} from 'react';

let Pair = ({start, end, dist, total, startInfo, endInfo}) => <tbody
    className="pair">
    <tr>
        <td>
            <h7><center>{start}</center></h7>
            <h7><center>{startInfo}</center></h7>
        </td>
        <td>
            <h7><center>{end}</center></h7>
            <h7><center>{endInfo}</center></h7>
        </td>
        <td>
            <h7><center>{dist}</center></h7>
        </td>
        <td>
            <h7><center>{total}</center></h7>
        </td>
    </tr>
</tbody>;

export default Pair;