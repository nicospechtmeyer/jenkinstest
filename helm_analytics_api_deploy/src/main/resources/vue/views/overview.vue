<template id="overview">
  <app-frame>
    <v-container>
      <v-row>
        <v-col>
          <h1 class="mt-5">CONTAINER Diagnostics</h1>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-tabs class="mt-3">
            <v-tab key="packages">
              Packages
              <!-- <v-chip
      class="ma-2"
      label
      small
    >
    </v-chip> -->
            </v-tab>
            <v-tab key="tests">Tests</v-tab>
            <v-tab-item key="packages">
              <packages />
            </v-tab-item>
            <v-tab-item key="tests">
              <ul>
                <li v-for="test in tests" :key="test">
                  {{ test }}
                </li>
              </ul>
            </v-tab-item>
          </v-tabs>
        </v-col>
      </v-row>
    </v-container>
  </app-frame>
</template>
<script>
Vue.component("overview", {
  template: "#overview",
  data: () => ({
    isLoading: false,
    tests: null,
  }),
  created() {
    this.isLoading = true;
    axios
      .post("http://localhost:8081/eval/file?filename=tests.R")
      .then((res) => {
        (this.tests = JSON.parse(res.data)), (this.isLoading = false);
      })
      // .catch(() => alert("Error while fetching user"));
  },
});
</script>
<style>
/* .overview {
        color: goldenrod;
    } */
</style>
