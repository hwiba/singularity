/**
 * Created by hyva on 2015. 10. 23..
 */

import React from 'react';

import TopNavigation from "./TopNavigation";

export default class LoginPage extends React.Component {

    render() {
        return (
            <div>
                <TopNavigation />
                <h1>LoginPage Test</h1>
                <form>
                    <label>id</label>
                    <input type="text" />
                    <label>pw</label>
                    <input type="text" />
                    <button type="submit" >로그인</button>
                </form>
            </div>
        )
    }
}
