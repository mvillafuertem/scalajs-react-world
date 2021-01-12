if (process.env.NODE_ENV === "production") {
    const opt = require("./chat-frontend-opt.js");
    opt.main(null);
    module.exports = opt;
} else {
    window.require = require("./chat-frontend-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./chat-frontend-fastopt.js");
    fastOpt.main(null)
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
