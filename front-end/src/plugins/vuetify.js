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
