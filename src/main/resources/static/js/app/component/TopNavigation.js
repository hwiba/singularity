/**
 * Created by hyva on 2015. 10. 23..
 */

import React from 'react';


export default class TopNavigation extends React.Component {
    render() {
        return (
            <nav className="navbar navbar-inverse navbar-fixed-top">
                <div className="container-fluid">
                    <div className="navbar-header">
                        <a href="/" className="navbar-brand">Singularity</a>
                    </div>

                    <div className="navbar-collapse collapse">
                        <ul className="nav navbar-nav navbar-right">
                            <li><a href="#">profile</a></li>
                            <li><a href="#">login</a></li>
                        </ul>
                        <form className="navbar-form navbar-right">
                            <input type="text" className="form-control" placeholder="Search..." />
                        </form>
                    </div>
                </div>
            </nav>
        )
    }
}
