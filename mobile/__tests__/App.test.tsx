import React from 'react';
import renderer from 'react-test-renderer';
import App from '../src/App';

test('renders without crashing', () => {
  renderer.create(<App />);
});
