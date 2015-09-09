/**
 * 
 */

var Spinbox = React.createClass({
    getInitialState : function(){
        return {
            value: this.props.value || 200
        }
    },
    render: function() {
        return (
                <div>
                    <input type="text" value={ this.state.value }></input>
                    <button onClick={ this.decrease } >▼</button>
                    <button onClick={ this.increase } >▲</button>
                </div>
            );
    },
    decrease : function(){
        this.setState( { value: --this.state.value } );
    },
    increase : function(){
        this.setState( { value: ++this.state.value } );
    }
  });

  React.render(
    <Spinbox value="300" />,
    document.getElementById('example')
  );