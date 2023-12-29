import { describe, it, expect, beforeEach } from 'vitest'

import { mount } from '@vue/test-utils'
import { createRouter, createWebHistory } from 'vue-router'

import RegisterPage from '../RegisterPage.vue'
import vuetify from '../../plugins/vuetify'

// eslint-disable-next-line no-undef
global.ResizeObserver = require('resize-observer-polyfill')

describe('RegisterPage', () => {
  let wrapper

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
  })

  it('초기 화면 구성요소 유효성 검사', () => {
    expect(wrapper.find('img').attributes().src).toEqual('/static/images/logo.png')
    expect(wrapper.find('.v-alert').exists()).toBe(false)
  })
})
