/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

 import React from 'react';
 import {
   Text,
   View,
 } from 'react-native';

 let app;
 if (__DEV__) {
   app = require("./target/scala-2.13/pokedex-native-fastopt/main.js").app;
 } else {

   // uncomment the following line to enable opt building
   // app = require("./target/scala-2.13/pokedex-native-opt.js").app;
 }

 export default app;
