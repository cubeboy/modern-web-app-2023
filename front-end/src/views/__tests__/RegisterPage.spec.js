import { describe, it, expect, beforeEach } from 'vitest'

import { mount } from '@vue/test-utils'
import { createRouter, createWebHistory } from 'vue-router'

import RegisterPage from '../RegisterPage.vue'
import vuetify from '../../plugins/vuetify'

// eslint-disable-next-line no-undef
global.ResizeObserver = require('resize-observer-polyfill')

describe('RegisterPage', () => {
  let wrapper
  let usernameField
  let emailAddressField
  let passwordField

  const router = createRouter({
    history: createWebHistory(),
    routes: []
  })

  beforeEach(() => {
    wrapper = mount(RegisterPage, {
      global: {
        plugins: [vuetify, router]
      }
    })
    usernameField = wrapper.find('#username')
    emailAddressField = wrapper.find('#emailAddress')
    passwordField = wrapper.find('#password')
  })

  it('오류 메시지  초기 표시는 비활성', () => {
    expect(wrapper.find('img').attributes().src).toEqual('/static/images/logo.png')
    expect(wrapper.find('.v-alert').exists()).toBe(false)
  })

  it('모든 필드 초기 입력 값은 빈 값', () => {
    expect(usernameField.element.value).toEqual('')
    expect(emailAddressField.element.value).toEqual('')
    expect(passwordField.element.value).toEqual('')
  })

  it('데이터 모델 필드와 화면 필드의 값이 일치', async () => {
    const txtUsername = 'testuser'
    const txtEmailAddress = 'testuser@taskagile.com'
    const txtPassword = 'password123'

    const form = wrapper.vm.form
    expect(form.username).toEqual('')
    expect(form.emailAddress).toEqual('')
    expect(form.password).toEqual('')

    form.username = txtUsername
    form.emailAddress = txtEmailAddress
    form.password = txtPassword

    await wrapper.vm.$nextTick()

    expect(usernameField.element.value).toEqual(txtUsername)
    expect(emailAddressField.element.value).toEqual(txtEmailAddress)
    expect(passwordField.element.value).toEqual(txtPassword)
  })
})
