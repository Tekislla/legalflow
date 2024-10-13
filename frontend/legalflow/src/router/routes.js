const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    meta: { requiresAuth: true },
    children: [
      {
        path: "home",
        component: () => import("pages/IndexPage.vue"),
        meta: { requiresAuth: true },
      },
      {
        path: "dashboard",
        component: () => import("pages/DashboardPage.vue"),
        meta: { requiresAuth: true, requiresAdmin: true },
      },
      {
        path: "processos",
        component: () => import("pages/ListagemProcessos.vue"),
        meta: { requiresAuth: true },
      },
      {
        path: "usuarios",
        component: () => import("pages/ListagemUsuarios.vue"),
        meta: { requiresAuth: true, requiresAdmin: true },
      },
    ],
  },
  {
    path: "/auth",
    component: () => import("layouts/AuthLayout.vue"),
    children: [
      { path: "login", component: () => import("pages/LoginPage.vue") },
      { path: "cadastro", component: () => import("pages/CadastroPage.vue") },
    ],
  },
  {
    path: "/:catchAll(.*)*",
    component: () => import("pages/ErrorNotFound.vue"),
  },
];

export default routes;
