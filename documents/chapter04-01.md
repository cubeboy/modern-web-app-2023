# front-end unit test by vites
---
javascript unit test framework 는 jest, MochaJS 등이 있다.
vites 는 비교적 최근에 등장한 vite 기반의 test framework 이며 빠른 속도와 간편한 설정, 풍부한 기능 지원 등이 장점이다.   
이 프로젝트는 vue.js 에 적용하지만 react 에서도 사용할 수 있는 범용 적인 Test Framework 도구이다.   
앞서 front-end 코드 프로젝트를 scaffolding 하면서 vites 를 이용한 테스트를 위한 설정 작업은 모두 준비되었다.   
하지만 이 프로젝트에서는 vuetify component 를 사용하고 있기 때문에 한가지 추가 작업이 필요하다.   
vitest.config.js 파일을 열어서 아래와 같이 구성을 추가한다.
```javascript
export default mergeConfig(
  viteConfig,
  defineConfig({
    test: {
      environment: 'jsdom',
      exclude: [...configDefaults.exclude, 'e2e/*'],
      root: fileURLToPath(new URL('./', import.meta.url)),
      deps: {
        inline: ['vuetify']
      }
    }
  })
)
```
이전에 초기 프로젝트 구성에서 main.js 에 vuetify 를 전역으로 사용할 수 있도록 구성을 하였다.   
단위 테스트에서는 전체 App 를 로드하지 않기 때문에 main.js 의 구성이 적용되지 않는다.   
단위 테스트에서 vuetify 가 적용되게 하기위한 구성을 추가 한다.   
   
회원 가입 페이지(RegisterPage.vue)가 통과 해야할 테스트 요구사항은 아래와 같다.   
> 가입중 오류 메시지 표시부분은 초기에 비활성 상태여야 한다.   
> 초기 화면에서는 모든 필드 입력 값이 빈 값이어야 한다.   
> 데이터 모델 필드와 화면 필드의 값이 일치 해야 한다.   
> 회원가입 버튼을 클릭했을때 회원가입 이벤트가 발생 한다.   
> 회원가입 이벤트가 발생 했을 때 입력 필드들의 유효성 검사가 발생 한다.
> 입력 필드 유효성 검사에 실패 했을 때는 오류 메시지가 표시 되어야 한다.
> username 은 최소 4 글자 이상, 최대 20자 이하 이며 필수 입력이다.   
> emailAddress 는 최소 6 글자 이상, 최대 100자 이하 이며 필수입력이다.   
> emailAddress [계정@도메인] 형태의 email format 을 지켜야 한다.   
> password 는 최소 6자 이상 최대 128자 이하이며 필수입력이다.   
> password 는 영문자 1개 이상 과 숫자 1개 이상이 반드시 포함되어야 한다.   
> usrname, emailAddress 는 전체 App 에서 유일 해야 하며 중복되는 경우 명확한 오류 메시지가 표시 되어야 한다.   
> 기타 회원 가입중 오류가 발생 하는 경우 오류 메시지를 표시 한다.   

위와 같은 테스트 요구사항을 만족하기 위한 테스트 코드를 만든다.   

src/views/RegisterPage.vue 의 테스트를 만들기 위해 파일이 있는 위치에 __tests__ 폴더를 만들고 RegisterPage.spec.js 파일을 만들고 아래와 같이 테스트 코드를 작성한다.
```javascript
import { describe, it, expect, beforeEach } from 'vitest'

import { mount } from '@vue/test-utils'
import { createRouter, createWebHistory } from 'vue-router'

import RegisterPage from '../RegisterPage.vue'
// vuetify plugin 로딩
import vuetify from '../../plugins/vuetify'

// vuetify layout component 들의 Resize 이벤트 오류를 방지
// eslint-disable-next-line no-undef
global.ResizeObserver = require('resize-observer-polyfill')

describe('RegisterPage', () => {
  let wrapper

// unit test 를 위한 가상 라우터 설정
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
```
위의 작업 진행 후 아래 명령어를 입력해서 unit test 를 실행 한다.   
```bash
npm run test:unit
```
정상적으로 구성 되었다면 아래와 같은 내용이 출력 된다.   
```bash
 ✓ src/views/__tests__/RegisterPage.spec.js (1)
   ✓ RegisterPage (1)
     ✓ 초기 화면 구성요소 유효성 검사

 Test Files  1 passed (1)
      Tests  1 passed (1)
   Start at  11:22:06
   Duration  1.24s
```
vitest 는 한번 실행해 두면 code 를 수정하고 저장 할 때 마다 자동으로 테스트를 수행한다.   
   
첫번째 테스트로 v-alert 이 화면상에 있는지 확인 하는 코드를 추가했다.   
> 가입중 오류 메시지 표시부분은 초기에 비활성 상태여야 한다.   

테스트 코드를 작성 할 때는 전체 기능을 한번에 테스트 하지 말고 최소한의 기능 단위로 테스트 코드를 작성하게 해서 가능하면 작은 단위로 테스를 수행 할 수 있도록 한다.   
테스트 코드는 한번 작성하는 것으로 끝나지 않고 App 가 유지되고 있는 동안 버그 수정이나 기능이 추가 될때마다 테스트도 더욱 정밀하게 유지 될 수 있도록 해야 한다.   
'초기 화면 구성요소 유효성 검사' 항목은 '오류 메시지  초기 표시는 비활성' 과 '모든 필드 초기 입력 값은 빈 값' 항목으로 분리해서 진행 하도록 한다.   
   
화면상의 각 필드 초기값을 점검하는 코드를 추가 한다.
```javascript

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
})
```
RegisterPage 를 객체화 하고 객체 내부에서 html element 를 찾는 메소드(find)의 작동은 CSS Selector 을 사용한다.   
> 초기 화면에서는 모든 필드 입력 값이 빈 값이어야 한다.   
   
이제 데이터 모델을 추가하고 초기 설정을 추가한다.
이후에 데이터 모델을 화면 요소에 바인딩 하고 입력값 유효성 테스트 코드는 chapter04 branch를 참조한다.
> 데이터 모델 필드와 화면 필드의 값이 일치 해야 한다.   
> 회원가입 버튼을 클릭했을때 회원가입 이벤트가 발생 한다.   
> 회원가입 이벤트가 발생 했을 때 입력 필드들의 유효성 검사가 발생 한다.
> 입력 필드 유효성 검사에 실패 했을 때는 오류 메시지가 표시 되어야 한다.
> username 은 최소 4 글자 이상, 최대 20자 이하 이며 필수 입력이다.   
> emailAddress 는 최소 6 글자 이상, 최대 100자 이하 이며 필수입력이다.   
> emailAddress [계정@도메인] 형태의 email format 을 지켜야 한다.   
> password 는 최소 6자 이상 최대 128자 이하이며 필수입력이다.   
> password 는 영문자 1개 이상 과 숫자 1개 이상이 반드시 포함되어야 한다.   

이제 username과 emailAddress 의 중복을 처리 할 차례이다.   
database 의 정보를 확인 해야 하는 동작은 client 측의 javascript 코드로는 검증 할 수없다.   
하지만 반환 값에 대한 약속만 정해 진다면 오류가 발생하는 경우를 가상으로 구현해서 테스트 할 수 있다.   
단위 테스트 단계에서는 잘되거나 잘못되는 경우를 mockup 으로 구현하고 실제 서버 통신과에서의 처리 결과 학인은 e2e 테스트 단계에서 만족 시킨다.   

먼저 api 통신을 가상화 해주는 axios와 axios mockup 라이브러리를 설치하자.   
네이티브 javascript method 인 fetch를 사용할 수도 있지만 다양한 브라우저 지원과 추가적인 보안기능 (XSRF Proejction), json변환 기능등을 지원 하는 axios 가 여러 편리 함을 제공 하므로 axios 를 사용한다.   
moxios 는 axios의 api 호출을 가로 채서 목업 데이터로 반환 하게 해주는 테스트용 핼퍼 라이브러리이다.

아래 명령어로 설치하자.   
```bash
npm install axios --save
npm install -D moxios
```
axois 로 api 를 호출 시 해야 하는 반복 작업을 미리 정의하는 커스텀 axios 를 정의한다.   
src/plugins/defaultAxios.js 파일을 만들고 아래와 같이 정의 한다.   
```javascript
import axios from 'axios'

const defaultAxios = axios.create(
  {
    baseURL: '/api'
  }
)
defaultAxios.defaults.headers.common.Accept = 'application/json'
defaultAxios.interceptors.response.use(
  (response) => response,
  (error) => {
    return Promise.reject(error)
  }
)

export default defaultAxios
```
src/services/registration.js 파일을 만들고 back-end api 서비스를 호출하는 기능을 정의한다.
```javascript
import defaultAxios from '../plugins/defaultAxios'

export default {
  register: (userInfo) => {
    return new Promise((resolve, reject) => {
      defaultAxios.post('/registrations', userInfo)
        .then(({ data }) => {
          resolve(data)
        })
        .catch((error) => {
          reject(error.response.data)
        })
    })
  }
}
```
앞서 정의한 defaultAxios 적용 된 부분을 주목하자.   
지금은 이 코드가 제대로 작동하는지는 관심 갖지 말자.   
아직은 back-end 서비스가 구현 되지 않았으니 확인할 방법도 없다.   
   
이제 다시 테스트 코드로 돌아 와서 moxios 를 이용한 코드 작업을 하자.   
```javascript
import { describe, it, vi, expect, beforeEach, afterEach } from 'vitest'
import moxios from 'moxios'
import defaultAxios from '@/plugins/defaultAxios'
// ... 생략
describe('RegisterPage', () => {
  // ... 생략
  let registerSpy

  beforeEach(() => {
    // ... 생략
    registerSpy = vi.spyOn(registrationService, 'register')

    moxios.install(defaultAxios)
  })

  afterEach(() => {
    registerSpy.mockReset()
    registerSpy.mockRestore()

    moxios.uninstall(defaultAxios)
  })
})
```
새롭게 추가된 spyOn 이라는 메소드가 있다.
spy 는 특정 객체의 특정 메소드의 호출 상태를 감시 하는 기능을 제공한다.
테스트를 할때는 RegisterPage 가 제공하는 submitForm 을 호출하지만 내부에서 호출되는 registrationService.register 메소드의 호출 상태는 확인 할 수 없다.   
spy 는 해당 메소들르 추적 감시해서 메소드의 호출 여부를 확인 할 수 있게 해준다.   
사용법은 RegisterPage.spec.js 의 registerSpy 의 상태를 확인하는 코드를 참조 한다.   

moxios 사용하기 위해서는 명시적으로 axios 호출을 가로채도록 선언 해 주어야 하며 install 메소드는 이를 명시적으로 처리하는 함수이다.   
afterEach 에서 spy 와 moxios 를 reset 또는 uninstall 해 주는 것은 다른 테스트 진행에 영향을 주지 않도록 하기 위해서 테스트 완료 후 초기화 해 주는 작업이다.   
   
이제 [username 중복검사] 를 확인 해 보자.   
가장 중요한 코드는 아래 코드이다.
```javascript
    moxios.stubRequest('/registrations', {
      status: 409,
      response: {
        message: 'username is already exists.'
      }
    })
```
'/registrations' REST Api 호출에 대한 가상 결과를 위와 같이 정의 한다.
이제 registrationService.register 호출에 대해서는 http status 409 와 {message: 'username is already exists.'} 가 반드시 반환 된다.   
이제 호출 결과에 대해서 reject 가 호출 되는지, 오류 메시지 표시 위치에 메시지가 표시되는지 확인 할 수 있다.   
> usrname, emailAddress 는 전체 App 에서 유일 해야 하며 중복되는 경우 명확한 오류 메시지가 표시 되어야 한다.   
> 기타 회원 가입중 오류가 발생 하는 경우 오류 메시지를 표시 한다.   
   
이제 목표한 오류 검증 사항에 대해서 테스트가 완료 되었으니 마지막으로 정상적으로 사용자 등록 처리를 확인 하고 마무리 하자.
   
