import { describe, it, vi, expect, beforeEach, afterEach } from 'vitest'

import { mount } from '@vue/test-utils'
import { createRouter, createWebHistory } from 'vue-router'
import moxios from 'moxios'
import defaultAxios from '@/plugins/defaultAxios'

import vuetify from '../../plugins/vuetify'
import RegisterPage from '../RegisterPage.vue'
import registrationService from '@/services/registration'

// eslint-disable-next-line no-undef
global.ResizeObserver = require('resize-observer-polyfill')

describe('RegisterPage', () => {
  let wrapper
  let usernameField
  let emailAddressField
  let passwordField
  let submitBtn
  let vForm
  let registerSpy

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
    submitBtn = wrapper.find('form button[type="submit"]')
    vForm = wrapper.findComponent({name: 'v-form'})
    registerSpy = vi.spyOn(registrationService, 'register')

    moxios.install(defaultAxios)
  })

  afterEach(() => {
    registerSpy.mockReset()
    registerSpy.mockRestore()

    moxios.uninstall(defaultAxios)
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
    expect(wrapper.vm.errorMessage).toEqual('')

    form.username = txtUsername
    form.emailAddress = txtEmailAddress
    form.password = txtPassword

    await wrapper.vm.$nextTick()

    expect(usernameField.element.value).toEqual(txtUsername)
    expect(emailAddressField.element.value).toEqual(txtEmailAddress)
    expect(passwordField.element.value).toEqual(txtPassword)
  })

  it('회원가입 버튼 클릭, 이벤트 메소드 호출', () => {
    const stub = vi.fn()
    wrapper.vm.submitForm = stub
    submitBtn.trigger('submit')
    expect(stub).toBeCalled()
  })

  it('username 유효성 검사', async () => {
    const txtEmailAddress = 'testuser@taskagile.com'
    const txtPassword = 'password123'

    const form = wrapper.vm.form

    form.emailAddress = txtEmailAddress
    form.password = txtPassword

    form.username = ''
    await wrapper.vm.$nextTick()
    let valid = await vForm.vm.validate()
    let usernameError = valid.errors.find(obj => obj.id === 'username')
    expect(valid.valid).toBe(false)
    expect(usernameError).not.toBeUndefined()
    expect(usernameError.errorMessages).toContain('Username is required')

    form.username = 'user!@'
    await wrapper.vm.$nextTick()
    valid = await vForm.vm.validate()
    usernameError = valid.errors.find(obj => obj.id === 'username')
    expect(valid.valid).toBe(false)
    expect(usernameError).not.toBeUndefined()
    expect(usernameError.errorMessages).toContain('Username can only contain letters and numbers')

    form.username = 'username12345678901234567890'
    await wrapper.vm.$nextTick()
    valid = await vForm.vm.validate()
    usernameError = valid.errors.find(obj => obj.id === 'username')
    expect(valid.valid).toBe(false)
    expect(usernameError).not.toBeUndefined()
    expect(usernameError.errorMessages).toContain('Username must have at least 4 and to maximum 20')
    expect(registerSpy).not.toHaveBeenCalled()
  })

  it('emailAddress 유효성 검사', async () => {
    const txtUsername = 'testuser'
    const txtPassword = 'password123'

    const form = wrapper.vm.form

    form.username = txtUsername
    form.password = txtPassword

    form.emailAddress = ''
    await wrapper.vm.$nextTick()
    let valid = await vForm.vm.validate()
    let emailAddressError = valid.errors.find(obj => obj.id === 'emailAddress')
    expect(valid.valid).toBe(false)
    expect(emailAddressError).not.toBeUndefined()
    expect(emailAddressError.errorMessages).toContain('Email address is required')

    form.emailAddress = 'bad-email@'
    await wrapper.vm.$nextTick()
    valid = await vForm.vm.validate()
    emailAddressError = valid.errors.find(obj => obj.id === 'emailAddress')
    expect(valid.valid).toBe(false)
    expect(emailAddressError).not.toBeUndefined()
    expect(emailAddressError.errorMessages).toContain('This is not a valid email address')

    form.emailAddress = '@baddomain.com'
    await wrapper.vm.$nextTick()
    valid = await vForm.vm.validate()
    emailAddressError = valid.errors.find(obj => obj.id === 'emailAddress')
    expect(valid.valid).toBe(false)
    expect(emailAddressError).not.toBeUndefined()
    expect(emailAddressError.errorMessages).toContain('This is not a valid email address')
    expect(registerSpy).not.toHaveBeenCalled()
  })

  it('password 유효성 검사', async () => {
    const txtUsername = 'testuser'
    const txtEmailAddress = 'testuser@taskagile'

    const form = wrapper.vm.form

    form.username = txtUsername
    form.emailAddress = txtEmailAddress

    form.password = ''
    await wrapper.vm.$nextTick()
    let valid = await vForm.vm.validate()
    let passwordError = valid.errors.find(obj => obj.id === 'password')
    expect(valid.valid).toBe(false)
    expect(passwordError).not.toBeUndefined()
    expect(passwordError.errorMessages).toContain('Password is required')

    form.password = 'abc12'
    await wrapper.vm.$nextTick()
    valid = await vForm.vm.validate()
    passwordError = valid.errors.find(obj => obj.id === 'password')
    expect(valid.valid).toBe(false)
    expect(passwordError).not.toBeUndefined()
    expect(passwordError.errorMessages).toContain('Password must have at least 6 and to maximum 128, must contains big charter or small charter and number')

    form.password = 'abcdefghij'
    await wrapper.vm.$nextTick()
    valid = await vForm.vm.validate()
    passwordError = valid.errors.find(obj => obj.id === 'password')
    expect(valid.valid).toBe(false)
    expect(passwordError).not.toBeUndefined()
    expect(passwordError.errorMessages).toContain('Password must have at least 6 and to maximum 128, must contains big charter or small charter and number')
    expect(registerSpy).not.toHaveBeenCalled()
  })

  it('username 중복 검사', async () => {
    const txtUsername = 'duplication'
    const txtEmailAddress = 'testuser@taskagile.com'
    const txtPassword = 'passwrod12'

    moxios.stubRequest('/registrations', {
      status: 409,
      response: {
        message: 'username is already exists.'
      }
    })

    const form = wrapper.vm.form
    form.username = txtUsername
    form.emailAddress = txtEmailAddress
    form.password = txtPassword
    await wrapper.vm.$nextTick()
    let {valid} = await vForm.vm.validate()
    expect(valid).toBe(true)

    await wrapper.vm.submitForm()
    expect(registerSpy).toHaveBeenCalledWith(form)
    await wrapper.vm.$nextTick()

    expect(wrapper.find('.v-alert').exists()).toBe(true)
    expect(wrapper.vm.errorMessage).toEqual('Failed to register user. username is already exists.')
  })

  it('사용자 회원 등록 성공', async () => {
    const txtUsername = 'registeruser'
    const txtEmailAddress = 'testuser@taskagile.com'
    const txtPassword = 'Passwrod12!@'

    moxios.stubRequest('/registrations', {
      status: 201,
      response: {
        message: 'success'
      }
    })

    const stub = vi.fn()
    router.push = stub

    const form = wrapper.vm.form
    form.username = txtUsername
    form.emailAddress = txtEmailAddress
    form.password = txtPassword
    await wrapper.vm.$nextTick()
    let {valid} = await vForm.vm.validate()
    expect(valid).toBe(true)

    await wrapper.vm.submitForm()
    expect(registerSpy).toHaveBeenCalledWith(form)
    await wrapper.vm.$nextTick()

    console.log(`v-laert exists ===>> ${wrapper.vm.errorMessage}`)
    expect(wrapper.find('.v-alert').exists()).toBe(false)
    expect(stub).toHaveBeenCalledWith({ name: 'LoginPage' })
  })
})
