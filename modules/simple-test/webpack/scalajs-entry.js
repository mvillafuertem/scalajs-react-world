if (process.env.NODE_ENV === "production") {
    const opt = require("./simple-test-opt.js");
    opt.main();
    module.exports = opt;
} else {
    var exports = window;
    exports.require = require("./simple-test-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./simple-test-fastopt.js");
    fastOpt.main()
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
