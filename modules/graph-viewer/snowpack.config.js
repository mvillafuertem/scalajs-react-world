// Snowpack Configuration File
// See all supported options: https://www.snowpack.dev/reference/configuration

/** @type {import("snowpack").SnowpackUserConfig } */
module.exports = {
    mount: {
        "public": "/",
        "target/scala-2.13/graph-viewer-fastopt": "/"
    },
    packageOptions: {
        /* ... */
    },
    devOptions: {
        port: 3000,
        open: "chrome"
    },
    buildOptions: {
        out: "../../docs/graph-viewer",
        clean: true,
        sourcemap: true,
        baseUrl: "./"
    },
    optimize: {
        bundle: true,
        minify: true,
        target: 'es2018',
    }
};
