/**
 * Created by hyva on 2015. 11. 17..
 */

import React from 'react';
import ReactDOM from 'react-dom';

import TopNavigation from "./component/TopNavigation";
import ContentBox from "./component/ContentBox";

ReactDOM.render(
    <div className="container">
        <TopNavigation />
        <ContentBox pageHeader="test"/>
    </div>
    , document.getElementById("reactRenderingPoint")
)