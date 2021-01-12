var path = require("path");
var merge = require('webpack-merge');
var core = require('./webpack-core.config.js')
var webpack = require("webpack");

var generatedConfig = require("./scalajs.webpack.config.js");
const entries = {};
entries[Object.keys(generatedConfig.entry)[0]] = "scalajs";

module.exports = merge(core, {
  mode: "production",
  devtool: "source-map",
  entry: entries,
  output: {
    path: path.resolve(__dirname, "../../../../target/build/public"),
    // https://stackoverflow.com/questions/49207826/webpack-dev-server-historyapifallback-not-working-in-case-of-multilevel-routing
    publicPath : '/' // https://stackoverflow.com/questions/43191569/html-webpack-plugin-and-webpack-2-no-starting-slash
  },
  optimization: {
    splitChunks: {
      chunks: 'all',
    },
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: JSON.stringify('production')
      }
    })
  ]
})
