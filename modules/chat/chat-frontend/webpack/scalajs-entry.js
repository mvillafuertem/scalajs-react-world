if (process.env.NODE_ENV === "production") {
    const opt = require("./chat-frontend-opt.js");
    opt.main();
    module.exports = opt;
} else {
    var exports = window;
    exports.require = require("./chat-frontend-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./chat-frontend-fastopt.js");
    fastOpt.main()
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
