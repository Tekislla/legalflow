import { route } from "quasar/wrappers";
import {
  createRouter,
  createMemoryHistory,
  createWebHistory,
  createWebHashHistory,
} from "vue-router";
import routes from "./routes";
import store from "../store";

export default route(function () {
  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : process.env.VUE_ROUTER_MODE === "history"
    ? createWebHistory
    : createWebHashHistory;

  const Router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    routes,

    history: createHistory(process.env.VUE_ROUTER_BASE),
  });

  Router.beforeEach((to, from, next) => {
    const isAuthenticated = !!store.state.token;
    const userRole = store.state.usuario.role;

    if (to.matched.some((record) => record.meta.requiresAuth)) {
      if (!isAuthenticated) {
        return next("/auth/login");
      }

      if (userRole === "ADMIN") {
        if (to.path === "/") {
          return next("/dashboard");
        } else if (to.meta.requiresAdmin) {
          return next();
        }
      } else if (userRole === "USER") {
        if (to.path === "/") {
          return next("/dashboard");
        } else if (to.meta.requiresAdmin) {
          return next("/home");
        }
      }

      return next();
    } else {
      next();
    }
  });

  return Router;
});
