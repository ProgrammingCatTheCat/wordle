<script setup>
import { useRouter, useRoute } from 'vue-router'
import { ref, watch } from 'vue'

const showHeader = ref(true);


const router = useRouter() // create router instance
const route = useRoute();

watch(
    () => route.name,
    (newName) => {
      showHeader.value = (newName !== 'Wordle');
    },
    { immediate: true }
);

function navigateToPlay() {
  console.log("navigate to play")
   router.push({ name: 'Wordle' })
}
</script>

<template>
  <div>
    <div v-if  = "showHeader" class = "header">

    <div class ="welcome">
      <!-- <img id = "logo" src="./assets/logo.png" alt="Logo Image" class="example-image" @click="navigateToMain"/> -->
      <h1 id="title">Hi, this is Wordle game. Have fun!</h1>
    </div>

    <div class="button-container">
      <button id="login" class="button-style" @click="navigateToLogin">log in</button>
      <button id="register" class="button-style" @click="navigateToRegister">register</button>
      <button id="logout" class="button-style" @click="logout">log out</button>
      <button id="play" class="button-style" @click="navigateToPlay">just play</button>
    </div>
  </div>
  <router-view></router-view>
  </div>
</template>

<style scoped>
.welcome { 
  width: 100%;
  text-align: center;
  margin-top: 20px;
}

.button-container {
  /* position: flex; */
  margin-top: 20px;
  width: 100%;
  display: flex;
  justify-content: center;
}
</style>
