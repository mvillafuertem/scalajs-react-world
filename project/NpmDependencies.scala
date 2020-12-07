object NpmDependencies {

  lazy val `calendar`: Seq[(String, String)] = Seq(
    Package.`@azure/msal-browser`               -> Version.`@azure/msal-browser`,
    Package.`@fortawesome/fontawesome-free`     -> Version.`@fortawesome/fontawesome-free`,
    Package.`@microsoft/microsoft-graph-client` -> Version.`@microsoft/microsoft-graph-client`,
    Package.`@types/microsoft-graph`            -> Version.`@types/microsoft-graph`,
    Package.`@types/react-router-dom`           -> Version.`@types/react-router-dom`,
    Package.`@types/reactstrap`                 -> Version.`@types/reactstrap`,
    Package.`bootstrap`                         -> Version.`bootstrap`,
    Package.`moment`                            -> Version.`moment`,
    Package.`moment-timezone`                   -> Version.`moment-timezone`,
    Package.`react-router-dom`                  -> Version.`react-router-dom`,
    Package.`reactstrap`                        -> Version.`reactstrap`,
    Package.`windows-iana`                      -> Version.`windows-iana`
  )

  lazy val `chat`: Seq[(String, String)] = Seq()

  lazy val `dashboard`: Seq[(String, String)] = Seq(
    Package.`@material-ui/core`       -> Version.`@material-ui/core`,
    Package.`@material-ui/styles`     -> Version.`@material-ui/styles`,
    Package.`@material-ui/icons`      -> Version.`@material-ui/icons`,
    Package.`recharts`                -> Version.`recharts`,
    Package.`@types/recharts`         -> Version.`@types/recharts`,
    Package.`@types/classnames`       -> Version.`@types/classnames`,
    Package.`react-router-dom`        -> Version.`react-router-dom`,
    Package.`@types/react-router-dom` -> Version.`@types/react-router-dom`
  )

  lazy val `gif-finder`: Seq[(String, String)] = Seq()

  lazy val `heroes`: Seq[(String, String)] = Seq()

  lazy val `journal`: Seq[(String, String)] = Seq(
    Package.`react-router-dom`         -> Version.`react-router-dom`,
    Package.`@types/react-router-dom`  -> Version.`@types/react-router-dom`,
    Package.`query-string`             -> Version.`query-string`,
    Package.`react-redux`              -> Version.`react-redux`,
    Package.`@types/react-redux`       -> Version.`@types/react-redux`,
    Package.`redux-devtools-extension` -> Version.`redux-devtools-extension`,
    Package.`redux`                    -> Version.`redux`,
    Package.`firebase`                 -> Version.`firebase`,
    Package.`redux-thunk`              -> Version.`redux-thunk`,
    Package.`validator`                -> Version.`validator`,
    Package.`@types/validator`         -> Version.`@types/validator`,
    Package.`sweetalert2`              -> Version.`sweetalert2`
  )

  lazy val `simple-test`: Seq[(String, String)] = Seq(
    Package.`@material-ui/core`       -> Version.`@material-ui/core`,
    Package.`@material-ui/styles`     -> Version.`@material-ui/styles`,
    Package.`@material-ui/icons`      -> Version.`@material-ui/icons`,
    Package.`@types/classnames`       -> Version.`@types/classnames`,
    Package.`react-router-dom`        -> Version.`react-router-dom`,
    Package.`@types/react-router-dom` -> Version.`@types/react-router-dom`,
    Package.`react-proxy`             -> Version.`react-proxy`,
    Package.`recharts`                -> Version.`recharts`,
    Package.`@types/recharts`         -> Version.`@types/recharts`
  )

  lazy val `reactNpmDeps`: Seq[(String, String)] = Seq(
    Package.`react`            -> Version.`react`,
    Package.`react-dom`        -> Version.`react-dom`,
    Package.`@types/react`     -> Version.`@types/react`,
    Package.`@types/react-dom` -> Version.`@types/react-dom`
  )

  lazy val `withCssLoading`: Seq[(String, String)] = Seq(
    Package.`webpack-merge` -> Version.`webpack-merge`,
    Package.`css-loader`    -> Version.`css-loader`,
    Package.`sass-loader`   -> Version.`sass-loader`,
    Package.`sass`          -> Version.`sass`,
    Package.`style-loader`  -> Version.`style-loader`,
    Package.`file-loader`   -> Version.`file-loader`,
    Package.`url-loader`    -> Version.`url-loader`
  )

  private object Package {
    val `query-string`                      = "query-string"
    val `react-redux`                       = "react-redux"
    val `@types/react-redux`                = "@types/react-redux"
    val `redux-devtools-extension`          = "redux-devtools-extension"
    val `redux`                             = "redux"
    val `firebase`                          = "firebase"
    val `redux-thunk`                       = "redux-thunk"
    val `validator`                         = "validator"
    val `@types/validator`                  = "@types/validator"
    val `sweetalert2`                       = "sweetalert2"
    val `@azure/msal-browser`               = "@azure/msal-browser"
    val `@fortawesome/fontawesome-free`     = "@fortawesome/fontawesome-free"
    val `@microsoft/microsoft-graph-client` = "@microsoft/microsoft-graph-client"
    val `@types/microsoft-graph`            = "@types/microsoft-graph"
    val `@types/reactstrap`                 = "@types/reactstrap"
    val `bootstrap`                         = "bootstrap"
    val `moment`                            = "moment"
    val `moment-timezone`                   = "moment-timezone"
    val `reactstrap`                        = "reactstrap"
    val `windows-iana`                      = "windows-iana"
    val `@material-ui/core`                 = "@material-ui/core"
    val `@material-ui/styles`               = "@material-ui/styles"
    val `@material-ui/icons`                = "@material-ui/icons"
    val `@types/classnames`                 = "@types/classnames"
    val `react-router-dom`                  = "react-router-dom"
    val `@types/react-router-dom`           = "@types/react-router-dom"
    val `react-proxy`                       = "react-proxy"
    val `recharts`                          = "recharts"
    val `@types/recharts`                   = "@types/recharts"
    val `react`                             = "react"
    val `react-dom`                         = "react-dom"
    val `@types/react`                      = "@types/react"
    val `@types/react-dom`                  = "@types/react-dom"
    val `webpack-merge`                     = "webpack-merge"
    val `css-loader`                        = "css-loader"
    val `sass-loader`                       = "sass-loader"
    val `sass`                              = "sass"
    val `style-loader`                      = "style-loader"
    val `file-loader`                       = "file-loader"
    val `url-loader`                        = "url-loader"
  }

  private object Version {
    val `query-string`                      = "6.13.1"
    val `react-redux`                       = "7.2.1"
    val `@types/react-redux`                = "7.1.9"
    val `redux-devtools-extension`          = "2.13.8"
    val `redux`                             = "4.0.5"
    val `firebase`                          = "7.21.1"
    val `redux-thunk`                       = "2.3.0"
    val `validator`                         = "13.1.0"
    val `@types/validator`                  = "13.1.0"
    val `sweetalert2`                       = "10.3.5"
    val `@azure/msal-browser`               = "2.3.0"
    val `@fortawesome/fontawesome-free`     = "5.14.0"
    val `@microsoft/microsoft-graph-client` = "2.1.0"
    val `@types/microsoft-graph`            = "1.18.0"
    val `@types/reactstrap`                 = "8.5.1"
    val `bootstrap`                         = "4.5.2"
    val `moment`                            = "2.27.0"
    val `moment-timezone`                   = "0.5.31"
    val `reactstrap`                        = "8.5.1"
    val `windows-iana`                      = "4.2.1"
    val `@material-ui/core`                 = "3.9.4" // note: version 4 is not supported yet
    val `@material-ui/styles`               = "3.0.0-alpha.10" // note: version 4 is not supported yet
    val `@material-ui/icons`                = "3.0.2"
    val `@types/classnames`                 = "2.2.10"
    val `react-router-dom`                  = "5.1.2"
    val `@types/react-router-dom`           = "5.1.2" // note 5.1.4 did weird things to the Link component
    val `react-proxy`                       = "1.1.8"
    val `recharts`                          = "1.8.5"
    val `@types/recharts`                   = "1.8.10"
    val `react`                             = "16.13.1"
    val `react-dom`                         = "16.13.1"
    val `@types/react`                      = "16.9.34"
    val `@types/react-dom`                  = "16.9.6"
    val `webpack-merge`                     = "4.2.2"
    val `css-loader`                        = "3.5.3"
    val `sass-loader`                       = "10.0.2"
    val `sass`                              = "1.26.11"
    val `style-loader`                      = "1.2.1"
    val `file-loader`                       = "6.0.0"
    val `url-loader`                        = "3.0.0"
  }

}
