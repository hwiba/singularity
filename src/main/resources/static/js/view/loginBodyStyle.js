/**
 * Created by Order on 2015. 10. 23..
 */

var LoginPage = {
    bodyStyle : {
        backgroundColor: "green",
        height: "500px",
        width: "500px"
    },

    background : React.createClass({
        render: function () {
            return (
                <div style={LoginPage.bodyStyle}></div>
            );
        }
    })
};


ReactDOM.render(
    <LoginPage.background />,
    document.querySelector('.background')
);