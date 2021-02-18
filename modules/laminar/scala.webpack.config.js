const webpack = require("webpack")
const merge = require("webpack-merge")
const UglifyJsPlugin = require("uglifyjs-webpack-plugin")
const path = require("path")
const CopyWebpackPlugin = require("copy-webpack-plugin")

function libraryOutput(dest) {
    return {
        output: {
            path: dest,
            filename: "[name].js",
            library: "[name]",
            libraryTarget: "var",
        }}
}

// These css style loaders are used for both plain js/ts files
// as well as any directly impported css files in scala.js.
const finalStyleLoaders = [
    { loader: "style-loader" },
    // may want to replace with types-for-css-modules-loader
    { loader: "css-loader", options: { modules: true, importLoaders: 1 } },
    {
        loader: "postcss-loader",
        options: {
            ident: 'postcss',
            plugins: (loader) => [
                require("postcss-import")({ root: loader.resourcePath }),
                require("postcss-mixins")(),
                require("postcss-cssnext")({
                    //https://github.com/MoOx/postcss-cssnext/blob/master/src/features.js
                    features: {
                        customProperties: false,
                        applyRule: false,
                        calc: false,
                    }
                }),
                require("postcss-reporter")({ clearMessages: true }),
            ]
        }
    }
]

// Static content is served from "dist". Webpack dynamically bundled
// content is served from / since there is no publicPath in the
// devServer definition below or in webpack.output.
const devServer = {
    contentBase: "dist",
    compress: true,
    hot: true,
    open: true,
    //https: true, // setting to true requires to accept self signed certs
    watchContentBase: true,
    headers: {
        'Access-Control-Allow-Origin': '*'
    }
}

// scalapath: relative path from topdir to scala output .js file
const common = (scalapath) => ({
    // The entry point for this app is a scala entry point but
    // this is not required. You could have your entry point
    // through a js file as you normally would in a js application.
    entry: {
        "Scala": scalapath,
    },
    target: "web",
    resolve: {
        // If using symlinks in node_modules, you need this false so webpack does
        // *not* use the symlink's resolved absolute path as the directory hierarchy.
        symlinks: false,
        extensions: [".ts", ".tsx", ".js", ".jsx", ".json", "*"],
        alias: {
            JS: path.resolve(__dirname, "./src/main/js"),
            Public: path.resolve(__dirname, "./src/main/public"),
            App: path.resolve(__dirname, "./src/main/scala/io/github/mvillafuertem"),
        },
    },
    module: {
        rules: [
            {
                // Load css, convert to js object, also load via stylesheet.
                test: /\.css(\.js)?$/,
                use: finalStyleLoaders
            },
            {
                // Order is important for this loader, run after babel but before other css loaders. Why not postcss-js?
                test: /\.css\.js?$/,
                use: [{ loader: "css-js-loader" }]
            },
            {
                test: /\.jsx?$/, // picks up pure .js and .jsx
                exclude: /node_modules/,
                include: [
                    path.resolve(__dirname, "./src/main/js")
                ],
                use: ["babel-loader"]
            },
            {
                test: /\.tsx$|\.ts$/,
                include: [
                    path.resolve(__dirname, "./src/main/js")
                ],
                exclude: [
                    /node_modules/,
                    /__tests__/,
                ],
                use: [
                    { loader: "babel-loader" },
                    {
                        loader: "ts-loader",
                        options: {
                            compilerOptions: {
                                // override paths, should use override plugin :-)
                                paths: {
                                    "ScalaJS": [scalapath]
                                }
                            }
                        }
                    }
                ]
            },
            {
                test: /\.(png|jpg|gif)$/,
                use: ["url-loader"]
            },
            {
                test: /\.js$/,
                use: ["scalajs-friendly-source-map-loader"],
                enforce: "pre",
                exclude: [/node_modules/],
            },
            {
                test: /\.md$/,
                use: "raw-loader"
            }
        ]
    },
    devServer: devServer
})

function copies(dest) {
    return [
        { from: "src/main/public/*.html", to: dest, flatten: true },
    ]
}

const dev = {
    devtool: "source-map",
    mode: "development",
}

const prod = {
    devtool: "source-map",
    mode: "production"
}

module.exports = function (env) {
    const isProd = env && env.BUILD_KIND && env.BUILD_KIND==="production"
    // the "app" name must be coordinated with build.sbt
    const scalapath = path.join(__dirname, "./target/scala-2.13/laminar-" + (isProd ? "opt.js":"fastopt.js"))
    const staticAssets = copies(path.join(__dirname, "dist"))
    const output = libraryOutput(path.join(__dirname, "dist"))
    const globals = (nodeEnv) => ({
        "process.env": { "NODE_ENV": JSON.stringify(nodeEnv || "development") }
    })
    const copyplugin = new CopyWebpackPlugin(staticAssets)
    //console.log("copyplugin", copyplugin, staticAssets)
    console.log("isProd: ", isProd)
    console.log("scalapath: ", scalapath)
    const modeNone = { mode: "none" }

    if (isProd) {
        const g = globals("production")
        console.log("Production build")
        console.log("globals: ", g)
        return merge(output, common(scalapath), modeNone, prod, {
            plugins: [
                new webpack.DefinePlugin(g),
                new UglifyJsPlugin({
                    cache: true,
                    parallel: 4,
                    sourceMap: true,
                    uglifyOptions: { ecma: 5, compress: true }
                }),
                copyplugin, // must be first
            ]
        })
    }
    else {
        const g = globals()
        console.log("Dev build")
        console.log("globals: ", g)
        return merge(output, common(scalapath), modeNone, dev, {
            plugins: [
                new webpack.HotModuleReplacementPlugin(),
                new webpack.DefinePlugin(g),
                copyplugin,
            ]
        })
    }
}