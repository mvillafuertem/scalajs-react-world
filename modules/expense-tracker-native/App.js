import React from 'react';
import {Text, View} from 'react-native';

let app;
if (__DEV__) {
  app = require("./target/scala-2.13/expense-tracker-native-fastopt.js").app;
} else {

  // uncomment the following line to enable opt building
  // app = require("./target/scala-2.13/expense-tracker-native-opt.js").app;
}
export default app;
