const { defineConfig } = require("cypress");

module.exports = defineConfig({
  env: {
    VUE_APP_API_BASE_URL:
      process.env.VUE_APP_API_BASE_URL || "http://localhost:8080/legalflow",
  },
  component: {
    devServer: {
      framework: "vue",
      bundler: "vite",
    },
  },
});
