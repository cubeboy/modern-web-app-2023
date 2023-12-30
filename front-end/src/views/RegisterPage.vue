<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

import registrationService from '../services/registration'

const router = useRouter()

const errorMessage = ref('')
const form = reactive({
  username: '',
  emailAddress: '',
  password: ''
})
const formValid = ref(false)

const USERNAME_LENGTH_MIN = 4
const USERNAME_LENGTH_MAX = 20
const EMAILADDRESS_LENGTH_MAX = 100
const PASSWORD_LENGTH_MIN = 6
const PASSWROD_LENGTH_MAX = 128
const formRules = reactive({
  username: [
    (value) => {
      if (value.trim() != '') return true
      return 'Username is required'
    },
    (value) => {
      if (!/[^a-z0-9]/.test(value)) return true
      return 'Username can only contain letters and numbers'
    },
    (value) => {
      if (value.length >= USERNAME_LENGTH_MIN && value.length <= USERNAME_LENGTH_MAX) return true
      return `Username must have at least ${USERNAME_LENGTH_MIN} and to maximum ${USERNAME_LENGTH_MAX}`
    }
  ],
  emailAddress: [
    (value) => {
      if (value.trim() != '') return true
      return 'Email address is required'
    },
    (value) => {
      if (/[a-z0-9]+@[a-z]+\.[a-z]{2,3}/.test(value)) return true
      return 'This is not a valid email address'
    },
    (value) => {
      if (value.trim().length <= EMAILADDRESS_LENGTH_MAX) return true
      return `Email address is too long. It can contains maximium ${EMAILADDRESS_LENGTH_MAX} letters.`
    }
  ],
  password: [
    (value) => {
      if (value.trim() != '') return true
      return 'Password is required'
    },
    (value) => {
      if (/^(?=(?:.*[A-Za-z]){2})(?=.*\d).{6,128}$/.test(value)) return true
      return `Password must have at least ${PASSWORD_LENGTH_MIN} and to maximum ${PASSWROD_LENGTH_MAX}, must contains big charter or small charter and number`
    }
  ]
})

const submitForm = async () => {
  if (!formValid.value) return

  return registrationService.register(form)
    .then(() => {
      router.push({ name: 'LoginPage' })
    })
    .catch(error => {
      errorMessage.value = 'Failed to register user. ' + error.message
    })
}
</script>

<template>
  <v-container >
    <v-row justify="center">
      <v-col cols="auto">
        <v-card max-width="320" class="mt-10">
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
            <v-form @submit.prevent="submitForm()" v-model="formValid">
              <v-text-field
                id="username"
                v-model="form.username"
                :rules="formRules.username"
                label="Username"
                prepend-inner-icon="mdi-account-box-outline"
                variant="underlined" required>
              </v-text-field>
              <v-text-field
                id="emailAddress"
                v-model="form.emailAddress"
                :rules="formRules.emailAddress"
                label="Email Address"
                prepend-inner-icon="mdi-email-outline"
                variant="underlined" required>
              </v-text-field>
              <v-text-field
                id="password"
                v-model="form.password"
                :rules="formRules.password"
                label="Password"
                prepend-inner-icon="mdi-lock-outline"
                variant="underlined"
                type="password"
                required>
              </v-text-field>
              <v-btn color="primary" type="submit" block>Create Account</v-btn>
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
</template>

<style scoped>

.logo {
  max-width: 150px;
  margin: 0 auto;
}
</style>
