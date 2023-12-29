<script setup>
import {RouterView} from 'vue-router'
</script>

<template>
  <div id="root">
    <v-app id="landingZone">
      <RouterView />
      <v-footer app class="text-caption" color="blue-grey-lighten-5">
        <v-container>
          <v-row justify="space-between">
            <v-col cols="auto">
              <span>&copy; 2023 TaskAgile.com</span>
            </v-col>
            <v-col cols="auto">
              <v-row>
                <v-col cols="auto"><a href="#">About</a></v-col>
                <v-col cols="auto"><a href="#">Terms of Service</a></v-col>
                <v-col cols="auto"><a href="#">Privacy Policy</a></v-col>
                <v-col cols="auto"><a href="https://github.com/cubeboy/modern-web-app-2023" target="_blank">GitHub</a></v-col>
              </v-row>
            </v-col>
          </v-row>
        </v-container>
      </v-footer>
    </v-app>
  </div>
</template>
