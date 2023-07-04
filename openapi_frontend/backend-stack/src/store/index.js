import { createPersistedState } from "pinia-plugin-persistedstate"

const store = createPinia()
store.use(
  createPersistedState({
    auto: true,
  }),
)

export default store
