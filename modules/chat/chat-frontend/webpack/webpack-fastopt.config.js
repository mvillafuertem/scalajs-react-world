var merge = require('webpack-merge');
var core = require('./webpack-core.config.js')

var generatedConfig = require("./scalajs.webpack.config.js");
const entries = {};
entries[Object.keys(generatedConfig.entry)[0]] = "scalajs";

module.exports = merge(core, {
  devtool: "cheap-module-eval-source-map",
  entry: entries,
  output: {
    // https://stackoverflow.com/questions/49207826/webpack-dev-server-historyapifallback-not-working-in-case-of-multilevel-routing
    publicPath : '/' // https://stackoverflow.com/questions/43191569/html-webpack-plugin-and-webpack-2-no-starting-slash
  },
  module: {
    noParse: (content) => {
      return content.endsWith("-fastopt.js");
    },
    rules: [
      {
        test: /\-fastopt.js$/,
        use: [ require.resolve('./fastopt-loader.js') ]
      }
    ]
  }
})
