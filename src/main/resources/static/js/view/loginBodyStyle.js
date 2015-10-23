/**
 * Created by Order on 2015. 10. 23..
 */

var LoginPage = {
    bodyStyle : {
        height: "1000px",
        width: "100%",
        backgroundSize: "cover",
        background: "rgb(254,255,255)", /* Old browsers */
        background: "-moz-linear-gradient(top, rgba(254,255,255,1) 0%, rgba(221,241,249,1) 35%, rgba(160,216,239,1) 100%)", /* FF3.6+ */
        background: "-webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(254,255,255,1)), color-stop(35%,rgba(221,241,249,1)), color-stop(100%,rgba(160,216,239,1)))", /* Chrome,Safari4+ */
        background: "-webkit-linear-gradient(top, rgba(254,255,255,1) 0%,rgba(221,241,249,1) 35%,rgba(160,216,239,1) 100%)", /* Chrome10+,Safari5.1+ */
        background: "-o-linear-gradient(top, rgba(254,255,255,1) 0%,rgba(221,241,249,1) 35%,rgba(160,216,239,1) 100%)", /* Opera 11.10+ */
        background: "-ms-linear-gradient(top, rgba(254,255,255,1) 0%,rgba(221,241,249,1) 35%,rgba(160,216,239,1) 100%)", /* IE10+ */
        background: "linear-gradient(to bottom, rgba(254,255,255,1) 0%,rgba(221,241,249,1) 35%,rgba(160,216,239,1) 100%)", /* W3C */
        backgroundRepeat: "no-repeat"
    },

    Page : React.createClass({
        render: function () {
            return (
                <div style={LoginPage.bodyStyle}></div>
            );
        }
    })
};


ReactDOM.render(
    <LoginPage.Page />,
    document.querySelector('.background')
);