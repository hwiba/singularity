/**
 * Created by Order on 2015. 10. 23..
 */

import React from 'react';


export default class LoginPage extends React.Component {

    render() {
        return (
            <div>
                <h1>LoginPage Test</h1>
                <form>
                    <label>id</label>
                    <input type="text" />
                    <label>pw</label>
                    <input type="text" />
                    <button type="submit" >button</button>
                </form>
            </div>
        )
    }
}
