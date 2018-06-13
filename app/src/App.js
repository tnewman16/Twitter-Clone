import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';
import { Home } from './pages';
import './App.css';

class App extends Component {
  render() {
    return (
      <div className="App">
        <Router>
          <div>
            <ul>
              <li>
                <Link to="/">Home</Link>
              </li>
            </ul>
            <Route exact path="/" component={Home} />
          </div>
        </Router>
      </div>
    );
  }
}

export default App;
