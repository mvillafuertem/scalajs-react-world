if (process.env.NODE_ENV === "production") {
    const opt = require("./my-app-opt.js");
    opt.main();
    module.exports = opt;
} else {
    var exports = window;
    exports.require = require("./my-app-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./my-app-fastopt.js");
    fastOpt.main()
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
