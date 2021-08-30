<template id="packages">
  <app-frame>
    <v-card v-if="packages" flat>
      <v-card-title>
        <v-text-field
          small
          v-model="search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
      </v-card-title>
      <v-data-table
        dense
        :headers="headers"
        :items="packages"
        :search="search"
      ></v-data-table>
    </v-card>
  </app-frame>
</template>
<script>
Vue.component("packages", {
  template: "#packages",
  data: () => ({
    isLoading: false,
    packages: null,
    search: "",
    headers: [
      {
        text: "Packages",
        align: "start",
        // filterable: false,
        value: "Package",
      },
      { text: "Version", value: "Version" },
    ],
  }),
  created() {
    this.isLoading = true;
    axios
      .post("http://localhost:8081/eval/file?filename=packages.R")
      .then((res) => {
        (this.packages = res.data), (this.isLoading = false);
      })
      // .catch(() => alert("Error while fetching packages"));
  },
});
</script>

