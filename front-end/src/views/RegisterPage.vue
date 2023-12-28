<script setup>
import { ref } from 'vue'

const errorMessage = ref('')
</script>

<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card max-width="344" class="mt-10">
          <v-card-title>
            <div class="my-2 text-center">
              <v-img src="/static/images/logo.png" class="logo"></v-img>
            </div>
          </v-card-title>
          <v-divider class="mx-2" thickness="4" color="deep-purple"></v-divider>
          <v-card-subtitle>
            <div class="my-2 text-center">Open source task management tool</div>
          </v-card-subtitle>
          <v-card-text>
            <v-alert v-if="errorMessage" icon="mdi-alert-circle-outline" density="compact" color="deep-orange-lighten-5" border>
              {{errorMessage}}
            </v-alert>
            <v-form>
              <v-text-field label="Username" prepend-inner-icon="mdi-account-circle-outline" variant="underlined" required></v-text-field>
              <v-text-field label="Email Address" prepend-inner-icon="mdi-email-outline" variant="underlined" required></v-text-field>
              <v-text-field label="Password" prepend-inner-icon="mdi-lock-outline" variant="underlined" type="password" required></v-text-field>
              <v-btn color="primary" block>Create Account</v-btn>
            </v-form>
            <div class="ma-2 text-left">
              <p>By clicking “Create account”, you agree to our <a href="#">terms of service</a> and <a href="#">privacy policy</a>.</p>
            </div>
            <div class="text-center mt-7">
              <p>Already have an account? <a href="/login">Sign in</a></p>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
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
</template>

<style scoped>

.logo {
  max-width: 150px;
  margin: 0 auto;
}
</style>
