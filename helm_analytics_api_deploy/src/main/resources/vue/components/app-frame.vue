<template id="app-frame">
 <v-app>
    <v-main>
        <v-container class="px-6" fluid>
        <v-row>
          <v-col>
            <!-- <transition name="fade"> -->
            <slot></slot>
            <!-- </transition> -->
          </v-col>
        </v-row>
      </v-container>
    </v-app>
</template>
<script>
    Vue.component("app-frame", {template: "#app-frame"});
</script>
<style>
    .main {
    }
    html {
        font-family: sans-serif;
    }
    #app-frame {
        /* padding: 20px;
        background: #b6e2ff;
        font-size: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center; */
    }
    .theme--light.v-application {
       /* background-color: var(--v-background-base, var(--v-background)) !important; */
    }
</style>
