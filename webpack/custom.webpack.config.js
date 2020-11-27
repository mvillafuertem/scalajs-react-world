var merge = require('webpack-merge');
var generated = require('./scalajs.webpack.config');

var local = {
    devServer: {
        historyApiFallback: true // https://blog.jimmydc.com/webpack-history-api-fallback/
    },
    module: {
        rules: [
            {
                test: /\.(css|scss)$/,
                use: ['style-loader', 'css-loader', 'sass-loader']
            },
            {
                test: /\.(ttf|eot|woff|woff2|png|glb|svg)$/,
                use: 'file-loader'
            },
            {
                test: /\.(eot)$/,
                use: 'url-loader'
            }
        ]
    }
};

module.exports = merge(generated, local);
