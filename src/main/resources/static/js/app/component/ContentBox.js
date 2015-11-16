/**
 * Created by hyva on 2015. 11. 17..
 */

import React from 'react';

export default class ContentBox extends React.Component {

    render() {
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-sm-3 col-md-2 sidebar">
                        <ul className="nav nav-sidebar">
                            <li className="active">
                                <a href="#">Group으로 돌아가기</a>
                            </li>
                            <li className="active">
                                <a href="#">Group으로 돌아가기</a>
                            </li>
                            <li className="active">
                                <a href="#">Group으로 돌아가기</a>
                            </li>
                            <li className="active">
                                <a href="#">Group으로 돌아가기</a>
                            </li>
                        </ul>
                    </div>

                    <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                        <h1 className="page-header">{this.props.pageHeader}</h1>
                    </div>
                </div>
            </div>
        )
    }
}