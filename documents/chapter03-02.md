# create front-end scaffold with vue3 & vuetify3
---
front-end 프로젝트는 vue3 에서 새롭게 제공하는 Composition API 를 사용 한다.   
Compostion API는 보다 유연하고 재사용 가능한 코드를 작성 할 수 있도록 제안 된 새로운 형태의 vue 에서의 javascript 구현 방식이다.   
자세한 설명은 다음 링크를 참조 하도록 한다. [컴포지션 API FAQ](https://ko.vuejs.org/guide/extras/composition-api-faq.html)   
빠르게 프로젝트의 뼈대를 만들기 위해서 아래의 명력어를 입력한다.   
```bash
npm init vue@latest
```
입력후 나오는 구성 선택지는 아래 내용대로 선택한다.   
```
✔ Project name: … front-end
✔ Add TypeScript? … No
✔ Add JSX Support? … No
✔ Add Vue Router for Single Page Application development? … Yes
✔ Add Pinia for state management? … Yes
✔ Add Vitest for Unit Testing? … Yes
✔ Add an End-to-End Testing Solution? › Playwright
✔ Add ESLint for code quality? … Yes
✔ Add Prettier for code formatting? … Yes
```

Vitest 는 java 의 Junit 과 같은 javascript unit test framework 이다.   
이 프로젝트에서 만드는 모든 화면기능 (ViewPage and Components)들은 vites 를 이용한 단위 테스트를 작성 한다.   
Playwright 는 front-end 와 back-end, database 를 관통하는 통합 테스트 프레임 워크이다.    
흐름상 중요하다고 판단 되는 데이터 흐름은 자동화된 e2e 테스트를 제공한다.

프로젝트가 생성 되었다면 아래 명령어를 입력해서 생성된 프로젝트를 확인 한다.   
```
cd front-end
npm install
npm run dev
```

이제 프로젝트에서 화면 디자인에 사용할 컴포넌트인 Vuetify 를 설치 한다.   
vue.js 의 3.x 버전이 나오면서 Vuetify3 버전이 별도로 제공된다.   
vue2 버전을 사용할때는 반드시 이전 버전의 [Vuetify2](https://v2.vuetifyjs.com/) 를 사용하도록 한다.   
Vuetify 는 Vue.js 프레임워크를 위한 Material Design 콤포넌트 라이브러리이다.   
Google 의 Meterial Design 사양을 기반으로하여, Vue.js App 을 위한 풍부한 UI 콤포넌트를 제공한다.   
풍부한 컴포넌트 세트를 제공하며 높은 생산성과 반응형 디자인을 제공한다.   
하지만 일정한 학습곡선이 필요하며 많은 기능을 포함한다는 장점이 오히려 App 이 무거워 질 수 있다는 단점으로 될 수 있다.   
자세한 내용은 공식 사이트의 문서를 확인 한다.   
[Vuetify - Vue Component Framework](https://vuetifyjs.com/)   
   
아래 명령어로 먼저 vuetify와 @mdi/font 를 설치 한다.
```
npm install @mdi/font
npm install -D vuetify vite-plugin-vuetify resize-observer-polyfill
```

front-end/src 폴더 아래에 plugins 폴더와 vuetify.js 파일을 만들고 아래 내용을 입력한다.   
```javascript
import { createVuetify } from 'vuetify'
import 'vuetify/dist/vuetify.min.css' // Vuetify 스타일 시트를 추가합니다.
import '@mdi/font/css/materialdesignicons.min.css' // Material Design Icons 아이콘 폰트를 추가합니다.

import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

export default createVuetify({
  components,
  directives,
  icons: {
    iconfont: 'mdi'
  }
})
```
   
플러그인 설정이 되었다면 App 전역에서 사용할 수 있도록 등록 과정을 거쳐야 한다.   
main.js 를 아래와 같이 수정 한다.   
```javascript
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import vuetify from './plugins/vuetify'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(vuetify)

app.mount('#app')
```
마지막으로 scaffold 과정에서 자동으로 만들어진 코드를 모두 정리한다.   
App.vue 를 아래 처럼 초기화 한다.   
```javascript
<script setup>

</script>

<template>
  <div id="root">
    Task Agile
  </div>
</template>
```

router/index.js 파일의 내용을 아래 처럼 초기화 한다.   
```javascript
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
  ]
})

export default router
```
이제 src 폴더 하위의 assets, components, views 폴더 내의 모든 파일을 삭제 한다.   
   
지금부터 Application 을 작성할 기초작업이 준비 되었다.
