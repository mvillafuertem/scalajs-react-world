package io.github.mvillafuertem.components

import slinky.core.facade.ReactElement
import slinky.web.html._
import typings.materialUiCore.components.{ ListItem, ListItemIcon, ListItemText, ListSubheader }
import typings.materialUiIcons.{ components => Icon }
import typings.reactRouterDom.components.{ Link, NavLink }

// https://github.com/mui-org/material-ui/blob/v3.x/docs/src/pages/getting-started/page-layout-examples/dashboard/listItems.js
object ListItems {

  val mainListItems: ReactElement =
    div(
      ListItem
        .withComponent(_ => Link.component)
        .set("to", "/")(
          ListItemIcon(Icon.Dashboard()),
          ListItemText.primary("Dashboard")
        ),
      ListItem()
        .withComponent(_ => NavLink.component)
        .set("to", "/orders")(
          ListItemIcon(Icon.ShoppingCart()),
          ListItemText.primary("Orders")
        ),
      ListItem()
        .withComponent(_ => NavLink.component)
        .set("to", "/customers")(
          ListItemIcon(Icon.People()),
          ListItemText.primary("Customers")
        ),
      ListItem()
        .withComponent(_ => NavLink.component)
        .set("to", "/reports")(
          ListItemIcon(Icon.BarChart()),
          ListItemText.primary("Reports")
        ),
      ListItem()
        .withComponent(_ => NavLink.component)
        .set("to", "/integrations")(
          ListItemIcon(Icon.Layers()),
          ListItemText.primary("Integrations")
        )
    )

  val secondaryListItems: ReactElement =
    div(
      ListSubheader.inset(true)("Saved reports"),
      ListItem.button(true)(
        ListItemIcon(Icon.Assignment()),
        ListItemText.primary("Current month")
      ),
      ListItem.button(true)(
        ListItemIcon(Icon.Assignment()),
        ListItemText.primary("Last quarter")
      ),
      ListItem.button(true)(
        ListItemIcon(Icon.Assignment()),
        ListItemText.primary("Year-end sale")
      )
    )
}
