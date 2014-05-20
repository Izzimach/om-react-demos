
TestComponent = React.createClass({
  render: function () {return React.DOM.div(null, "argh!");}
});

TestComponent2 = React.createClass({
  render: function() { return TestComponent();},
  componentWillMount: function() { window.console.log("mounting");},
  componentDidMount: function() { window.console.log("mounted");},
  componentWillUnmount: function() { window.console.log("unmounting");},
});

