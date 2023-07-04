<template>
  <aside>
    <div class="aside-wrapper">
      <img :src="defaultLogo" />
      <h3>
        <b>openapi 开放平台</b>
      </h3>
    </div>
  </aside>
</template>

<script setup>
import defaultLogo from "@/assets/logo/logo.jpg"
</script>

<style lang="scss" scoped>
aside {
  width: 100%;
  height: inherit;
  padding: 0;
  background: none;
  margin: auto;
  text-shadow: 0 1px 1px hsla(0, 0%, 100%, 0.3);
  box-shadow: 0 0 0 1px hsla(0, 0%, 100%, 0.3) inset, 0 0.3em 1em rgba(0, 0, 0, 0.12);
}

.aside-wrapper::before {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: -1;
  filter: blur(10px);
  margin: -30px;
}
.aside-wrapper {
  display: flex;
  height: inherit;
  flex-flow: column nowrap;
  justify-content: center;
  align-items: center;
  color: #000;
  hyphens: auto;
  background: hsla(0, 0%, 100%, 0.5);
  overflow: hidden;
  position: relative;
  > img {
    width: 60px;
    height: 60px;
    border-radius: 50%;
  }
  h5 {
    cite {
      font-style: normal;
    }
  }
}
</style>
